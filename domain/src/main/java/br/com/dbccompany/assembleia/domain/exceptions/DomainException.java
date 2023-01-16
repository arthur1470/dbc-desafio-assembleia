package br.com.dbccompany.assembleia.domain.exceptions;

import br.com.dbccompany.assembleia.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public static DomainException with(final String aMessage, final List<Error> anErrors) {
        return new DomainException(aMessage, anErrors);
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
