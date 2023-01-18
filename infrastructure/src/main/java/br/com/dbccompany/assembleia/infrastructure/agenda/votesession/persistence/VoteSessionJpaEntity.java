package br.com.dbccompany.assembleia.infrastructure.agenda.votesession.persistence;

import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSession;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.persistence.VoteJpaEntity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "VoteSession")
@Table(name = "agendas_vote_sessions")
public class VoteSessionJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "started_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant startedAt;

    @Column(name = "ended_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant endedAt;

    @OneToMany(mappedBy = "voteSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<VoteJpaEntity> votes;

    public VoteSessionJpaEntity() {
    }

    public VoteSessionJpaEntity(final String id) {
        this.id = id;
    }

    public VoteSessionJpaEntity(
            final String id,
            final Instant startedAt,
            final Instant endedAt,
            final Set<VoteJpaEntity> votes
    ) {
        this.id = id;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.votes = votes;
    }

    public static VoteSessionJpaEntity from(final VoteSession aVoteSession) {
        final var votes = aVoteSession.getVotes().stream()
                .map(vote -> VoteJpaEntity.from(VoteSessionJpaEntity.from(aVoteSession.getId()), vote))
                .collect(Collectors.toSet());

        return new VoteSessionJpaEntity(
                aVoteSession.getId().getValue(),
                aVoteSession.getStartedAt(),
                aVoteSession.getEndedAt(),
                votes
        );
    }


    public static VoteSessionJpaEntity from(final VoteSessionID aVoteSessionId) {
        return new VoteSessionJpaEntity(
                aVoteSessionId.getValue()
        );
    }


    public VoteSession toDomain() {
        return VoteSession.with(
                VoteSessionID.from(getId()),
                getStartedAt(),
                getEndedAt(),
                getVotes().stream().map(VoteJpaEntity::toDomain).collect(Collectors.toSet())
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public Set<VoteJpaEntity> getVotes() {
        return votes == null ? Collections.emptySet() : votes;
    }

    public void setVotes(Set<VoteJpaEntity> votes) {
        this.votes = votes;
    }

}
