package br.com.dbccompany.assembleia.infrastructure.configuration;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.create.DefaultCreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.DefaultGetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.GetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.DefaultListAgendasUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.ListAgendasUseCase;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.create.DefaultCreateAssociateUseCase;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final AssociateGateway associateGateway;
    private final AgendaGateway agendaGateway;

    public UseCaseConfig(
            final AssociateGateway associateGateway,
            final AgendaGateway agendaGateway
    ) {
        this.associateGateway = associateGateway;
        this.agendaGateway = agendaGateway;
    }

    @Bean
    public CreateAssociateUseCase createAssociateUseCase() {
        return new DefaultCreateAssociateUseCase(associateGateway);
    }

    @Bean
    public CreateAgendaUseCase createAgendaUseCase() {
        return new DefaultCreateAgendaUseCase(agendaGateway);
    }

    @Bean
    public GetAgendaByIdUseCase getAgendaByIdUseCase() {
        return new DefaultGetAgendaByIdUseCase(agendaGateway);
    }

    @Bean
    public ListAgendasUseCase listAgendasUseCase() {
        return new DefaultListAgendasUseCase(agendaGateway);
    }
}
