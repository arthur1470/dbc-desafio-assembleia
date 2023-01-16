package br.com.dbccompany.assembleia.infrastructure.associate.presenters;

import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateOutput;
import br.com.dbccompany.assembleia.infrastructure.associate.models.CreateAssociateResponse;

public interface AssociatePresenter {
    static CreateAssociateResponse present(final CreateAssociateOutput output) {
        return new CreateAssociateResponse(
                output.id()
        );
    }
}
