package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;

public record CreateAgendaVoteCommand(
        AgendaID agendaId,
        VoteSessionID voteSessionId,
        AssociateID associateId,
        VoteType vote
) {
    public static CreateAgendaVoteCommand with(
            AgendaID anAgendaId,
            VoteSessionID aVoteSessionId,
            AssociateID anAssociateId,
            VoteType aVote
    ) {
        return new CreateAgendaVoteCommand(
                anAgendaId,
                aVoteSessionId,
                anAssociateId,
                aVote
        );
    }
}
