package br.com.dbccompany.assembleia.domain.associate;

import br.com.dbccompany.assembleia.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class AssociateID extends Identifier {

    private final String value;

    private AssociateID(final String value) {
        this.value = value;
    }

    public static AssociateID unique() {
        return AssociateID.from(UUID.randomUUID());
    }

    public static AssociateID from(final String anId) {
        return new AssociateID(anId);
    }

    public static AssociateID from(final UUID anId) {
        return AssociateID.from(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AssociateID that = (AssociateID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
