package br.com.dbccompany.assembleia.application.agenda.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;

import java.util.Objects;

public class DefaultCreateAgendaUseCase extends CreateAgendaUseCase {

    private final AgendaGateway agendaGateway;

    public DefaultCreateAgendaUseCase(final AgendaGateway agendaGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
    }

    @Override
    public CreateAgendaOutput execute(final CreateAgendaCommand anIn) {
        final var anAgenda = Agenda.newAgenda(
                anIn.name(),
                anIn.description(),
                anIn.active()
        );

        return CreateAgendaOutput.from(agendaGateway.create(anAgenda));
    }
}
