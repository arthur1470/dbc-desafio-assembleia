package br.com.dbccompany.assembleia.infrastructure.api.controllers;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaCommand;
import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.GetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.ListAgendasUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.create.CreateAgendaVoteCommand;
import br.com.dbccompany.assembleia.application.agenda.vote.create.CreateAgendaVoteUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.ListAgendaVotesCommand;
import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.ListAgendaVotesUseCase;
import br.com.dbccompany.assembleia.application.agenda.votesession.create.CreateAgendaVoteSessionCommand;
import br.com.dbccompany.assembleia.application.agenda.votesession.create.CreateAgendaVoteSessionUseCase;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.presenters.AgendaApiPresenter;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.AgendaVotesListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.presenters.VoteApiPresenter;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.VoteSessionDuration;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.presenters.VoteSessionApiPresenter;
import br.com.dbccompany.assembleia.infrastructure.api.AgendaAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Supplier;

@RestController
public class AgendaController implements AgendaAPI {

    private final CreateAgendaUseCase createAgendaUseCase;
    private final GetAgendaByIdUseCase getAgendaByIdUseCase;
    private final ListAgendasUseCase listAgendasUseCase;
    private final CreateAgendaVoteSessionUseCase createAgendaVoteSessionUseCase;
    private final CreateAgendaVoteUseCase createAgendaVoteUseCase;
    private final ListAgendaVotesUseCase listAgendaVotesUseCase;

    public AgendaController(
            final CreateAgendaUseCase createAgendaUseCase,
            final GetAgendaByIdUseCase getAgendaByIdUseCase,
            final ListAgendasUseCase listAgendasUseCase,
            final CreateAgendaVoteSessionUseCase createAgendaVoteSessionUseCase,
            final CreateAgendaVoteUseCase createAgendaVoteUseCase,
            final ListAgendaVotesUseCase listAgendaVotesUseCase
    ) {
        this.createAgendaUseCase = Objects.requireNonNull(createAgendaUseCase);
        this.getAgendaByIdUseCase = Objects.requireNonNull(getAgendaByIdUseCase);
        this.listAgendasUseCase = Objects.requireNonNull(listAgendasUseCase);
        this.createAgendaVoteSessionUseCase = Objects.requireNonNull(createAgendaVoteSessionUseCase);
        this.createAgendaVoteUseCase = Objects.requireNonNull(createAgendaVoteUseCase);
        this.listAgendaVotesUseCase = Objects.requireNonNull(listAgendaVotesUseCase);
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
    public Pagination<AgendaListResponse> listAgendas(
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

    @Override
    public ResponseEntity<CreateAgendaVoteSessionResponse> createAgendaVoteSession(
            final String agendaId,
            final CreateAgendaVoteSessionRequest request
    ) {
        final var aVoteSessionEndDate = VoteSessionDuration.of(request.durationType())
                .orElse(VoteSessionDuration.MINUTES)
                .getEndDate(request.duration());

        final var aCommand = CreateAgendaVoteSessionCommand.with(
                AgendaID.from(agendaId),
                aVoteSessionEndDate
        );

        final var response = createAgendaVoteSessionUseCase.execute(aCommand);

        return ResponseEntity
                .created(URI.create("/agendas/%s/vote-sessions/%s".formatted(agendaId, response.id())))
                .body(VoteSessionApiPresenter.present(response));
    }

    @Override
    public ResponseEntity<CreateAgendaVoteResponse> createAgendaVote(
            final String agendaId,
            final String voteSessionId,
            final CreateAgendaVoteRequest request
    ) {
        Supplier<DomainException> invalidVoteType = () ->
                DomainException.with("Invalid '%s' for vote, use only YES or NO".formatted(request.vote()));

        final var aVote = VoteType.of(request.vote())
                .orElseThrow(invalidVoteType);

        final var aCommand = CreateAgendaVoteCommand.with(
                AgendaID.from(agendaId),
                VoteSessionID.from(voteSessionId),
                AssociateID.from(request.associateId()),
                aVote
        );

        final var response = createAgendaVoteUseCase.execute(aCommand);

        final var uri = URI.create(
                "/agendas/%s/vote-sessions/%s/votes/%s".formatted(agendaId, voteSessionId, response.id())
        );

        return ResponseEntity
                .created(uri)
                .body(VoteApiPresenter.present(response));
    }

    @Override
    public Pagination<AgendaVotesListResponse> listVotes(
            final String agendaId,
            final String voteSessionId,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var searchQuery = new AgendaVotesSearchQuery(
                page,
                perPage,
                sort,
                direction
        );

        final var aCommand = ListAgendaVotesCommand.with(
                VoteSessionID.from(voteSessionId),
                searchQuery
        );

        return listAgendaVotesUseCase.execute(aCommand)
                .map(AgendaVotesListResponse::from);
    }
}
