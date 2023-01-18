package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListAgendaVotesUseCase extends ListAgendaVotesUseCase {

    private final AgendaGateway agendaGateway;

    public DefaultListAgendaVotesUseCase(final AgendaGateway agendaGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
    }

    @Override
    public Pagination<AgendaVotesListOutput> execute(final ListAgendaVotesCommand aCommand) {
        return agendaGateway.findAll(aCommand.voteSessionId(), aCommand.searchQuery())
                .map(AgendaVotesListOutput::from);
    }
}
