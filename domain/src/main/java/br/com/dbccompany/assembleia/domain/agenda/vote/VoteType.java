package br.com.dbccompany.assembleia.domain.agenda.vote;

import java.util.Arrays;
import java.util.Optional;

public enum VoteType {
    YES,
    NO;

    public static Optional<VoteType> of(final String value) {
        return Arrays.stream(VoteType.values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
