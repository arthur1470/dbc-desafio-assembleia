package br.com.dbccompany.assembleia.infrastructure.agenda.vote.presenters;

import br.com.dbccompany.assembleia.application.agenda.vote.create.CreateAgendaVoteOutput;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteResponse;

public interface VoteApiPresenter {

    static CreateAgendaVoteResponse present(final CreateAgendaVoteOutput output) {
        return new CreateAgendaVoteResponse(
                output.id()
        );
    }
}
