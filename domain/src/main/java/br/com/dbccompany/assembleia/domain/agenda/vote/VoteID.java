package br.com.dbccompany.assembleia.domain.agenda.vote;

import br.com.dbccompany.assembleia.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VoteID extends Identifier {

    private final String value;

    private VoteID(final String value) {
        this.value = value;
    }

    public static VoteID unique() {
        return VoteID.from(UUID.randomUUID());
    }

    public static VoteID from(final String anId) {
        return new VoteID(anId);
    }

    public static VoteID from(final UUID anId) {
        return VoteID.from(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VoteID that = (VoteID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
