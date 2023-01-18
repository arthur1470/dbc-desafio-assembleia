package br.com.dbccompany.assembleia.domain.agenda.vote;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.validation.Validator;

public class VoteValidator extends Validator {

    private final Vote vote;

    protected VoteValidator(final Vote aVote) {
        this.vote = aVote;
    }

    @Override
    public void validate() {
        if (vote.getCreatedAt() == null)
            append("'createdAt' should not be null");

        if (vote.getVote() == null)
            append("'vote' should not be null");

        if (vote.getAssociate() == null)
            append("'associate' should not be null");

        if (hasErrors()) {
            throw DomainException.with("Failed to create a Vote", errors);
        }
    }
}
