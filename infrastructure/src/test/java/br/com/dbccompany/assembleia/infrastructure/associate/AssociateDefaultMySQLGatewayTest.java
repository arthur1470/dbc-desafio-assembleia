package br.com.dbccompany.assembleia.infrastructure.associate;

import br.com.dbccompany.assembleia.MySQLGatewayTest;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class AssociateDefaultMySQLGatewayTest {

    @Autowired
    private AssociateDefaultMySQLGateway associateGateway;
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

        final var actualAssociate = associateGateway.create(anAssociate);

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

        final var actualAssociate = associateGateway.create(anAssociate);

        assertEquals(1, associateRepository.count());

        final var actualResult = associateGateway.existsByDocument(expectedDocument);

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

        final var actualResult = associateGateway.existsByDocument(expectedDocument);

        assertEquals(1, associateRepository.count());
        assertFalse(actualResult);
    }

    @Test
    void givenAValidAssociateDocument_whenCallsIsDocumentValid_shouldReturnTrue() {
        final var expectedDocument = "12345678901";

        final var actualResult = associateGateway.isDocumentValid(expectedDocument);

        assertTrue(actualResult);
    }

    @Test
    void givenPrePersistedAssociates_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var associateJoao = Associate.newAssociate("Joao da Silva", "12345678901", true);
        final var associateMaria = Associate.newAssociate("Maria Maria", "12345678902", true);
        final var associatePaulo = Associate.newAssociate("Paulo Pedro", "12345678903", true);

        Assertions.assertEquals(0, associateRepository.count());

        associateRepository.saveAllAndFlush(List.of(
                AssociateJpaEntity.from(associateJoao),
                AssociateJpaEntity.from(associateMaria),
                AssociateJpaEntity.from(associatePaulo)
        ));

        Assertions.assertEquals(3, associateRepository.count());

        final var aQuery =
                new AssociateSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = associateGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(associateJoao.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyAssociatesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, associateRepository.count());

        final var aQuery =
                new AssociateSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = associateGateway.findAll(aQuery);

        Assertions.assertEquals(0, associateRepository.count());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenPrePersistedAssociatesAndMariAsTerms_whenCallsFindAllAndTermsMatchesAssociateName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var associateJoao = Associate.newAssociate("Joao da Silva", "12345678901", true);
        final var associateMaria = Associate.newAssociate("Maria Maria", "12345678902", true);
        final var associatePaulo = Associate.newAssociate("Paulo Pedro", "12345678903", true);

        Assertions.assertEquals(0, associateRepository.count());

        associateRepository.saveAllAndFlush(List.of(
                AssociateJpaEntity.from(associateJoao),
                AssociateJpaEntity.from(associateMaria),
                AssociateJpaEntity.from(associatePaulo)
        ));

        Assertions.assertEquals(3, associateRepository.count());

        final var aQuery =
                new AssociateSearchQuery(0, 1, "MARI", "name", "asc");

        final var actualResult = associateGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(associateMaria.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithSpecificPage_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var associateJoao = Associate.newAssociate("Joao da Silva", "12345678901", true);
        final var associateMaria = Associate.newAssociate("Maria Maria", "12345678902", true);
        final var associatePaulo = Associate.newAssociate("Paulo Pedro", "12345678903", true);

        Assertions.assertEquals(0, associateRepository.count());

        associateRepository.saveAllAndFlush(List.of(
                AssociateJpaEntity.from(associateJoao),
                AssociateJpaEntity.from(associateMaria),
                AssociateJpaEntity.from(associatePaulo)
        ));

        Assertions.assertEquals(3, associateRepository.count());

        var aQuery = new AssociateSearchQuery(0, 1, "", "name", "asc");
        var actualResult = associateGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(associateJoao.getId(), actualResult.items().get(0).getId());

        expectedPage = 1;
        aQuery = new AssociateSearchQuery(1, 1, "", "name", "asc");
        actualResult = associateGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(associateMaria.getId(), actualResult.items().get(0).getId());

        expectedPage = 2;
        aQuery = new AssociateSearchQuery(2, 1, "", "name", "asc");
        actualResult = associateGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(associatePaulo.getId(), actualResult.items().get(0).getId());
    }
}