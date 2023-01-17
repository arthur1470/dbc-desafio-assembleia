package br.com.dbccompany.assembleia.application.agenda.create;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
class CreateAgendaUseCaseIT {

    @Autowired
    private CreateAgendaUseCase useCase;
    @Autowired
    private AgendaRepository agendaRepository;
    @SpyBean
    private AgendaGateway agendaGateway;

    @Test
    void givenAValidCommand_whenCallsCreateAgenda_shouldReturnAgendaId() {
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var aCommand = CreateAgendaCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, agendaRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, agendaRepository.count());
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAssociate =
                agendaRepository.findById(actualOutput.id()).get();

        assertNotNull(actualAssociate.getId());
        assertEquals(expectedName, actualAssociate.getName());
        assertEquals(expectedDescription, actualAssociate.getDescription());
        assertEquals(expectedIsActive, actualAssociate.isActive());
        assertNotNull(actualAssociate.getCreatedAt());
        assertNotNull(actualAssociate.getUpdatedAt());
        assertNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveAgenda_whenCallsCreateAgenda_shouldReturnAgendaId() {
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = false;

        final var aCommand = CreateAgendaCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, agendaRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, agendaRepository.count());
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAssociate =
                agendaRepository.findById(actualOutput.id()).get();

        assertNotNull(actualAssociate.getId());
        assertEquals(expectedName, actualAssociate.getName());
        assertEquals(expectedDescription, actualAssociate.getDescription());
        assertEquals(expectedIsActive, actualAssociate.isActive());
        assertNotNull(actualAssociate.getCreatedAt());
        assertNotNull(actualAssociate.getUpdatedAt());
        assertNotNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallsCreateAgenda_shouldThrowDomainException() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateAgendaCommand.with(
                null,
                "",
                true
        );

        assertEquals(0, agendaRepository.count());

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(0, agendaRepository.count());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Mockito.verify(agendaGateway, Mockito.times(0)).create(any());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallsCreateAgenda_shouldThrowDomainException() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = CreateAgendaCommand.with(
                "  ",
                "",
                true
        );

        assertEquals(0, agendaRepository.count());

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(0, agendaRepository.count());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Mockito.verify(agendaGateway, Mockito.times(0)).create(any());
    }

}
