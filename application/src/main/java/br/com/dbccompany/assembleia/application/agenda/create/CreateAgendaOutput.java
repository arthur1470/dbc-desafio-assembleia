package br.com.dbccompany.assembleia.application.agenda.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;

public record CreateAgendaOutput(String id) {
    public static CreateAgendaOutput from(final Agenda anAgenda) {
        return new CreateAgendaOutput(
                anAgenda.getId().getValue()
        );
    }

    public static CreateAgendaOutput from(final String anId) {
        return new CreateAgendaOutput(
                anId
        );
    }
}
