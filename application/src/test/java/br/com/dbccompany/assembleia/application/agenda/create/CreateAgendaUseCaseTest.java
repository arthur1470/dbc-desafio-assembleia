package br.com.dbccompany.assembleia.application.agenda.create;

import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAgendaUseCaseTest {

    @InjectMocks
    private DefaultCreateAgendaUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateAgenda_shouldReturnAgendaId() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var aCommand = CreateAgendaCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(agendaGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(agendaGateway, times(1)).create(argThat(anAssociate ->
                Objects.equals(expectedName, anAssociate.getName())
                        && Objects.equals(expectedDescription, anAssociate.getDescription())
                        && Objects.equals(expectedIsActive, anAssociate.isActive())
                        && Objects.nonNull(anAssociate.getId())
                        && Objects.nonNull(anAssociate.getCreatedAt())
                        && Objects.nonNull(anAssociate.getUpdatedAt())
                        && Objects.isNull(anAssociate.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommandWithInactiveAgenda_whenCallsCreateAgenda_shouldReturnAgendaId() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = false;

        final var aCommand = CreateAgendaCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(agendaGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(agendaGateway, times(1)).create(argThat(anAssociate ->
                Objects.equals(expectedName, anAssociate.getName())
                        && Objects.equals(expectedDescription, anAssociate.getDescription())
                        && Objects.equals(expectedIsActive, anAssociate.isActive())
                        && Objects.nonNull(anAssociate.getId())
                        && Objects.nonNull(anAssociate.getCreatedAt())
                        && Objects.nonNull(anAssociate.getUpdatedAt())
                        && Objects.nonNull(anAssociate.getDeletedAt())
        ));
    }

    @Test
    void givenAnInvalidCommandWithNullName_whenCallsCreateAgenda_shouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateAgendaCommand.with(
                null,
                "Pauta muito importante",
                true
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(agendaGateway, times(0)).create(any());
    }


    @Test
    void givenAnInvalidCommandWithEmptyName_whenCallsCreateAgenda_shouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = CreateAgendaCommand.with(
                "  ",
                "Pauta muito importante",
                true
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidCommandWithNameLessThan3_whenCallsCreateAgenda_shouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var aCommand = CreateAgendaCommand.with(
                "12",
                "Pauta muito importante",
                true
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(agendaGateway, times(0)).create(any());
    }

    @Test
    void givenAnInvalidCommandWithNameMoreThan255_whenCallsCreateAgenda_shouldThrowDomainException() {
        // given
        final var anInvalidName = """
                dasmkdasdklasdkasklmdaskmldmksadasdasdaudhaiudhsuhffdsiojaioj
                fdsklnfdsafndslkfnasdlkfnksdlfnfdsijfjioasifjoadsijofdjisoafij
                fnsdfndskjfnaskdfndskjfnkjnfnsdkfsdfhidashufdsuhfhdsiufhsdiuf
                fnsdjkfndksajnfkdjsnfksdnkjfnajsfhsdfhsduhfisudfhiasdhfdshuhfu
                fnsdjkfndksajnfkdjsnfksdnkjfnajsfhsdfhsduhfisudfhiasdhfdshuhfu
                """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var aCommand = CreateAgendaCommand.with(
                anInvalidName,
                "Pauta muito importante",
                true
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(agendaGateway, times(0)).create(any());
    }
}