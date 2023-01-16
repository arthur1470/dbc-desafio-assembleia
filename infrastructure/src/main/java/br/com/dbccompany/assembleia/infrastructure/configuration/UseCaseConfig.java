package br.com.dbccompany.assembleia.infrastructure.configuration;

import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.create.DefaultCreateAssociateUseCase;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final AssociateGateway associateGateway;

    public UseCaseConfig(final AssociateGateway associateGateway) {
        this.associateGateway = associateGateway;
    }

    @Bean
    public CreateAssociateUseCase createAssociateUseCase() {
        return new DefaultCreateAssociateUseCase(associateGateway);
    }
}
