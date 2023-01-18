package br.com.dbccompany.assembleia.application.agenda.votesession.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
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
class CreateAgendaVoteSessionUseCaseTest {

    @InjectMocks
    private DefaultCreateAgendaVoteSessionUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateAgendaVoteSession_shouldReturnVoteSessionId() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive);

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                anAgenda.getId(),
                InstantUtils.now().plusSeconds(30)
        );

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.of(anAgenda));

        when(agendaGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(agendaGateway, times(1)).create(argThat(agenda ->
                Objects.equals(expectedName, agenda.getName())
                        && Objects.equals(expectedDescription, agenda.getDescription())
                        && Objects.equals(expectedIsActive, agenda.isActive())
                        && Objects.nonNull(agenda.getId())
                        && Objects.nonNull(agenda.getCreatedAt())
                        && Objects.nonNull(agenda.getUpdatedAt())
                        && Objects.isNull(agenda.getDeletedAt())
                        && Objects.nonNull(agenda.getVoteSession())
                        && Objects.nonNull(agenda.getVoteSession().getStartedAt())
                        && Objects.nonNull(agenda.getVoteSession().getEndedAt())
        ));
    }

    @Test
    void givenAValidCommandWithVoteSessionAlreadyStarted_whenCallsCreateAgendaVoteSession_shouldThrowDomainException() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
                .startVoteSession(InstantUtils.now().plusSeconds(30));

        final var expectedErrorMessage = "This agenda already has a voting session";

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                anAgenda.getId(),
                InstantUtils.now().plusSeconds(30)
        );

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.of(anAgenda));

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidIdNotStored_whenCallsCreateAgendaVoteSession_shouldThrowNotFoundException() {
        // given
        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                AgendaID.from("123"),
                InstantUtils.now().plusSeconds(30)
        );

        when(agendaGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(agendaGateway, times(0)).create(any());
    }
}