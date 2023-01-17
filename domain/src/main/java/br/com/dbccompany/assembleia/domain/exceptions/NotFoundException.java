package br.com.dbccompany.assembleia.domain.exceptions;

import br.com.dbccompany.assembleia.domain.AggregateRoot;
import br.com.dbccompany.assembleia.domain.Identifier;

public class NotFoundException extends NoStacktraceException {

    protected NotFoundException(final String aMessage) {
        super(aMessage);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue()
        );

        return new NotFoundException(anError);
    }
}