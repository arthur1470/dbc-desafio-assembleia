package br.com.dbccompany.assembleia.domain.associate;

import br.com.dbccompany.assembleia.domain.AggregateRoot;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Instant;

public class Associate extends AggregateRoot<AssociateID> {

    private String name;
    private String document;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Associate(
            final AssociateID anId,
            final String aName,
            final String aDocument,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.document = aDocument;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = anUpdateDate;
        this.deletedAt = aDeleteDate;

        this.validate();
    }

    @Override
    public void validate() {
        new AssociateValidator(this).validate();
    }

    public static Associate newAssociate(final String aName, final String aDocument, final boolean isActive) {
        final var anId = AssociateID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;

        return Associate.with(
                anId,
                aName,
                aDocument,
                isActive,
                now,
                now,
                deletedAt
        );
    }

    public static Associate with(
            final AssociateID anId,
            final String aName,
            final String aDocument,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate
    ) {
        return new Associate(
                anId,
                aName,
                aDocument,
                isActive,
                aCreationDate,
                anUpdateDate,
                aDeleteDate
        );
    }

    public static Associate with(final Associate anAssociate) {
        return Associate.with(
                anAssociate.getId(),
                anAssociate.getName(),
                anAssociate.getDocument(),
                anAssociate.isActive(),
                anAssociate.getCreatedAt(),
                anAssociate.getUpdatedAt(),
                anAssociate.getDeletedAt()
        );
    }

    public Associate activate() {
        this.active = true;
        this.updatedAt = InstantUtils.now();
        this.deletedAt = null;
        return this;
    }

    public Associate deactivate() {
        final var now = InstantUtils.now();

        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Associate createClone() {
        return with(this);
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
