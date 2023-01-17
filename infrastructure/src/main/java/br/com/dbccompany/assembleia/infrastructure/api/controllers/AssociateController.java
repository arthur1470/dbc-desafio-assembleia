package br.com.dbccompany.assembleia.infrastructure.api.controllers;

import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateCommand;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.infrastructure.api.AssociateAPI;
import br.com.dbccompany.assembleia.infrastructure.associate.models.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.associate.models.CreateAssociateResponse;
import br.com.dbccompany.assembleia.infrastructure.associate.presenters.AssociateApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AssociateController implements AssociateAPI {

    private final CreateAssociateUseCase createAssociateUseCase;

    public AssociateController(final CreateAssociateUseCase createAssociateUseCase) {
        this.createAssociateUseCase = Objects.requireNonNull(createAssociateUseCase);
    }

    @Override
    public ResponseEntity<CreateAssociateResponse> createCategory(final CreateAssociateRequest request) {
        final var isActive = request.active() == null || request.active();

        final var aCommand = CreateAssociateCommand.with(
                request.name(),
                request.document(),
                isActive
        );

        final var response =
                AssociateApiPresenter.present(createAssociateUseCase.execute(aCommand));

        return ResponseEntity
                .created(URI.create("/associates/%s".formatted(response.id())))
                .body(response);
    }
}
