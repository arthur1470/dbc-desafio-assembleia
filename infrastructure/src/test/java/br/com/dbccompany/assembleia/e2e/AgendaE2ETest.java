package br.com.dbccompany.assembleia.e2e;

import br.com.dbccompany.assembleia.E2ETest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.models.CreateAgendaVoteRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models.CreateAgendaVoteSessionRequest;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import br.com.dbccompany.assembleia.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import static br.com.dbccompany.assembleia.domain.agenda.vote.VoteType.NO;
import static br.com.dbccompany.assembleia.domain.agenda.vote.VoteType.YES;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
class AgendaE2ETest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private AssociateRepository associateRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("assembleia");

    @DynamicPropertySource
    private static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    void itShouldBeAbleToCreateANewAgenda() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var actualId = givenAnAgenda(expectedName, expectedDescription, expectedIsActive);
        final var actualAgenda = agendaRepository.findById(actualId.getValue()).get();

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualAgenda.getId());
        assertEquals(expectedName, actualAgenda.getName());
        assertEquals(expectedDescription, actualAgenda.getDescription());
        assertEquals(expectedIsActive, actualAgenda.isActive());
        assertNotNull(actualAgenda.getCreatedAt());
        assertNotNull(actualAgenda.getUpdatedAt());
        assertNull(actualAgenda.getDeletedAt());
    }

    @Test
    void itShouldNotBeAbleToCreateAnAgendaWithNullName() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final String expectedName = null;
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aRequestBody = new CreateAgendaRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var aRequest = MockMvcRequestBuilders.post("/v1/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualResponse = this.mvc.perform(aRequest)
                .andDo(print());

        assertEquals(0, agendaRepository.count());
        actualResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void ItShouldBeAbleToGetAnAgendaByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa e uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var actualId = givenAnAgenda(expectedName, expectedDescription, expectedIsActive);
        final var actualAgenda = retrieveAnAgenda(actualId.getValue());

        assertEquals(1, agendaRepository.count());

        assertNotNull(actualAgenda.id());
        assertEquals(expectedName, actualAgenda.name());
        assertEquals(expectedDescription, actualAgenda.description());
        assertEquals(expectedIsActive, actualAgenda.active());
        assertNotNull(actualAgenda.createdAt());
        assertNotNull(actualAgenda.updatedAt());
        assertNull(actualAgenda.deletedAt());
    }

    @Test
    void ItShouldReturnErrorWhenTryToGetAnAgendaByANotStoredIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        final var aRequest = MockMvcRequestBuilders.get("/v1/agendas/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void ItShouldBeAbleToNavigateToAllAgendas() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        givenAnAgenda("Pauta1", null, true);
        givenAnAgenda("Pauta2", null, true);
        givenAnAgenda("Pauta3", null, true);

        assertEquals(3, agendaRepository.count());

        listAgendas(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Pauta1")));

        listAgendas(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Pauta2")));

        listAgendas(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Pauta3")));

        listAgendas(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void ItShouldBeAbleToSearchBetweenAllAgendas() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        givenAnAgenda("Pauta Saude", null, true);
        givenAnAgenda("Pauta Educacao", null, true);
        givenAnAgenda("Pauta Diversidade", null, true);

        assertEquals(3, agendaRepository.count());

        listAgendas(0, 1, "EDU")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Pauta Educacao")));

    }

    @Test
    void ItShouldBeAbleToSortAllAgendasByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        givenAnAgenda("Pauta Saude", "C", true);
        givenAnAgenda("Pauta Educacao", "Z", true);
        givenAnAgenda("Pauta Diversidade", "A", true);

        assertEquals(3, agendaRepository.count());

        listAgendas(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Pauta Educacao")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Pauta Saude")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Pauta Diversidade")));

    }

    @Test
    void ItShouldBeAbleToStartAVoteSession() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());

        final var aRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        final var actualSession = retrieveAnAgenda(agendaId).voteSession();

        assertNotNull(actualSession.voteSessionId());
        assertTrue(actualSession.isVoteSessionActive());
        assertNotNull(actualSession.voteSessionStartedAt());
        assertNotNull(actualSession.voteSessionEndAt());
        assertTrue(actualSession.voteSessionStartedAt().isBefore(InstantUtils.now()));
        assertTrue(actualSession.voteSessionEndAt().isAfter(InstantUtils.now()));

    }

    @Test
    void ItShouldNotBeAbleToStartAVoteSessionIfAgendaAlreadyHasAVoteSession() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());

        final var expectedErrorMessage = "This agenda already has a voting session";
        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());

        final var aRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        this.mvc.perform(aRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void ItShouldBeAbleToVoteYesToAnAgenda() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        associateRepository.saveAndFlush(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var expectedTotalVotes = 1;
        final var expectedYesVotes = 1;
        final var expectedNoVotes = 0;
        final var expectedYesVotesPercentage = "100,00";
        final var expectedNoVotesPercentage = "0,00";

        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());


        final var aSessionRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aVoteSessionRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aSessionRequestBody));

        final var sessionId = this.mvc.perform(aVoteSessionRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getHeader("location")
                .replace(
                        "/v1/agendas/" + agendaId + "/vote-sessions/",
                        ""
                );

        final var aVoteRequestBody = new CreateAgendaVoteRequest(
                "yes",
                anAssociate.getId().getValue()
        );

        final var aVoteRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes", agendaId, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aVoteRequestBody));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        final var actualVotes = retrieveAnAgenda(agendaId).votes();

        assertEquals(expectedTotalVotes, actualVotes.totalVotes());
        assertEquals(expectedYesVotes, actualVotes.yes());
        assertEquals(expectedNoVotes, actualVotes.no());
        assertEquals(expectedYesVotesPercentage, actualVotes.yesPercentage());
        assertEquals(expectedNoVotesPercentage, actualVotes.noPercentage());
    }

    @Test
    void ItShouldBeAbleToVoteNoToAnAgenda() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        associateRepository.saveAndFlush(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var expectedTotalVotes = 1;
        final var expectedYesVotes = 0;
        final var expectedNoVotes = 1;
        final var expectedYesVotesPercentage = "0,00";
        final var expectedNoVotesPercentage = "100,00";

        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());


        final var aSessionRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aVoteSessionRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aSessionRequestBody));

        final var sessionId = this.mvc.perform(aVoteSessionRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getHeader("location")
                .replace(
                        "/v1/agendas/" + agendaId + "/vote-sessions/",
                        ""
                );

        final var aVoteRequestBody = new CreateAgendaVoteRequest(
                "no",
                anAssociate.getId().getValue()
        );

        final var aVoteRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes", agendaId, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aVoteRequestBody));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        final var actualVotes = retrieveAnAgenda(agendaId).votes();

        assertEquals(expectedTotalVotes, actualVotes.totalVotes());
        assertEquals(expectedYesVotes, actualVotes.yes());
        assertEquals(expectedNoVotes, actualVotes.no());
        assertEquals(expectedYesVotesPercentage, actualVotes.yesPercentage());
        assertEquals(expectedNoVotesPercentage, actualVotes.noPercentage());
    }

    @Test
    void ItShouldNotAbleToVoteTwiceToAnAgenda() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        associateRepository.saveAndFlush(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var expectedErrorMessage = "Associate already voted for this agenda";

        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());


        final var aSessionRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aVoteSessionRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aSessionRequestBody));

        final var sessionId = this.mvc.perform(aVoteSessionRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getHeader("location")
                .replace(
                        "/v1/agendas/" + agendaId + "/vote-sessions/",
                        ""
                );

        final var aVoteRequestBody = new CreateAgendaVoteRequest(
                "no",
                anAssociate.getId().getValue()
        );

        final var aVoteRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes", agendaId, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aVoteRequestBody));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

    }

    @Test
    void ItShouldNotAbleToVoteToAnAgendaWhenAssociateIsInactive() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var anAssociate = Associate.newAssociate("Joao", "12345678901", false);
        associateRepository.saveAndFlush(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var expectedErrorMessage =
                "Associate %s is currently inactive".formatted(anAssociate.getId().getValue());

        final var agendaId = givenAnAgenda("Pauta Saude", "C", true)
                .getValue();

        assertEquals(1, agendaRepository.count());


        final var aSessionRequestBody = new CreateAgendaVoteSessionRequest(
                "HOURS",
                1
        );

        final var aVoteSessionRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{id}/vote-sessions", agendaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aSessionRequestBody));

        final var sessionId = this.mvc.perform(aVoteSessionRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getHeader("location")
                .replace(
                        "/v1/agendas/" + agendaId + "/vote-sessions/",
                        ""
                );

        final var aVoteRequestBody = new CreateAgendaVoteRequest(
                "no",
                anAssociate.getId().getValue()
        );

        final var aVoteRequest =
                MockMvcRequestBuilders.post("/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes", agendaId, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aVoteRequestBody));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void ItShouldNotAbleToVoteToAnAgendaWhenVoteSessionIsOver() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var anAssociate = Associate.newAssociate("Joao", "12345678901", true);
        associateRepository.saveAndFlush(AssociateJpaEntity.from(anAssociate));
        assertEquals(1, associateRepository.count());

        final var anAgenda =
                Agenda.newAgenda("Pauta Saude", "C", true).startVoteSession(InstantUtils.now());
        agendaRepository.saveAndFlush(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var expectedErrorMessage = "This agenda does not have an active voting session";

        final var aVoteRequestBody = new CreateAgendaVoteRequest(
                "no",
                anAssociate.getId().getValue()
        );

        final var aVoteRequest = MockMvcRequestBuilders.post(
                        "/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes",
                        anAgenda.getId().getValue(),
                        anAgenda.getVoteSession().getId().getValue()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aVoteRequestBody));

        this.mvc.perform(aVoteRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void ItShouldAbleToListVotes() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        assertEquals(0, agendaRepository.count());
        assertEquals(0, associateRepository.count());

        final var associateJoao = Associate.newAssociate("Joao", "12345678901", true);
        final var associatePedro = Associate.newAssociate("Pedro", "12345678902", true);
        final var associateMaria = Associate.newAssociate("Maria", "12345678903", true);

        Stream.of(associateJoao, associatePedro, associateMaria)
                .map(AssociateJpaEntity::from)
                .forEach(associateRepository::saveAndFlush);
        assertEquals(3, associateRepository.count());

        final var anAgenda = Agenda.newAgenda("Pauta Legal", null, true)
                .startVoteSession(InstantUtils.now().plusSeconds(3))
                .addVote(Vote.newVote(YES, associateJoao.getId()))
                .addVote(Vote.newVote(YES, associatePedro.getId()))
                .addVote(Vote.newVote(NO, associateMaria.getId()));

        final var agendaId = anAgenda.getId().getValue();
        final var sessionId = anAgenda.getVoteSession().getId().getValue();

        agendaRepository.saveAndFlush(AgendaJpaEntity.from(anAgenda));
        assertEquals(1, agendaRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aListVotesRequest =
                MockMvcRequestBuilders.get("/v1/agendas/{agendaId}/vote-sessions/{sessionId}/votes", agendaId, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("page", String.valueOf(expectedPage))
                        .queryParam("perPage", String.valueOf(expectedPerPage))
                        .queryParam("sort", expectedSort)
                        .queryParam("dir", expectedDirection)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(aListVotesRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)));
    }

    private AgendaID givenAnAgenda(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateAgendaRequest(
                aName,
                aDescription,
                isActive
        );

        final var aRequest = MockMvcRequestBuilders.post("/v1/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualJson = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var response = Json.readValue(actualJson, Map.class);
        return AgendaID.from((String) response.get("id"));
    }

    private AgendaResponse retrieveAnAgenda(final String anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/v1/agendas/{id}", anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, AgendaResponse.class);
    }

    private ResultActions listAgendas(final int page, final int perPage, final String search) throws Exception {
        return listAgendas(
                page,
                perPage,
                search,
                "",
                ""
        );
    }

    private ResultActions listAgendas(final int page, final int perPage) throws Exception {
        return listAgendas(
                page,
                perPage,
                "",
                "",
                ""
        );
    }

    private ResultActions listAgendas(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/v1/agendas")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return mvc.perform(aRequest);
    }
}
