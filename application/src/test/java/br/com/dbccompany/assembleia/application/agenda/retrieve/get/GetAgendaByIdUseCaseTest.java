package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetAgendaByIdUseCaseTest {

    @InjectMocks
    private DefaultGetAgendaByIdUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
    }

    @Test
    void givenAValidId_whenCallsGetAgenda_shouldReturnAgenda() {
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = anAgenda.getId();

        Mockito.when(agendaGateway.findById(expectedId))
                .thenReturn(Optional.of(anAgenda.createClone()));

        final var actualAgenda = useCase.execute(expectedId.getValue());

        Mockito.verify(agendaGateway, Mockito.times(1)).findById(expectedId);

        assertEquals(expectedId.getValue(), actualAgenda.id());
        assertEquals(expectedName, actualAgenda.name());
        assertEquals(expectedDescription, actualAgenda.description());
        assertEquals(expectedIsActive, actualAgenda.isActive());
        assertEquals(anAgenda.getCreatedAt(), actualAgenda.createdAt());
        assertEquals(anAgenda.getUpdatedAt(), actualAgenda.updatedAt());
        assertEquals(anAgenda.getDeletedAt(), actualAgenda.deletedAt());
    }

    @Test
    void givenAnInvalidId_whenCallsGetAgenda_shouldReturnNotFound() {
        final var expectedId = AgendaID.from("123");
        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        Mockito.when(agendaGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = AgendaID.from("123");
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(agendaGateway.findById(expectedId))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}