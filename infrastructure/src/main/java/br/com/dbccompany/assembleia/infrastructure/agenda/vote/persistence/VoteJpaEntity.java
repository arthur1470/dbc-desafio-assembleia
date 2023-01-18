package br.com.dbccompany.assembleia.infrastructure.agenda.vote.persistence;

import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteID;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.persistence.VoteSessionJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "Vote")
@Table(name = "agendas_vote_sessions_votes")
public class VoteJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "vote", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private VoteType vote;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "associate_id")
    private AssociateJpaEntity associate;

    @ManyToOne
    @JoinColumn(name = "vote_session_id")
    private VoteSessionJpaEntity voteSession;

    public VoteJpaEntity() {
    }

    public VoteJpaEntity(
            final String id,
            final VoteType vote,
            final Instant createdAt,
            final AssociateJpaEntity associate,
            final VoteSessionJpaEntity voteSession
    ) {
        this.id = id;
        this.vote = vote;
        this.createdAt = createdAt;
        this.associate = associate;
        this.voteSession = voteSession;
    }

    public static VoteJpaEntity from(final VoteSessionJpaEntity aVoteSession, final Vote aVote) {
        return new VoteJpaEntity(
                aVote.getId().getValue(),
                aVote.getVote(),
                aVote.getCreatedAt(),
                AssociateJpaEntity.from(aVote.getAssociate()),
                aVoteSession
        );
    }

    public Vote toDomain() {
        return Vote.with(
                VoteID.from(this.id),
                this.vote,
                this.createdAt,
                AssociateID.from(this.associate.getId())
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VoteType getVote() {
        return vote;
    }

    public void setVote(VoteType vote) {
        this.vote = vote;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AssociateJpaEntity getAssociate() {
        return associate;
    }

    public void setAssociate(AssociateJpaEntity associate) {
        this.associate = associate;
    }

    public VoteSessionJpaEntity getVoteSession() {
        return voteSession;
    }

    public void setVoteSession(VoteSessionJpaEntity voteSession) {
        this.voteSession = voteSession;
    }
}
