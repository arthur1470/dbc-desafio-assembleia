package br.com.dbccompany.assembleia.application.agenda.votesession.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;

public record CreateAgendaVoteSessionOutput(
        String id
) {
    public static CreateAgendaVoteSessionOutput from(final Agenda agenda) {
        return new CreateAgendaVoteSessionOutput(agenda.getVoteSession().getId().getValue());
    }

    public static CreateAgendaVoteSessionOutput from(final String anId) {
        return new CreateAgendaVoteSessionOutput(anId);
    }
}
