package br.com.dbccompany.assembleia.application.associate.retrieve.list;

import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListAssociatesUseCase extends ListAssociatesUseCase {

    private final AssociateGateway associateGateway;

    public DefaultListAssociatesUseCase(final AssociateGateway associateGateway) {
        this.associateGateway = Objects.requireNonNull(associateGateway);
    }

    @Override
    public Pagination<AssociateListOutput> execute(final AssociateSearchQuery aQuery) {
        return this.associateGateway.findAll(aQuery)
                .map(AssociateListOutput::from);
    }
}
