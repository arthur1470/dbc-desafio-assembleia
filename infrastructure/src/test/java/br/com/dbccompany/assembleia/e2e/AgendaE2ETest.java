package br.com.dbccompany.assembleia.e2e;

import br.com.dbccompany.assembleia.E2ETest;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaRequest;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
class AgendaE2ETest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AgendaRepository agendaRepository;

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

        final var aRequest = MockMvcRequestBuilders.post("/agendas")
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

        final var aRequest = MockMvcRequestBuilders.get("/agendas/{id}", "123")
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

        final var aRequest = MockMvcRequestBuilders.post("/agendas")
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
        final var aRequest = MockMvcRequestBuilders.get("/agendas/{id}", anId)
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
        final var aRequest = MockMvcRequestBuilders.get("/agendas")
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
