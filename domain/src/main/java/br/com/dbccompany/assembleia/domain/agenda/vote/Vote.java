package br.com.dbccompany.assembleia.domain.agenda.vote;

import br.com.dbccompany.assembleia.domain.Entity;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Instant;

public class Vote extends Entity<VoteID> {

    private VoteType vote;
    private Instant createdAt;
    private AssociateID associate;

    private Vote(
            final VoteID anId,
            final VoteType aVote,
            final Instant aCreationDate,
            final AssociateID anAssociate
    ) {
        super(anId);
        this.vote = aVote;
        this.createdAt = aCreationDate;
        this.associate = anAssociate;

        this.validate();
    }

    @Override
    public void validate() {
        new VoteValidator(this).validate();
    }

    public static Vote with(
            final VoteID anId,
            final VoteType aVote,
            final Instant aCreationDate,
            final AssociateID anAssociate
    ) {
        return new Vote(
                anId,
                aVote,
                aCreationDate,
                anAssociate
        );
    }

    public static Vote newVote(
            final VoteType aVote,
            final AssociateID anAssociate
    ) {
        final var anId = VoteID.unique();
        final var now = InstantUtils.now();

        return with(
                anId,
                aVote,
                now,
                anAssociate
        );
    }

    public VoteType getVote() {
        return vote;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AssociateID getAssociate() {
        return associate;
    }
}
