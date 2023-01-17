package br.com.dbccompany.assembleia.infrastructure.agenda;

import br.com.dbccompany.assembleia.MySQLGatewayTest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class AgendaDefaultMySQLGatewayTest {

    @Autowired
    private AgendaDefaultMySQLGateway agendaGateway;
    @Autowired
    private AgendaRepository agendaRepository;

    @Test
    void givenAValidAgenda_whenCallsCreateAgenda_shouldReturnANewAgenda() {
        final var expectedName = "Pauta Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var anAgenda = Agenda.newAgenda(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, agendaRepository.count());

        final var actualAgenda = agendaGateway.create(anAgenda);

        assertEquals(1, agendaRepository.count());

        assertEquals(anAgenda.getId(), actualAgenda.getId());
        assertEquals(expectedName, actualAgenda.getName());
        assertEquals(expectedDescription, actualAgenda.getDescription());
        assertEquals(expectedIsActive, actualAgenda.isActive());
        assertEquals(anAgenda.getCreatedAt(), actualAgenda.getCreatedAt());
        assertEquals(anAgenda.getUpdatedAt(), actualAgenda.getUpdatedAt());
        assertEquals(anAgenda.getDeletedAt(), actualAgenda.getDeletedAt());
        assertNull(actualAgenda.getDeletedAt());

        final var actualEntity =
                agendaRepository.findById(actualAgenda.getId().getValue()).get();

        assertEquals(actualAgenda.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(actualAgenda.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(actualAgenda.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(actualAgenda.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedAgendaAndValidAgendaId_whenCallsFindById_thenShouldReturnAgenda() {
        final var expectedName = "Pauta Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var anAgenda = Agenda.newAgenda(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, agendaRepository.count());

        this.agendaRepository.saveAndFlush(AgendaJpaEntity.from(anAgenda));

        assertEquals(1, agendaRepository.count());

        final var actualEntity = agendaGateway.findById(anAgenda.getId()).get();

        assertEquals(1, agendaRepository.count());
        assertEquals(anAgenda.getId(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(anAgenda.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(anAgenda.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(anAgenda.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidAgendaIdNotStored_whenCallsFindById_thenShouldReturnEmpty() {
        assertEquals(0, agendaRepository.count());

        final var actualEntity = agendaGateway.findById(AgendaID.from("123"));

        assertEquals(0, agendaRepository.count());
        assertTrue(actualEntity.isEmpty());
    }

    @Test
    void givenPrePersistedAgendas_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var pautaSaude = Agenda.newAgenda("Pauta Saude", null, true);
        final var pautaEducacao = Agenda.newAgenda("Pauta Educacao", null, true);
        final var pautaEsportes = Agenda.newAgenda("Pauta Esportes", null, true);

        Assertions.assertEquals(0, agendaRepository.count());

        agendaRepository.saveAllAndFlush(List.of(
                AgendaJpaEntity.from(pautaSaude),
                AgendaJpaEntity.from(pautaEducacao),
                AgendaJpaEntity.from(pautaEsportes)
        ));

        Assertions.assertEquals(3, agendaRepository.count());

        final var aQuery =
                new AgendaSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = agendaGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(pautaEducacao.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyAgendasTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, agendaRepository.count());

        final var aQuery =
                new AgendaSearchQuery(0, 1, "", "name", "asc");

        final var actualResult = agendaGateway.findAll(aQuery);

        Assertions.assertEquals(0, agendaRepository.count());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenPrePersistedAgendasAndSauAsTerms_whenCallsFindAllAndTermsMatchesAgendaName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var pautaSaude = Agenda.newAgenda("Pauta Saude", null, true);
        final var pautaEducacao = Agenda.newAgenda("Pauta Educacao", null, true);
        final var pautaEsportes = Agenda.newAgenda("Pauta Esportes", null, true);

        Assertions.assertEquals(0, agendaRepository.count());

        agendaRepository.saveAllAndFlush(List.of(
                AgendaJpaEntity.from(pautaSaude),
                AgendaJpaEntity.from(pautaEducacao),
                AgendaJpaEntity.from(pautaEsportes)
        ));

        Assertions.assertEquals(3, agendaRepository.count());

        final var aQuery =
                new AgendaSearchQuery(0, 1, "SAU", "name", "asc");

        final var actualResult = agendaGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(pautaSaude.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithSpecificPage_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var pautaSaude = Agenda.newAgenda("Pauta Saude", null, true);
        final var pautaEducacao = Agenda.newAgenda("Pauta Educacao", null, true);
        final var pautaEsportes = Agenda.newAgenda("Pauta Esportes", null, true);

        Assertions.assertEquals(0, agendaRepository.count());

        agendaRepository.saveAllAndFlush(List.of(
                AgendaJpaEntity.from(pautaSaude),
                AgendaJpaEntity.from(pautaEducacao),
                AgendaJpaEntity.from(pautaEsportes)
        ));

        Assertions.assertEquals(3, agendaRepository.count());

        var aQuery = new AgendaSearchQuery(0, 1, "", "name", "asc");
        var actualResult = agendaGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(pautaEducacao.getId(), actualResult.items().get(0).getId());

        expectedPage = 1;
        aQuery = new AgendaSearchQuery(1, 1, "", "name", "asc");
        actualResult = agendaGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(pautaEsportes.getId(), actualResult.items().get(0).getId());

        expectedPage = 2;
        aQuery = new AgendaSearchQuery(2, 1, "", "name", "asc");
        actualResult = agendaGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(pautaSaude.getId(), actualResult.items().get(0).getId());
    }

}
