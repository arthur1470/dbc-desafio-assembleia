package br.com.dbccompany.assembleia.infrastructure.api;

import br.com.dbccompany.assembleia.ControllerTest;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateCommand;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateOutput;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.infrastructure.associate.models.CreateAssociateRequest;
import br.com.dbccompany.assembleia.infrastructure.configuration.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
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
        final var request = MockMvcRequestBuilders.post("/associates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequest));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                status().isCreated(),
                MockMvcResultMatchers.header().string("Location", "/associates/123"),
                MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                jsonPath("$.id", equalTo("123"))
        );

        verify(createAssociateUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDocument, cmd.document())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
}
