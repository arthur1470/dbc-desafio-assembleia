package br.com.dbccompany.assembleia.infrastructure.api;

import br.com.dbccompany.assembleia.ControllerTest;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateOutput;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.AssociateListOutput;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.ListAssociatesUseCase;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.domain.validation.Error;
import br.com.dbccompany.assembleia.infrastructure.api.v1.AssociateAPI;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = AssociateAPI.class)
class AssociateAPITest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CreateAssociateUseCase createAssociateUseCase;
    @MockBean
    private ListAssociatesUseCase listAssociatesUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateAssociate_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;

        final var aRequest = new CreateAssociateRequest(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(createAssociateUseCase.execute(any()))
                .thenReturn(CreateAssociateOutput.from("123"));

        // when
        final var request = MockMvcRequestBuilders.post("/v1/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequest));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isCreated(),
                MockMvcResultMatchers.header().string("Location", "/v1/associates/123"),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", equalTo("123"))
        );

        verify(createAssociateUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDocument, cmd.document())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateAssociate_thenShouldReturnError() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aRequest = new CreateAssociateRequest(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(createAssociateUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        // when
        final var request = MockMvcRequestBuilders.post("/v1/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequest));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isUnprocessableEntity(),
                MockMvcResultMatchers.header().string("Location", nullValue()),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
        );

        verify(createAssociateUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDocument, cmd.document())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenValidParams_whenCallsListAssociates_shouldReturnAssociates() throws Exception {
        // given
        final var associates = List.of(
                AssociateListOutput.from(Associate.newAssociate("Andre Marques", "12345678901", true)),
                AssociateListOutput.from(Associate.newAssociate("Clarice Silveira", "12345678902", true))
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 2;
        final var expectedTotal = 2;

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, associates);

        Mockito.when(listAssociatesUseCase.execute(any()))
                .thenReturn(expectedPagination);

        // when
        final var request = MockMvcRequestBuilders.get("/v1/associates")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isOk(),
                jsonPath("$.current_page", equalTo(expectedPage)),
                jsonPath("$.per_page", equalTo(expectedPerPage)),
                jsonPath("$.total", equalTo(expectedTotal)),
                jsonPath("$.items", hasSize(expectedItemsCount)),

                jsonPath("$.items[0].id", equalTo(associates.get(0).id())),
                jsonPath("$.items[0].name", equalTo(associates.get(0).name())),
                jsonPath("$.items[0].document", equalTo(associates.get(0).document())),
                jsonPath("$.items[0].is_active", equalTo(associates.get(0).isActive())),
                jsonPath("$.items[0].created_at", equalTo(associates.get(0).createdAt().toString())),
                jsonPath("$.items[0].deleted_at", nullValue()),

                jsonPath("$.items[1].id", equalTo(associates.get(1).id())),
                jsonPath("$.items[1].name", equalTo(associates.get(1).name())),
                jsonPath("$.items[1].document", equalTo(associates.get(1).document())),
                jsonPath("$.items[1].is_active", equalTo(associates.get(1).isActive())),
                jsonPath("$.items[1].created_at", equalTo(associates.get(1).createdAt().toString())),
                jsonPath("$.items[1].deleted_at", nullValue())
        );

        Mockito.verify(listAssociatesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
