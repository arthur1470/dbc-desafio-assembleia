package br.com.dbccompany.assembleia.infrastructure.agenda.persistence;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.persistence.VoteSessionJpaEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Optional;

@Entity(name = "Agenda")
@Table(name = "agendas")
public class AgendaJpaEntity {

    @Id
    private String id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "description", length = 4000)
    private String description;
    @Column(name = "active", nullable = false)
    private boolean active;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "vote_session_id")
    private VoteSessionJpaEntity voteSession;

    public AgendaJpaEntity() {}

    public AgendaJpaEntity(
            final String id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final VoteSessionJpaEntity voteSession
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.voteSession = voteSession;
    }

    public static AgendaJpaEntity from(final Agenda anAgenda) {
        final var aVoteSession = Optional.ofNullable(anAgenda.getVoteSession())
                .map(VoteSessionJpaEntity::from)
                .orElse(null);

        return new AgendaJpaEntity(
                anAgenda.getId().getValue(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getUpdatedAt(),
                anAgenda.getDeletedAt(),
                aVoteSession
        );
    }

    public Agenda toAggregate() {
        return Agenda.with(
                AgendaID.from(this.getId()),
                this.getName(),
                this.getDescription(),
                this.isActive(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt(),
                this.voteSession == null ? null : voteSession.toDomain()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setVoteSession(VoteSessionJpaEntity voteSession) {
        this.voteSession = voteSession;
    }

    public VoteSessionJpaEntity getVoteSession() {
        return voteSession;
    }
}
