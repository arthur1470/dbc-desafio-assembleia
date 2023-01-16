package br.com.dbccompany.assembleia.domain.validation;

import java.util.ArrayList;
import java.util.List;

public abstract class Validator {
    protected List<Error> errors;

    public abstract void validate();

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public List<Error> append(final String aMessage) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }

        this.errors.add(Error.with(aMessage));
        return this.errors;
    }
}
