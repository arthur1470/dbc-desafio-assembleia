package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAgendaVoteUseCaseTest {

    @InjectMocks
    private DefaultCreateAgendaVoteUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;
    @Mock
    private AssociateGateway associateGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
        Mockito.reset(associateGateway);
    }

    @Test
    void givenAValidCommandWithVoteYes_whenCallsCreateAgendaVote_shouldReturnVoteId() {
        // given
        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);

        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(30));

        final var expectedVotesCount = 1;

        final var anAgendaId = anAgenda.getId();
        final var aVoteSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = AssociateID.from("123");

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                VoteType.YES
        );

        when(associateGateway.findById(anAssociateId))
                .thenReturn(Optional.of(anAssociate));

        when(agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aVoteSessionId))
                .thenReturn(false);

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.of(anAgenda.createClone()));

        when(agendaGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(agendaGateway, times(1)).create(argThat(agenda ->
                Objects.equals(anAgenda.getName(), agenda.getName())
                        && Objects.equals(anAgenda.getDescription(), agenda.getDescription())
                        && Objects.equals(anAgenda.isActive(), agenda.isActive())
                        && Objects.equals(anAgenda.getId(), agenda.getId())
                        && Objects.equals(anAgenda.getCreatedAt(), agenda.getCreatedAt())
                        && anAgenda.getUpdatedAt().isBefore(agenda.getUpdatedAt())
                        && Objects.isNull(agenda.getDeletedAt())
                        && Objects.nonNull(agenda.getVoteSession())
                        && Objects.equals(aVoteSessionId, agenda.getVoteSession().getId())
                        && Objects.equals(anAgenda.getVoteSession().getStartedAt(), agenda.getVoteSession().getStartedAt())
                        && Objects.equals(anAgenda.getVoteSession().getEndedAt(), agenda.getVoteSession().getEndedAt())
                        && Objects.equals(expectedVotesCount, agenda.getVoteSession().getVotes().size())
                        && Objects.equals(VoteType.YES, agenda.getVoteSession().getVotes().stream().findFirst().get().getVote())
        ));
    }

    @Test
    void givenAValidCommandWithVoteNo_whenCallsCreateAgendaVote_shouldReturnVoteId() {
        // given
        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(30));

        final var expectedVotesCount = 1;

        final var anAgendaId = anAgenda.getId();
        final var aVoteSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = AssociateID.from("123");

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                VoteType.NO
        );

        when(associateGateway.findById(anAssociateId))
                .thenReturn(Optional.of(anAssociate));

        when(agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aVoteSessionId))
                .thenReturn(false);

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.of(anAgenda.createClone()));

        when(agendaGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(agendaGateway, times(1)).create(argThat(agenda ->
                Objects.equals(anAgenda.getName(), agenda.getName())
                        && Objects.equals(anAgenda.getDescription(), agenda.getDescription())
                        && Objects.equals(anAgenda.isActive(), agenda.isActive())
                        && Objects.equals(anAgenda.getId(), agenda.getId())
                        && Objects.equals(anAgenda.getCreatedAt(), agenda.getCreatedAt())
                        && anAgenda.getUpdatedAt().isBefore(agenda.getUpdatedAt())
                        && Objects.isNull(agenda.getDeletedAt())
                        && Objects.nonNull(agenda.getVoteSession())
                        && Objects.equals(aVoteSessionId, agenda.getVoteSession().getId())
                        && Objects.equals(anAgenda.getVoteSession().getStartedAt(), agenda.getVoteSession().getStartedAt())
                        && Objects.equals(anAgenda.getVoteSession().getEndedAt(), agenda.getVoteSession().getEndedAt())
                        && Objects.equals(expectedVotesCount, agenda.getVoteSession().getVotes().size())
                        && Objects.equals(VoteType.NO, agenda.getVoteSession().getVotes().stream().findFirst().get().getVote())
        ));
    }

    @Test
    void givenAInvalidCommandWithAssociateIdNotStored_whenCallsCreateAgendaVote_shouldThrowNotFoundException() {
        // given
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(30));

        final var anAgendaId = anAgenda.getId();
        final var aVoteSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = AssociateID.from("123");

        final var expectedErrorMessage = "Associate with ID 123 was not found";

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                VoteType.YES
        );

        when(associateGateway.findById(anAssociateId))
                .thenReturn(Optional.empty());

        // when
        final var actualOutput = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedErrorMessage, actualOutput.getMessage());

        verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAValidCommandWithAssociateAlreadyVoted_whenCallsCreateAgendaVote_shouldReturnVoteId() {
        // given
        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        final var anAgenda = Agenda.newAgenda("Pauta Importante", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(30));

        final var anAgendaId = anAgenda.getId();
        final var aVoteSessionId = anAgenda.getVoteSession().getId();
        final var anAssociateId = AssociateID.from("123");

        final var expectedErrorMessage = "Associate already voted for this agenda";

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                VoteType.NO
        );

        when(associateGateway.findById(anAssociateId))
                .thenReturn(Optional.of(anAssociate));

        when(agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aVoteSessionId))
                .thenReturn(true);

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnAgendaIdNotStored_whenCallsCreateAgendaVote_shouldThrowNotFoundException() {
        // given
        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        final var anAgendaId = AgendaID.from("123");
        final var aVoteSessionId = VoteSessionID.from("123");
        final var anAssociateId = AssociateID.from("123");

        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        final var aCommand = CreateAgendaVoteCommand.with(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                VoteType.YES
        );

        when(associateGateway.findById(anAssociateId))
                .thenReturn(Optional.of(anAssociate));

        when(agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aVoteSessionId))
                .thenReturn(false);

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(agendaGateway, times(0)).create(any());
    }
}