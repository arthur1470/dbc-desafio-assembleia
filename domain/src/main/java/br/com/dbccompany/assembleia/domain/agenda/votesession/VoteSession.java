package br.com.dbccompany.assembleia.domain.agenda.votesession;

import br.com.dbccompany.assembleia.domain.Entity;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VoteSession extends Entity<VoteSessionID> {

    private Instant startedAt;
    private Instant endedAt;
    private Set<Vote> votes;

    private VoteSession(
            final VoteSessionID anId,
            final Instant aStartDate,
            final Instant anEndDate,
            final Set<Vote> votes
    ) {
        super(anId);
        this.startedAt = aStartDate;
        this.endedAt = anEndDate;
        this.votes = votes;

        this.validate();
    }

    @Override
    public void validate() {
        new VoteSessionValidator(this).validate();
    }

    public static VoteSession with(
            final VoteSessionID anId,
            final Instant aStartDate,
            final Instant anEndDate,
            final Set<Vote> votes
    ) {
        final var endedAt = anEndDate == null ? aStartDate.plus(Duration.ofMinutes(1)) : anEndDate;

        return new VoteSession(
                anId,
                aStartDate,
                endedAt,
                votes
        );
    }

    public static VoteSession newVoteSession(
            final Instant anEndDate
    ) {
        final var anId = VoteSessionID.unique();
        final var now = InstantUtils.now();

        return with(
                anId,
                now,
                anEndDate,
                new HashSet<>()
        );
    }

    public VoteSession addVote(final Vote aVote) {
        if (aVote == null) {
            return this;
        }

        this.votes.add(aVote);
        return this;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public Set<Vote> getVotes() {
        return Collections.unmodifiableSet(votes);
    }
}
