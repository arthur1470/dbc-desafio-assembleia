package br.com.dbccompany.assembleia.infrastructure.agenda.votesession.presenters;

import br.com.dbccompany.assembleia.application.agenda.votesession.create.CreateAgendaVoteSessionOutput;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionResponse;

public interface VoteSessionApiPresenter {

    static CreateAgendaVoteSessionResponse present(final CreateAgendaVoteSessionOutput output) {
        return new CreateAgendaVoteSessionResponse(
                output.id()
        );
    }
}
