package br.com.dbccompany.assembleia.infrastructure.associate;

import br.com.dbccompany.assembleia.MySQLGatewayTest;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class AssociateDefaultMySQLGatewayTest {

    @Autowired
    private AssociateDefaultMySQLGateway associateDefaultMySQLGateway;
    @Autowired
    private AssociateRepository associateRepository;

    @Test
    void givenAValidAssociate_whenCallsCreateAssociate_shouldReturnANewAssociate() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;

        final var anAssociate = Associate.newAssociate(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        final var actualAssociate = associateDefaultMySQLGateway.create(anAssociate);

        assertEquals(1, associateRepository.count());

        assertEquals(anAssociate.getId(), actualAssociate.getId());
        assertEquals(expectedName, actualAssociate.getName());
        assertEquals(expectedDocument, actualAssociate.getDocument());
        assertEquals(expectedIsActive, actualAssociate.isActive());
        assertEquals(anAssociate.getCreatedAt(), actualAssociate.getCreatedAt());
        assertEquals(anAssociate.getUpdatedAt(), actualAssociate.getUpdatedAt());
        assertEquals(anAssociate.getDeletedAt(), actualAssociate.getDeletedAt());
        assertNull(actualAssociate.getDeletedAt());

        final var actualEntity = associateRepository.findById(actualAssociate.getId().getValue()).get();

        assertEquals(actualAssociate.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDocument, actualEntity.getDocument());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(actualAssociate.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(actualAssociate.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(actualAssociate.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedAssociateDocument_whenCallsExistsByDocument_shouldReturnTrue() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;

        final var anAssociate = Associate.newAssociate(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        final var actualAssociate = associateDefaultMySQLGateway.create(anAssociate);

        assertEquals(1, associateRepository.count());

        final var actualResult = associateDefaultMySQLGateway.existsByDocument(expectedDocument);

        assertEquals(1, associateRepository.count());
        assertTrue(actualResult);
    }

    @Test
    void givenANotPersistedAssociateDocument_whenCallsExistsByDocument_shouldReturnFalse() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;

        final var anAssociate = Associate.newAssociate(
                expectedName,
                "12345678900",
                expectedIsActive
        );

        assertEquals(0, associateRepository.count());

        associateRepository.save(AssociateJpaEntity.from(anAssociate));

        assertEquals(1, associateRepository.count());

        final var actualResult = associateDefaultMySQLGateway.existsByDocument(expectedDocument);

        assertEquals(1, associateRepository.count());
        assertFalse(actualResult);
    }

    @Test
    void givenAValidAssociateDocument_whenCallsIsDocumentValid_shouldReturnTrue() {
        final var expectedDocument = "12345678901";

        final var actualResult = associateDefaultMySQLGateway.isDocumentValid(expectedDocument);

        assertTrue(actualResult);
    }
}