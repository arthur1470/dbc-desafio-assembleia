package br.com.dbccompany.assembleia.infrastructure.api;

import br.com.dbccompany.assembleia.ControllerTest;
import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.AgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.GetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.AgendaListOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.ListAgendasUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.create.CreateAgendaVoteUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.ListAgendaVotesUseCase;
import br.com.dbccompany.assembleia.application.agenda.votesession.create.CreateAgendaVoteSessionUseCase;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.domain.validation.Error;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaRequest;
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

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = AgendaAPI.class)
class AgendaAPITest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CreateAgendaUseCase createAgendaUseCase;
    @MockBean
    private GetAgendaByIdUseCase getAgendaByIdUseCase;
    @MockBean
    private ListAgendasUseCase listAgendasUseCase;
    @MockBean
    private CreateAgendaVoteSessionUseCase createAgendaVoteSessionUseCase;
    @MockBean
    private CreateAgendaVoteUseCase createAgendaVoteUseCase;
    @MockBean
    private ListAgendaVotesUseCase listAgendaVotesUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateAgenda_shouldReturnAgendaId() throws Exception {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var aRequest = new CreateAgendaRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(createAgendaUseCase.execute(any()))
                .thenReturn(CreateAgendaOutput.from("123"));

        // when
        final var request = MockMvcRequestBuilders.post("/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequest));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isCreated(),
                MockMvcResultMatchers.header().string("Location", "/agendas/123"),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", equalTo("123"))
        );

        verify(createAgendaUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateAgenda_thenShouldReturnError() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "Pauta importante";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aRequest = new CreateAgendaRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(createAgendaUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        // when
        final var request = MockMvcRequestBuilders.post("/agendas")
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

        verify(createAgendaUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    void givenAValidId_whenCallsGetAgenda_shouldReturnAgenda() throws Exception {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedVoteSessionEnd = Instant.now().plusSeconds(30);

        final var anAgenda = Agenda.newAgenda(
                expectedName,
                expectedDescription,
                expectedIsActive
        )
                .startVoteSession(expectedVoteSessionEnd)
                .addVote(Vote.newVote(VoteType.YES, AssociateID.from("123")));

        final var expectedId = anAgenda.getId().getValue();

        Mockito.when(getAgendaByIdUseCase.execute(any()))
                .thenReturn(AgendaOutput.from(anAgenda));

        // when
        final var request = MockMvcRequestBuilders.get("/agendas/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isOk(),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", equalTo(expectedId)),
                jsonPath("$.name", equalTo(expectedName)),
                jsonPath("$.description", equalTo(expectedDescription)),
                jsonPath("$.is_active", equalTo(expectedIsActive)),
                jsonPath("$.created_at", equalTo(anAgenda.getCreatedAt().toString())),
                jsonPath("$.updated_at", equalTo(anAgenda.getUpdatedAt().toString())),
                jsonPath("$.deleted_at", equalTo(anAgenda.getDeletedAt())),
                jsonPath("$.is_vote_session_active", equalTo(true)),
                jsonPath("$.vote_session_end_at", equalTo(expectedVoteSessionEnd.toString())),
                jsonPath("$.votes.total_votes", equalTo(1)),
                jsonPath("$.votes.yes_votes", equalTo(1)),
                jsonPath("$.votes.no_votes", equalTo(0)),
                jsonPath("$.votes.yes_votes_percentage", equalTo("100,00")),
                jsonPath("$.votes.no_votes_percentage", equalTo("0,00"))
        );

        Mockito.verify(getAgendaByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetAgenda_shouldReturnNotFound() throws Exception {
        // given
        final var expectedId = AgendaID.from("123");
        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        when(getAgendaByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Agenda.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/agendas/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage))
        );
    }

    @Test
    void givenValidParams_whenCallsListAgendas_shouldReturnAgendas() throws Exception {
        // given
        final var agendas = List.of(
                AgendaListOutput.from(Agenda.newAgenda("Agenda 1", null, true)),
                AgendaListOutput.from(Agenda.newAgenda("Agenda 2", null, true))
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 2;
        final var expectedTotal = 2;

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, agendas);

        Mockito.when(listAgendasUseCase.execute(any()))
                .thenReturn(expectedPagination);

        // when
        final var request = MockMvcRequestBuilders.get("/agendas")
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

                jsonPath("$.items[0].id", equalTo(agendas.get(0).id())),
                jsonPath("$.items[0].name", equalTo(agendas.get(0).name())),
                jsonPath("$.items[0].description", nullValue()),
                jsonPath("$.items[0].is_active", equalTo(agendas.get(0).isActive())),
                jsonPath("$.items[0].created_at", equalTo(agendas.get(0).createdAt().toString())),
                jsonPath("$.items[0].deleted_at", nullValue()),

                jsonPath("$.items[1].id", equalTo(agendas.get(1).id())),
                jsonPath("$.items[1].name", equalTo(agendas.get(1).name())),
                jsonPath("$.items[1].description", nullValue()),
                jsonPath("$.items[1].is_active", equalTo(agendas.get(1).isActive())),
                jsonPath("$.items[1].created_at", equalTo(agendas.get(1).createdAt().toString())),
                jsonPath("$.items[1].deleted_at", nullValue())
        );

        Mockito.verify(listAgendasUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
