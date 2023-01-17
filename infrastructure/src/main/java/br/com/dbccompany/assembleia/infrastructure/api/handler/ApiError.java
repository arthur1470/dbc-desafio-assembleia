package br.com.dbccompany.assembleia.infrastructure.api.handler;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.domain.validation.Error;

import java.util.List;

public record ApiError(
        String message,
        List<Error> errors
) {
    public static ApiError from(final DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors());
    }

    public static ApiError from(final NotFoundException ex) {
        return new ApiError(ex.getMessage(), null);
    }
}

