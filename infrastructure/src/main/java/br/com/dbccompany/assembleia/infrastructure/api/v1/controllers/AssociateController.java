package br.com.dbccompany.assembleia.infrastructure.api.v1.controllers;

import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateCommand;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.ListAssociatesUseCase;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.api.v1.AssociateAPI;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.AssociateListResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateResponse;
import br.com.dbccompany.assembleia.infrastructure.associate.presenters.AssociateApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AssociateController implements AssociateAPI {

    private final CreateAssociateUseCase createAssociateUseCase;
    private final ListAssociatesUseCase listAssociatesUseCase;

    public AssociateController(
            final CreateAssociateUseCase createAssociateUseCase,
            final ListAssociatesUseCase listAssociatesUseCase
    ) {
        this.createAssociateUseCase = Objects.requireNonNull(createAssociateUseCase);
        this.listAssociatesUseCase = Objects.requireNonNull(listAssociatesUseCase);
    }

    @Override
    public ResponseEntity<CreateAssociateResponse> createAssociate(final CreateAssociateRequest request) {
        final var isActive = request.active() == null || request.active();

        final var aCommand = CreateAssociateCommand.with(
                request.name(),
                request.document(),
                isActive
        );

        final var response =
                AssociateApiPresenter.present(createAssociateUseCase.execute(aCommand));

        return ResponseEntity
                .created(URI.create("/v1/associates/%s".formatted(response.id())))
                .body(response);
    }

    @Override
    public Pagination<AssociateListResponse> listAssociates(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var aQuery = new AssociateSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction
        );

        return listAssociatesUseCase.execute(aQuery)
                .map(AssociateApiPresenter::present);
    }
}
