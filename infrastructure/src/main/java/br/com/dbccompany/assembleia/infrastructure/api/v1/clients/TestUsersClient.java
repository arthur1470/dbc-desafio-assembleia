package br.com.dbccompany.assembleia.infrastructure.api.v1.clients;

import br.com.dbccompany.assembleia.domain.clients.UsersClient;
import br.com.dbccompany.assembleia.domain.clients.ValidateDocumentOutput;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"test-integration", "test-e2e"})
public class TestUsersClient implements UsersClient {
    @Override
    public ValidateDocumentOutput validateDocument(String document) {
        return new ValidateDocumentOutput(true, "ABLE_TO_VOTE");
    }
}
