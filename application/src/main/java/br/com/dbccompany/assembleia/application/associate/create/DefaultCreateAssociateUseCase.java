package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.validation.Error;

import java.util.Objects;

public class DefaultCreateAssociateUseCase extends CreateAssociateUseCase {

    private final AssociateGateway associateGateway;

    public DefaultCreateAssociateUseCase(final AssociateGateway associateGateway) {
        this.associateGateway = Objects.requireNonNull(associateGateway);
    }

    @Override
    public CreateAssociateOutput execute(final CreateAssociateCommand aCommand) {
        final var aName = aCommand.name();
        final var aDocument = aCommand.document();
        final var isActive = aCommand.isActive();
        final var anAssociate = Associate.newAssociate(aName, aDocument, isActive);

        validateDocument(aDocument);

        return CreateAssociateOutput.from(associateGateway.create(anAssociate));
    }

    private void validateDocument(final String aDocument) {
        final var isDocumentAlreadyRegistered = associateGateway.existsByDocument(aDocument);
        if (isDocumentAlreadyRegistered) {
            final var errorMessage = "'document' %s is already registered".formatted(aDocument);
            throw DomainException.with(Error.with(errorMessage));
        }

        final var isDocumentValid = associateGateway.isDocumentValid(aDocument).isValid();
        if (!isDocumentValid) {
            final var errorMessage = "'document' %s is invalid".formatted(aDocument);
            throw DomainException.with(Error.with(errorMessage));
        }
    }
}
