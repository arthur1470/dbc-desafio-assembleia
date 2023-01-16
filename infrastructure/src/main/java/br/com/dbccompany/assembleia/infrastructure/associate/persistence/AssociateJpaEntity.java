package br.com.dbccompany.assembleia.infrastructure.associate.persistence;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "Associate")
@Table(name = "associate")
public class AssociateJpaEntity {

    @Id
    private String id;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "document", length = 11, nullable = false)
    private String document;
    @Column(name = "active", nullable = false)
    private boolean active;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public AssociateJpaEntity() {
    }

    public AssociateJpaEntity(
            String id,
            String name,
            String document,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static AssociateJpaEntity from(final Associate anAssociate) {
        return new AssociateJpaEntity(
                anAssociate.getId().getValue(),
                anAssociate.getName(),
                anAssociate.getDocument(),
                anAssociate.isActive(),
                anAssociate.getCreatedAt(),
                anAssociate.getUpdatedAt(),
                anAssociate.getDeletedAt()
        );
    }

    public Associate toAggregate() {
        return Associate.with(
                AssociateID.from(this.getId()),
                this.getName(),
                this.getDocument(),
                this.isActive(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

    public String getId() {
        return id;
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
