package br.com.dbccompany.assembleia.application.agenda.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
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
class ListAgendasUseCaseTest {

    @InjectMocks
    private DefaultListAgendasUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListAgendas_thenShouldReturnAgendas() {
        final var agendas = of(
                Agenda.newAgenda("Pauta Importante", null, true),
                Agenda.newAgenda("Pauta Irrelevante", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, agendas.size(), agendas);

        final var expectedItemsCount = 2;

        final var expectedResult = expectedPagination.map(AgendaListOutput::from);

        Mockito.when(agendaGateway.findAll(aQuery))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(agendas.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenCallsListAgendasAndHasNoResults_thenShouldReturnEmptyAgendas() {
        final var agendas = List.<Agenda>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, agendas.size(), agendas);

        final var expectedItemsCount = 0;

        final var expectedResult = expectedPagination.map(AgendaListOutput::from);

        Mockito.when(agendaGateway.findAll(aQuery))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(agendas.size(), actualResult.total());
    }

}