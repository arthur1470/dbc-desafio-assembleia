package br.com.dbccompany.assembleia.infrastructure.configuration;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.create.DefaultCreateAgendaUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.DefaultGetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.GetAgendaByIdUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.DefaultListAgendasUseCase;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.ListAgendasUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.create.CreateAgendaVoteUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.create.DefaultCreateAgendaVoteUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.DefaultListAgendaVotesUseCase;
import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.ListAgendaVotesUseCase;
import br.com.dbccompany.assembleia.application.agenda.votesession.create.CreateAgendaVoteSessionUseCase;
import br.com.dbccompany.assembleia.application.agenda.votesession.create.DefaultCreateAgendaVoteSessionUseCase;
import br.com.dbccompany.assembleia.application.associate.create.CreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.create.DefaultCreateAssociateUseCase;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.DefaultListAssociatesUseCase;
import br.com.dbccompany.assembleia.application.associate.retrieve.list.ListAssociatesUseCase;
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
    public ListAssociatesUseCase listAssociatesUseCase() {
        return new DefaultListAssociatesUseCase(associateGateway);
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

    @Bean
    public CreateAgendaVoteUseCase createAgendaVoteUseCase() {
        return new DefaultCreateAgendaVoteUseCase(agendaGateway, associateGateway);
    }

    @Bean
    public CreateAgendaVoteSessionUseCase createAgendaVoteSessionUseCase() {
        return new DefaultCreateAgendaVoteSessionUseCase(agendaGateway);
    }

    @Bean
    public ListAgendaVotesUseCase listAgendaVotesUseCase() {
        return new DefaultListAgendaVotesUseCase(agendaGateway);
    }
}
