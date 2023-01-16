package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;

import java.util.Objects;

public class DefaultCreateAssociateUseCase extends CreateAssociateUseCase {

    private final AssociateGateway associateGateway;

    public DefaultCreateAssociateUseCase(final AssociateGateway associateGateway) {
        this.associateGateway = Objects.requireNonNull(associateGateway);
    }

    @Override
    public CreateAssociateOutput execute(final CreateAssociateCommand aCommand) {
        final var anAssociate = Associate.newAssociate(
                aCommand.name(),
                aCommand.document(),
                aCommand.isActive()
        );

        return CreateAssociateOutput.from(associateGateway.create(anAssociate));
    }
}
