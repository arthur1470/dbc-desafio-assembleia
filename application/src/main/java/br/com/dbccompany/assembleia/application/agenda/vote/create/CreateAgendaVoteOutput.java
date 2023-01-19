package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;

public record CreateAgendaVoteOutput(
        String id
) {
    public static CreateAgendaVoteOutput from(final Vote aVote) {
        return new CreateAgendaVoteOutput(
                aVote.getId().getValue()
        );
    }

    public static CreateAgendaVoteOutput from(final String anId) {
        return new CreateAgendaVoteOutput(
                anId
        );
    }
}
