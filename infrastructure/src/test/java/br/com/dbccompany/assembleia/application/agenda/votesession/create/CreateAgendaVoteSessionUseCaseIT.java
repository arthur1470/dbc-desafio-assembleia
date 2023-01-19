package br.com.dbccompany.assembleia.application.agenda.votesession.create;


import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class CreateAgendaVoteSessionUseCaseIT {

    @Autowired
    private CreateAgendaVoteSessionUseCase useCase;
    @Autowired
    private AgendaRepository agendaRepository;
    @SpyBean
    private AgendaGateway agendaGateway;

    @Test
    void givenAValidCommand_whenCallsCreateAgendaVoteSession_shouldReturnVoteSessionId() {
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true);
        final var expectedEndedDate = InstantUtils.now().plusSeconds(20);

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                anAgenda.getId(),
                expectedEndedDate
        );

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAgenda =
                agendaRepository.findById(anAgenda.getId().getValue()).get();

        assertNotNull(actualAgenda.getVoteSession());
        assertNotNull(actualAgenda.getVoteSession().getId());
        assertNotNull(actualAgenda.getVoteSession().getStartedAt());
        assertEquals(0, actualAgenda.getVoteSession().getVotes().size());
        assertEquals(expectedEndedDate, actualAgenda.getVoteSession().getEndedAt());
    }

    @Test
    void givenAnAgendaWithVoteSessionAlreadyStarted_whenCallsCreateAgendaVoteSession_shouldReturnVoteSessionId() {
        final var expectedEndedDate = InstantUtils.now().plusSeconds(20);
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(expectedEndedDate);
        final var expectedErrorMessage = "This agenda already has a voting session";

        assertEquals(0, agendaRepository.count());
        agendaRepository.save(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                anAgenda.getId(),
                expectedEndedDate
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
