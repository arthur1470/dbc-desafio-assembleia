package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Optional;

public interface AgendaGateway {
    Agenda create(Agenda anAgenda);

    Optional<Agenda> findById(AgendaID anId);

    Pagination<Agenda> findAll(AgendaSearchQuery aQuery);

    Pagination<Vote> findAll(VoteSessionID voteSessionID, AgendaVotesSearchQuery aQuery);

    boolean existsByAssociateAndVoteSession(final AssociateID anAssociateId, final VoteSessionID aVoteSessionId);
}
