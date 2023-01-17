package br.com.dbccompany.assembleia.infrastructure.api.controllers;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(exception));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleDomainException(final NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(exception));
    }

    record ApiError(
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
}
