package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.validation.Validator;

public class AgendaValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;

    private final Agenda agenda;

    protected AgendaValidator(final Agenda anAgenda) {
        this.agenda = anAgenda;
    }

    @Override
    public void validate() {
        checkNameConstraint();
        checkDateConstraints();

        if (hasErrors()) {
            throw DomainException.with("Failed to create an Aggregate Agenda", errors);
        }
    }

    private void checkDateConstraints() {
        if (agenda.getCreatedAt() == null) {
            append("'createdAt' should not be null");
        }

        if (agenda.getUpdatedAt() == null) {
            append("'updatedAt' should not be null");
        }

        if (!agenda.isActive() && agenda.getDeletedAt() == null) {
            append("'deletedAt' should not be null when associate is inactive");
        }
    }

    private void checkNameConstraint() {
        final var name = agenda.getName();

        if (name == null) {
            append("'name' should not be null");
        } else if (name.isBlank()) {
            append("'name' should not be empty");
        } else if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            append("'name' must be between 3 and 255 characters");
        }
    }
}
