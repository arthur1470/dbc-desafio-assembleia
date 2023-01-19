package br.com.dbccompany.assembleia.application.associate.retrieve.list;

import br.com.dbccompany.assembleia.application.UseCase;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

public abstract class ListAssociatesUseCase extends UseCase<AssociateSearchQuery, Pagination<AssociateListOutput>> {
}
