package br.com.dbccompany.assembleia.application.agenda.retrieve.list;

import br.com.dbccompany.assembleia.application.UseCase;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

public abstract class ListAgendasUseCase extends UseCase<AgendaSearchQuery, Pagination<AgendaListOutput>> {
}
