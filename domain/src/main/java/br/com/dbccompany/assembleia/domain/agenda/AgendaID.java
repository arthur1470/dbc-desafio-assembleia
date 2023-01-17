package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class AgendaID extends Identifier {

    private final String value;

    private AgendaID(final String value) {
        this.value = value;
    }

    public static AgendaID unique() {
        return AgendaID.from(UUID.randomUUID());
    }

    public static AgendaID from(final String anId) {
        return new AgendaID(anId);
    }

    public static AgendaID from(final UUID anId) {
        return AgendaID.from(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AgendaID that = (AgendaID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
