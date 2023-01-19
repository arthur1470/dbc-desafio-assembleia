package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
class CreateAssociateUseCaseIT {

    @Autowired
    private CreateAssociateUseCase useCase;
    @Autowired
    private AssociateRepository associateRepository;
    @SpyBean
    private AssociateGateway associateGateway;

    @Test
    void givenAValidCommand_whenCallsCreateAssociate_shouldReturnAssociateId() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "86400464093";
        final var expectedIsActive = true;

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, associateRepository.count());
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAssociate =
                associateRepository.findById(actualOutput.id()).get();

        assertNotNull(actualAssociate.getId());
        assertEquals(expectedName, actualAssociate.getName());
        assertEquals(expectedDocument, actualAssociate.getDocument());
        assertEquals(expectedIsActive, actualAssociate.isActive());
        assertNotNull(actualAssociate.getCreatedAt());
        assertNotNull(actualAssociate.getUpdatedAt());
        assertNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveAssociate_whenCallsCreateAssociate_shouldReturnInactiveAssociateId() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "86400464093";
        final var expectedIsActive = false;

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertEquals(1, associateRepository.count());
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualAssociate =
                associateRepository.findById(actualOutput.id()).get();

        assertNotNull(actualAssociate.getId());
        assertEquals(expectedName, actualAssociate.getName());
        assertEquals(expectedDocument, actualAssociate.getDocument());
        assertEquals(expectedIsActive, actualAssociate.isActive());
        assertNotNull(actualAssociate.getCreatedAt());
        assertNotNull(actualAssociate.getUpdatedAt());
        assertNotNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallsCreateAssociate_shouldReturnDomainException() {
        final var expectedName = "  ";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(0, associateRepository.count());

        Mockito.verify(associateGateway, Mockito.times(0)).create(any());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "06986509057";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(associateGateway).create(any());

        final var actualException = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aCommand)
        );

        assertEquals(0, associateRepository.count());
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
