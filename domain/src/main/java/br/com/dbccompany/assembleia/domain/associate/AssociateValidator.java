package br.com.dbccompany.assembleia.domain.associate;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.validation.Validator;

public class AssociateValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int DOCUMENT_LENGTH = 11;

    private final Associate associate;

    protected AssociateValidator(final Associate anAssociate) {
        this.associate = anAssociate;
    }

    @Override
    public void validate() {
        checkNameConstraint();
        checkDocumentConstraint();
        checkDateConstraints();

        if (hasErrors()) {
            throw DomainException.with("Failed to create an Aggregate Associate", errors);
        }
    }

    private void checkDateConstraints() {
        if (associate.getCreatedAt() == null) {
            append("'createdAt' should not be null");
        }

        if (associate.getUpdatedAt() == null) {
            append("'updatedAt' should not be null");
        }

        if (!associate.isActive() && associate.getDeletedAt() == null) {
            append("'deletedAt' should not be null when associate is inactive");
        }
    }

    private void checkDocumentConstraint() {
        final var document = associate.getDocument();

        if (document == null) {
            append("'document' should not be null");
        } else if (document.isBlank()) {
            append("'document' should not be empty");
        } else if (document.length() != DOCUMENT_LENGTH) {
            append("'document' must have 11 characters");
        }
    }

    private void checkNameConstraint() {
        final var name = associate.getName();

        if (name == null) {
            append("'name' should not be null");
        } else if (name.isBlank()) {
            append("'name' should not be empty");
        } else if (name.length() > NAME_MAX_LENGTH) {
            append("'name' must be between 1 and 100 characters");
        }
    }
}
