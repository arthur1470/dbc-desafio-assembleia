package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;

public record ListAgendaVotesCommand(
        VoteSessionID voteSessionId,
        AgendaVotesSearchQuery searchQuery
) {
    public static ListAgendaVotesCommand with(
            final VoteSessionID aVoteSessionId,
            final AgendaVotesSearchQuery aSearchQuery
    ) {
        return new ListAgendaVotesCommand(aVoteSessionId, aSearchQuery);
    }
}
