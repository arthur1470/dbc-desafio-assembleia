package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.AggregateRoot;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSession;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Instant;

public class Agenda extends AggregateRoot<AgendaID> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private VoteSession voteSession;

    private Agenda(
            final AgendaID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate,
            final VoteSession voteSession
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = anUpdateDate;
        this.deletedAt = aDeleteDate;
        this.voteSession = voteSession;

        this.validate();
    }

    @Override
    public void validate() {
        new AgendaValidator(this).validate();
    }

    public static Agenda newAgenda(final String aName, final String aDescription, final boolean isActive) {
        final var anId = AgendaID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;

        return Agenda.with(
                anId,
                aName,
                aDescription,
                isActive,
                now,
                now,
                deletedAt,
                null
        );
    }

    public static Agenda with(
            final AgendaID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final Instant aDeleteDate,
            final VoteSession aVoteSession
    ) {
        return new Agenda(
                anId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                anUpdateDate,
                aDeleteDate,
                aVoteSession
        );
    }

    public static Agenda with(final Agenda anAgenda) {
        return Agenda.with(
                anAgenda.getId(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getUpdatedAt(),
                anAgenda.getDeletedAt(),
                anAgenda.getVoteSession()
        );
    }

    public Agenda startVoteSession(final Instant anEndDate) {
        if (this.voteSession != null) {
            throw DomainException.with("This agenda already has a voting session");
        }

        this.voteSession = VoteSession.newVoteSession(anEndDate);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Agenda addVote(final Vote aVote) {
        boolean isVoteSessionInvalid = this.voteSession == null || this.voteSession.getEndedAt().isBefore(InstantUtils.now());
        if (isVoteSessionInvalid) {
            throw DomainException.with("This agenda does not have an active voting session");
        }

        this.voteSession.addVote(aVote);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Agenda activate() {
        this.active = true;
        this.updatedAt = InstantUtils.now();
        this.deletedAt = null;
        return this;
    }

    public Agenda deactivate() {
        final var now = InstantUtils.now();

        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Agenda createClone() {
        return with(this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public VoteSession getVoteSession() {
        return voteSession;
    }
}
