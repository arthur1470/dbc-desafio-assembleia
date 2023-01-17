package br.com.dbccompany.assembleia.application.agenda.retrieve.list;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class ListAgendasUseCaseIT {

    @Autowired
    private ListAgendasUseCase useCase;
    @Autowired
    private AgendaRepository agendaRepository;

    @BeforeEach
    void mockup() {
        final var agendas = Stream.of(
                        Agenda.newAgenda("Pauta salarios", "Pauta de salarios", true),
                        Agenda.newAgenda("Pauta empregos", "Pauta de gerar empregos", true),
                        Agenda.newAgenda("Pauta saude", "Pauta sobre hospitais", true),
                        Agenda.newAgenda("Pauta educacao", "Pauta das escolas publicas", true),
                        Agenda.newAgenda("Pauta esportes", "Pauta sobre esportes", true),
                        Agenda.newAgenda("Pauta criancas", "Pauta para crianças", true),
                        Agenda.newAgenda("Pauta internet", "Pauta para debater a internet", true)
                )
                .map(AgendaJpaEntity::from)
                .toList();

        agendaRepository.saveAllAndFlush(agendas);
    }

    @Test
    void givenAValidTerm_whenTermDoesNotMatchesPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "jijisadjidsaijd";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "sala,0,10,1,1,Pauta salarios",
            "crian,0,10,1,1,Pauta criancas",
            "NET,0,10,1,1,Pauta internet",
            "espo,0,10,1,1,Pauta esportes",
            "sau,0,10,1,1,Pauta saude",
            "EmP,0,10,1,1,Pauta empregos",
    })
    void givenAValidTerm_whenCallsListAgendas_shouldReturnAgendasFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Pauta criancas",
            "name,desc,0,10,7,7,Pauta saude",
            "createdAt,asc,0,10,7,7,Pauta salarios",
            "createdAt,desc,0,10,7,7,Pauta internet",
    })
    void givenAValidSortAndDirection_whenCallsListAgendas_thenShouldReturnAgendasOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Pauta criancas;Pauta educacao",
            "1,2,2,7,Pauta empregos;Pauta esportes",
            "2,2,2,7,Pauta internet;Pauta salarios",
            "3,2,1,7,Pauta saude",
    })
    void givenAValidPage_whenCallsListAgendas_shouldReturnAgendasPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaSearchQuery(expectedPage, expectedPerPage, "", expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for(String expectedName: expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            assertEquals(expectedName, actualName);
            index++;
        }
    }
}
