package br.com.dbccompany.assembleia.infrastructure.api.v1.clients;

import br.com.dbccompany.assembleia.domain.clients.UsersClient;
import br.com.dbccompany.assembleia.domain.clients.ValidateDocumentOutput;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Component
@Profile({"development", "production"})
public class DefaultUsersClient implements UsersClient {

    @Override
    public ValidateDocumentOutput validateDocument(final String aDocument) {
        final var aUrl = "https://api.nfse.io/validate/NaturalPeople/taxNumber/{document}";
        final var restTemplate = new RestTemplate();

        final var alternatives = new String[]{"ABLE_TO_VOTE", "UNABLE_TO_VOTE"};
        final var select = (int) Instant.now().getEpochSecond() % 2;

        final var status = alternatives[select];

        try {
            final var response =
                    restTemplate.getForObject(aUrl, ValidateDocumentResponse.class, aDocument);

            final var isValid = Optional.ofNullable(response)
                    .map(ValidateDocumentResponse::valid)
                    .orElse(true);

            return new ValidateDocumentOutput(isValid, status);
        } catch (RuntimeException ex) {
            return new ValidateDocumentOutput(true, status);
        }
    }
}
