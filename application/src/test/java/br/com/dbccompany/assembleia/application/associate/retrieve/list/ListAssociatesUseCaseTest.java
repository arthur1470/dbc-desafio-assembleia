package br.com.dbccompany.assembleia.application.associate.retrieve.list;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ListAssociatesUseCaseTest {

    @InjectMocks
    private DefaultListAssociatesUseCase useCase;
    @Mock
    private AssociateGateway associateGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(associateGateway);
    }

    @Test
    void givenAValidSearchQuery_whenCallsListAssociates_thenShouldReturnAssociates() {
        final var associates = of(
                Associate.newAssociate("Joao da Silva", "12345678901", true),
                Associate.newAssociate("Paula Maria", "12345678902", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, associates.size(), associates);

        final var expectedItemsCount = 2;

        final var expectedResult = expectedPagination.map(AssociateListOutput::from);

        Mockito.when(associateGateway.findAll(aQuery))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(associates.size(), actualResult.total());
    }

    @Test
    void givenAValidSearchQuery_whenCallsListAssociatesAndHasNoResults_thenShouldReturnEmptyAssociates() {
        final var associates = List.<Associate>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AssociateSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, associates.size(), associates);

        final var expectedItemsCount = 0;

        final var expectedResult = expectedPagination.map(AssociateListOutput::from);

        Mockito.when(associateGateway.findAll(aQuery))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(associates.size(), actualResult.total());
    }
}
