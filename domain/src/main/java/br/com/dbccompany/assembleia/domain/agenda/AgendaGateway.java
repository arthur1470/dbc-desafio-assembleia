package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Optional;

public interface AgendaGateway {
    Agenda create(Agenda anAgenda);

    Optional<Agenda> findById(AgendaID anId);

    Pagination<Agenda> findAll(AgendaSearchQuery aQuery);
}
