package br.com.dbccompany.assembleia.infrastructure.api.v1;

import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.AssociateListResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Associates")
@RequestMapping("/v1/associates")
public interface AssociateAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new associate")
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
            @ApiResponse(
                    responseCode = "422",
                    description = "A validation error was thrown",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "'document' 07789238000 is already registered",
                                                "errors": [
                                                    {
                                                        "message": "'document' 07789238000 is already registered"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                            {
                                                 "name": "Camila Barreiros",
                                                 "document": "07789238001",
                                                 "is_active": true
                                             }
                                    """
                    )
            )
    )
    ResponseEntity<CreateAssociateResponse> createAssociate(@RequestBody final CreateAssociateRequest request);

    @GetMapping()
    @Operation(summary = "List all associates paginated")
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
                                                 "total": 5,
                                                 "items": [
                                                     {
                                                         "id": "ae5dcb79-bf95-4c3b-b832-5f60521ebc48",
                                                         "name": "Pedro Barreiros",
                                                         "document": "84650193044",
                                                         "is_active": true,
                                                         "created_at": "2023-01-19T16:16:34.101988Z",
                                                         "deleted_at": null
                                                     },
                                                     {
                                                         "id": "03bc81f5-aabe-41d1-8d42-89634bf3760e",
                                                         "name": "Camila Barreiros",
                                                         "document": "07789238000",
                                                         "is_active": true,
                                                         "created_at": "2023-01-19T16:17:34.082274Z",
                                                         "deleted_at": null
                                                     },
                                                     {
                                                         "id": "3443dca5-4290-408b-baf7-ffaea1f4fd69",
                                                         "name": "Camila Barreiros",
                                                         "document": "60610687000",
                                                         "is_active": true,
                                                         "created_at": "2023-01-19T16:13:45.097845Z",
                                                         "deleted_at": null
                                                     },
                                                     {
                                                         "id": "8f4c50f7-8068-4658-8568-5fb9c3b438d6",
                                                         "name": "Camila Barreiros",
                                                         "document": "63147722066",
                                                         "is_active": true,
                                                         "created_at": "2023-01-19T16:17:07.477971Z",
                                                         "deleted_at": null
                                                     },
                                                     {
                                                         "id": "da4d4f93-bb4d-454e-b7f0-a53b342f4e0f",
                                                         "name": "Camila Barreiros",
                                                         "document": "45167762006",
                                                         "is_active": true,
                                                         "created_at": "2023-01-19T19:36:26.556087Z",
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
    Pagination<AssociateListResponse> listAssociates(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );
}
