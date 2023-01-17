package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAgendaByIdUseCase extends GetAgendaByIdUseCase {

    private final AgendaGateway agendaGateway;

    public DefaultGetAgendaByIdUseCase(final AgendaGateway agendaGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
    }

    @Override
    public AgendaOutput execute(final String anId) {
        final var anAgendaId = AgendaID.from(anId);

        return this.agendaGateway.findById(anAgendaId)
                .map(AgendaOutput::from)
                .orElseThrow(notFound(anAgendaId));
    }

    private static Supplier<NotFoundException> notFound(final AgendaID anAgendaId) {
        return () -> NotFoundException.with(Agenda.class, anAgendaId);
    }
}
