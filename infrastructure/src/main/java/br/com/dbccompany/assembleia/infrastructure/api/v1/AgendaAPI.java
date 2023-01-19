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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "da4d4f93-bb4d-454e-b7f0-a53b342f4e0f"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                            {
                                                  "name": "Pauta Esportes",
                                                  "description": "uma pauta para os esportes"
                                            }
                                    """
                    )
            )
    )
    ResponseEntity<CreateAgendaResponse> createAgenda(@RequestBody final CreateAgendaRequest request);

    @GetMapping()
    @Operation(summary = "List all agendas paginated")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "current_page": 0,
                                                "per_page": 10,
                                                "total": 3,
                                                "items": [
                                                    {
                                                        "id": "420fe2a3-2b87-4c86-b368-fd27fe8c4254",
                                                        "name": "Pauta Esportes",
                                                        "description": "uma pauta pra debater os esportes",
                                                        "is_active": true,
                                                        "created_at": "2023-01-19T16:13:59.975783Z",
                                                        "deleted_at": null
                                                    },
                                                    {
                                                        "id": "de3e1e48-2962-454d-a89a-85d8ae01be17",
                                                        "name": "Pauta Esportes",
                                                        "description": "uma pauta para os esportes",
                                                        "is_active": true,
                                                        "created_at": "2023-01-19T19:43:13.395958Z",
                                                        "deleted_at": null
                                                    },
                                                    {
                                                        "id": "fb9fca1f-fe1f-42c9-9282-deaf08bbdb9c",
                                                        "name": "Pauta Esportes",
                                                        "description": "uma pauta para os esportes",
                                                        "is_active": true,
                                                        "created_at": "2023-01-19T16:14:13.797164Z",
                                                        "deleted_at": null
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
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
            @ApiResponse(
                    responseCode = "200",
                    description = "Agenda retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "id": "fb9fca1f-fe1f-42c9-9282-deaf08bbdb9c",
                                                 "name": "Pauta Esportes",
                                                 "description": "uma pauta para os esportes",
                                                 "is_active": true,
                                                 "created_at": "2023-01-19T16:14:13.797164Z",
                                                 "updated_at": "2023-01-19T16:17:55.067697Z",
                                                 "vote_session": {
                                                     "id": "f769f90b-e4cf-4bbf-91df-0bf7972a5685",
                                                     "is_active": false,
                                                     "started_at": "2023-01-19T16:14:52.015493Z",
                                                     "end_at": "2023-01-19T16:24:51.981606Z"
                                                 },
                                                 "votes": {
                                                     "total_votes": 4,
                                                     "yes_votes": 3,
                                                     "no_votes": 1,
                                                     "yes_votes_percentage": "75,00",
                                                     "no_votes_percentage": "25,00"
                                                 }
                                             }
                                            """
                            )
                    )
            ),
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "da4d4f93-bb4d-454e-b7f0-a53b342f4e0f"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                            {
                                                "duration_type": "MINUTES",
                                                "duration": 10
                                            }
                                    """
                    )
            )
    )
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Vote registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "da4d4f93-bb4d-454e-b7f0-a53b342f4e0f"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                            {
                                                "vote": "YES",
                                                "associate_id": "03bc81f5-aabe-41d1-8d42-89634bf3760e"
                                            }
                                    """
                    )
            )
    )
    ResponseEntity<CreateAgendaVoteResponse> createAgendaVote(
            @PathVariable("agendaId") final String agendaId,
            @PathVariable("voteSessionId") final String voteSessionId,
            @RequestBody final CreateAgendaVoteRequest request
    );

    @GetMapping(value = "/{agendaId}/vote-sessions/{voteSessionId}/votes")
    @Operation(summary = "List all votes of the agenda")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "current_page": 0,
                                                "per_page": 10,
                                                "total": 4,
                                                "items": [
                                                    {
                                                        "id": "8b54a8e8-2099-4029-83c0-d4843892f661",
                                                        "vote": "YES",
                                                        "created_at": "2023-01-19T16:17:54.964059Z",
                                                        "associate_id": "03bc81f5-aabe-41d1-8d42-89634bf3760e"
                                                    },
                                                    {
                                                        "id": "a6c7688a-1415-4e66-8ecc-ec3a4f8addaa",
                                                        "vote": "YES",
                                                        "created_at": "2023-01-19T16:17:15.799831Z",
                                                        "associate_id": "8f4c50f7-8068-4658-8568-5fb9c3b438d6"
                                                    },
                                                    {
                                                        "id": "47d5be6f-6942-48ec-a440-d951edb260fc",
                                                        "vote": "YES",
                                                        "created_at": "2023-01-19T16:16:50.195699Z",
                                                        "associate_id": "ae5dcb79-bf95-4c3b-b832-5f60521ebc48"
                                                    },
                                                    {
                                                        "id": "828cccae-d01a-4168-95a4-36c566288973",
                                                        "vote": "NO",
                                                        "created_at": "2023-01-19T16:15:54.984233Z",
                                                        "associate_id": "3443dca5-4290-408b-baf7-ffaea1f4fd69"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
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
