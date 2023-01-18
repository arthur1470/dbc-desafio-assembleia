package br.com.dbccompany.assembleia.application.agenda.votesession.create;

import br.com.dbccompany.assembleia.domain.agenda.AgendaID;

import java.time.Instant;

public record CreateAgendaVoteSessionCommand(
        AgendaID agendaId,
        Instant endsAt
) {

    public static CreateAgendaVoteSessionCommand with(
            final AgendaID anAgendaId,
            final Instant anEndDate
    ) {
        return new CreateAgendaVoteSessionCommand(
                anAgendaId,
                anEndDate
        );
    }
}
