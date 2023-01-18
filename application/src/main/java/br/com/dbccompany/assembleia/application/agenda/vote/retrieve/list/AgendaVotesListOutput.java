package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;

import java.time.Instant;

public record AgendaVotesListOutput(
        String vote,
        Instant createdAt,
        AssociateID associateId
) {
    public static AgendaVotesListOutput from(final Vote aVote) {
        return new AgendaVotesListOutput(
                aVote.getVote().name(),
                aVote.getCreatedAt(),
                aVote.getAssociate()
        );
    }
}
