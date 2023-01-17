package br.com.dbccompany.assembleia.infrastructure.api.controllers;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaCommand;
import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.GetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.ListAgendasUseCase;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.presenters.AgendaApiPresenter;
import br.com.dbccompany.assembleia.infrastructure.api.AgendaAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AgendaController implements AgendaAPI {

    private final CreateAgendaUseCase createAgendaUseCase;
    private final GetAgendaByIdUseCase getAgendaByIdUseCase;
    private final ListAgendasUseCase listAgendasUseCase;

    public AgendaController(
            final CreateAgendaUseCase createAgendaUseCase,
            final GetAgendaByIdUseCase getAgendaByIdUseCase,
            final ListAgendasUseCase listAgendasUseCase
    ) {
        this.createAgendaUseCase = Objects.requireNonNull(createAgendaUseCase);
        this.getAgendaByIdUseCase = Objects.requireNonNull(getAgendaByIdUseCase);
        this.listAgendasUseCase = Objects.requireNonNull(listAgendasUseCase);
    }

    @Override
    public ResponseEntity<CreateAgendaResponse> createAgenda(final CreateAgendaRequest request) {
        final var aCommand = CreateAgendaCommand.with(
                request.name(),
                request.description(),
                request.active() == null || request.active()
        );

        final var response = AgendaApiPresenter.present(createAgendaUseCase.execute(aCommand));

        return ResponseEntity
                .created(URI.create("/agendas/%s".formatted(response.id())))
                .body(response);
    }

    @Override
    public Pagination<AgendaListResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var searchQuery = new AgendaSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction
        );

        return listAgendasUseCase.execute(searchQuery)
                .map(AgendaApiPresenter::present);
    }

    @Override
    public AgendaResponse getById(final String id) {
        return AgendaApiPresenter
                .present(this.getAgendaByIdUseCase.execute(id));
    }
}
