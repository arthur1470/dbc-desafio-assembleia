package br.com.dbccompany.assembleia.e2e;

import br.com.dbccompany.assembleia.E2ETest;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.infrastructure.associate.models.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import br.com.dbccompany.assembleia.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
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
class AssociateE2ETest {

    @Autowired
    private MockMvc mvc;
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
    void itShouldBeAbleToCreateANewActiveAssociate() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, associateRepository.count());

        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = true;

        final var actualId = givenAnAssociate(expectedName, expectedDocument, expectedIsActive);
        final var actualCategory = associateRepository.findById(actualId.getValue()).get();

        assertEquals(1, associateRepository.count());

        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDocument, actualCategory.getDocument());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void itShouldBeAbleToCreateANewInactiveAssociate() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, associateRepository.count());

        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = false;

        final var actualId = givenAnAssociate(expectedName, expectedDocument, expectedIsActive);
        final var actualCategory = associateRepository.findById(actualId.getValue()).get();

        assertEquals(1, associateRepository.count());

        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDocument, actualCategory.getDocument());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void itShouldNotBeAbleToCreateAnAssociateWithNullName() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, associateRepository.count());

        final String expectedName = null;
        final var expectedDocument = "12345678901";
        final var expectedIsActive = false;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aRequestBody = new CreateAssociateRequest(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        final var aRequest = MockMvcRequestBuilders.post("/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualResponse = this.mvc.perform(aRequest)
                .andDo(print());

        assertEquals(0, associateRepository.count());
        actualResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void itShouldBeAbleToReturnMultipleErrors() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, associateRepository.count());

        final String expectedName = null;
        final var expectedDocument = "1234567890";
        final var expectedIsActive = false;
        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "'name' should not be null";
        final var expectedErrorMessage2 = "'document' must have 11 characters";

        final var aRequestBody = new CreateAssociateRequest(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        final var aRequest = MockMvcRequestBuilders.post("/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualResponse = this.mvc.perform(aRequest)
                .andDo(print());

        assertEquals(0, associateRepository.count());
        actualResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage1)))
                .andExpect(jsonPath("$.errors[1].message", equalTo(expectedErrorMessage2)));
    }

    private AssociateID givenAnAssociate(
            final String aName,
            final String aDocument,
            final boolean isActive
    ) throws Exception {
        final var aRequestBody = new CreateAssociateRequest(
                aName,
                aDocument,
                isActive
        );

        final var aRequest = MockMvcRequestBuilders.post("/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualJson = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var response = Json.readValue(actualJson, Map.class);
        return AssociateID.from((String) response.get("id"));
    }
}
