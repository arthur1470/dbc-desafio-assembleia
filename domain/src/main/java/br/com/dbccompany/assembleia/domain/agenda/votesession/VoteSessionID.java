package br.com.dbccompany.assembleia.domain.agenda.votesession;

import br.com.dbccompany.assembleia.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VoteSessionID extends Identifier {

    private final String value;

    private VoteSessionID(final String value) {
        this.value = value;
    }

    public static VoteSessionID unique() {
        return VoteSessionID.from(UUID.randomUUID());
    }

    public static VoteSessionID from(final String anId) {
        return new VoteSessionID(anId);
    }

    public static VoteSessionID from(final UUID anId) {
        return VoteSessionID.from(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VoteSessionID that = (VoteSessionID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
