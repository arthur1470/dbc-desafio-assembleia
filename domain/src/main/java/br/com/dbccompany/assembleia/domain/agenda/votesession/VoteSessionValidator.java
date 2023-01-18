package br.com.dbccompany.assembleia.domain.agenda.votesession;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.validation.Validator;

public class VoteSessionValidator extends Validator {

    private final VoteSession voteSession;

    protected VoteSessionValidator(final VoteSession aVoteSession) {
        this.voteSession = aVoteSession;
    }

    @Override
    public void validate() {
        checkDateConstraints();

        if (hasErrors()) {
            throw DomainException.with("Failed to create a Vote Session", errors);
        }
    }

    private void checkDateConstraints() {
        if (voteSession.getStartedAt() == null) {
            append("'startedAt' should not be null");
        }

        if (voteSession.getEndedAt() == null) {
            append("'endedAt' should not be null");
        }
    }
}
