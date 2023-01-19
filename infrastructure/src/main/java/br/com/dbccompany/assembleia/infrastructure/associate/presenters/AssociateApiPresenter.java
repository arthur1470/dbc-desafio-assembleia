package br.com.dbccompany.assembleia.infrastructure.associate.presenters;

import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateOutput;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.AssociateListOutput;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.AssociateListResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateResponse;

public interface AssociateApiPresenter {
    static CreateAssociateResponse present(final CreateAssociateOutput output) {
        return new CreateAssociateResponse(
                output.id()
        );
    }

    static AssociateListResponse present(final AssociateListOutput output) {
        return new AssociateListResponse(
                output.id(),
                output.name(),
                output.document(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
