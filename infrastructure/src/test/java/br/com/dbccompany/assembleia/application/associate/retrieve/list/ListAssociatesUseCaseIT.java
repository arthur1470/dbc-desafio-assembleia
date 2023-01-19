package br.com.dbccompany.assembleia.application.associate.retrieve.list;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class ListAssociatesUseCaseIT {

    @Autowired
    private ListAssociatesUseCase useCase;
    @Autowired
    private AssociateRepository associateRepository;

    @BeforeEach
    void mockup() {
        final var associates = Stream.of(
                        Associate.newAssociate("Bruna Carolina", "12345678902", true),
                        Associate.newAssociate("Abel Ramos", "12345678901", true),
                        Associate.newAssociate("Anderson Augusto", "12345678903", true),
                        Associate.newAssociate("Maria da Silva", "12345678905", true),
                        Associate.newAssociate("Sandro Borges", "12345678907", true),
                        Associate.newAssociate("Pedro Jorge", "12345678904", true),
                        Associate.newAssociate("Camila Madalena", "12345678906", true)
                )
                .map(AssociateJpaEntity::from)
                .toList();

        associateRepository.saveAllAndFlush(associates);
    }

    @Test
    void givenAValidTerm_whenTermDoesNotMatchesPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Jose";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "ab,0,10,1,1,Abel Ramos",
            "caro,0,10,1,1,Bruna Carolina",
            "aUgU,0,10,1,1,Anderson Augusto",
            "pedro,0,10,1,1,Pedro Jorge",
            "ILVa,0,10,1,1,Maria da Silva",
            "CaM,0,10,1,1,Camila Madalena",
            "ndr,0,10,1,1,Sandro Borges",
    })
    void givenAValidTerm_whenCallsListAssociates_shouldReturnAssociatesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedAssociateName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedAssociateName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Abel Ramos",
            "name,desc,0,10,7,7,Sandro Borges",
            "createdAt,asc,0,10,7,7,Bruna Carolina",
            "createdAt,desc,0,10,7,7,Camila Madalena",
    })
    void givenAValidSortAndDirection_whenCallsListAssociates_thenShouldReturnAssociatesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedAssociateName
    ) {
        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedAssociateName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Abel Ramos;Bruna Carolina",
            "1,2,2,7,Anderson Augusto;Pedro Jorge",
            "2,2,2,7,Maria da Silva;Camila Madalena",
            "3,2,1,7,Sandro Borges",
    })
    void givenAValidPage_whenCallsListAssociates_shouldReturnAssociatesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedAssociatesName
    ) {
        final var expectedSort = "document";
        final var expectedDirection = "asc";

        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedAssociatesName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            assertEquals(expectedName, actualName);
            index++;
        }
    }
}
