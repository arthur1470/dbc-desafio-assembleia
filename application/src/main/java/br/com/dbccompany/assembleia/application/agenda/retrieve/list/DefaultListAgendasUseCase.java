package br.com.dbccompany.assembleia.application.agenda.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListAgendasUseCase extends ListAgendasUseCase {

    private final AgendaGateway agendaGateway;

    public DefaultListAgendasUseCase(final AgendaGateway agendaGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
    }

    @Override
    public Pagination<AgendaListOutput> execute(final AgendaSearchQuery aQuery) {
        return this.agendaGateway.findAll(aQuery)
                .map(AgendaListOutput::from);
    }
}
