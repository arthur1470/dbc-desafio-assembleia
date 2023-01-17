package br.com.dbccompany.assembleia.infrastructure.api;

import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agendas")
@RequestMapping("/agendas")
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
    Pagination<AgendaListResponse> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get an agenda by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agenda retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Agenda was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    AgendaResponse getById(@PathVariable("id") final String id);
}
