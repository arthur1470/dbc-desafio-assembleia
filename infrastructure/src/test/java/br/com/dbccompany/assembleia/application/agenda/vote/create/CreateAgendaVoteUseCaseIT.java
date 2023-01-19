package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@IntegrationTest
class CreateAgendaVoteUseCaseIT {

    @Autowired
    private CreateAgendaVoteUseCase useCase;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AssociateRepository associateRepository;
    @SpyBean
    private AgendaGateway agendaGateway;

    @Test
    void givenAValidCommand_whenCallsCreateAgendaVote_shouldReturnVoteId() {
        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(20));

        final var anAgendaId = anAgenda.getId();
        final var aSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = anAssociate.getId();

        final var expectedVotesSize = 1;

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        assertEquals(0, associateRepository.count());
        associateRepository.save(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aSessionId,
                anAssociateId,
                VoteType.YES
        );

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAgenda =
                agendaRepository.findById(anAgenda.getId().getValue()).get();

        assertNotNull(actualAgenda.getVoteSession());
        assertNotNull(actualAgenda.getVoteSession().getId());
        assertNotNull(actualAgenda.getVoteSession().getStartedAt());
        assertEquals(expectedVotesSize, actualAgenda.getVoteSession().getVotes().size());
    }

    @Test
    void givenAnInvalidAssociateIdNotStored_whenCallsCreateAgendaVote_shouldThrowNotFoundException() {
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(20));

        final var anAgendaId = anAgenda.getId();
        final var aSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = AssociateID.from("123");

        final var expectedErrorMessage = "Associate with ID 123 was not found";

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aSessionId,
                anAssociateId,
                VoteType.YES
        );

        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidAssociateInactive_whenCallsCreateAgendaVote_shouldThrowDomainException() {
        final var anAssociate =
                Associate.newAssociate("Joao", "12345678901", false);

        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(20));

        final var anAgendaId = anAgenda.getId();
        final var aSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = anAssociate.getId();

        final var expectedErrorMessage =
                "Associate %s is currently inactive".formatted(anAssociateId.getValue());

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        assertEquals(0, associateRepository.count());
        associateRepository.save(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aSessionId,
                anAssociateId,
                VoteType.YES
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidAssociateAlreadyVoted_whenCallsCreateAgendaVote_shouldThrowDomainException() {
        final var anAssociate =
                Associate.newAssociate("Joao", "12345678901", true);

        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(20))
                .addVote(Vote.newVote(VoteType.YES, anAssociate.getId()));

        final var anAgendaId = anAgenda.getId();
        final var aSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = anAssociate.getId();

        final var expectedErrorMessage =
                "Associate already voted for this agenda";

        assertEquals(0, associateRepository.count());
        associateRepository.save(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aSessionId,
                anAssociateId,
                VoteType.YES
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(agendaGateway, times(0)).create(any());
    }
}
