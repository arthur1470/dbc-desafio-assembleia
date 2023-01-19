package br.com.dbccompany.assembleia.infrastructure.api.v1;

import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.AgendaVotesListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.CreateAgendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendas")
@RequestMapping("/v1/agendas")
public interface AgendaAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CreateAgendaResponse> createAgenda(@RequestBody final CreateAgendaRequest request);

    @GetMapping()
    @Operation(summary = "List all agendas paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<AgendaListResponse> listAgendas(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "/{agendaId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get an agenda by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Agenda was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    AgendaResponse getById(@PathVariable("agendaId") final String id);

    @PostMapping(
            value = "/{agendaId}/vote-sessions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a vote session to an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CreateAgendaVoteSessionResponse> createAgendaVoteSession(
            @PathVariable("agendaId") final String agendaId,
            @RequestBody final CreateAgendaVoteSessionRequest request
    );

    @PostMapping(
            value = "/{agendaId}/vote-sessions/{voteSessionId}/votes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Register a vote to an agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vote registered successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CreateAgendaVoteResponse> createAgendaVote(
            @PathVariable("agendaId") final String agendaId,
            @PathVariable("voteSessionId") final String voteSessionId,
            @RequestBody final CreateAgendaVoteRequest request
    );

    @GetMapping(value = "/{agendaId}/vote-sessions/{voteSessionId}/votes")
    @Operation(summary = "List all votes of the agenda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<AgendaVotesListResponse> listVotes(
            @PathVariable("agendaId") final String agendaId,
            @PathVariable("voteSessionId") final String voteSessionId,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "createdAt") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "desc") final String direction
    );
}
