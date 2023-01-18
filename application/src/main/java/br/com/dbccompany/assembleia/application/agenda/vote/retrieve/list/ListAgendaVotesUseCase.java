package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.application.UseCase;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

public abstract class ListAgendaVotesUseCase extends UseCase<ListAgendaVotesCommand, Pagination<AgendaVotesListOutput>> {
}
