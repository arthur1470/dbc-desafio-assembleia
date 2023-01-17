package br.com.dbccompany.assembleia.infrastructure.agenda.presenters;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.AgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.AgendaListOutput;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaResponse;

public interface AgendaApiPresenter {

    static CreateAgendaResponse present(final CreateAgendaOutput output) {
        return new CreateAgendaResponse(
                output.id()
        );
    }

    static AgendaListResponse present(final AgendaListOutput output) {
        return new AgendaListResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

    static AgendaResponse present(final AgendaOutput output) {
        return new AgendaResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
