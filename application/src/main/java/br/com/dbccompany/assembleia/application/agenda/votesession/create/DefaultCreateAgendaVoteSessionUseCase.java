package br.com.dbccompany.assembleia.application.agenda.votesession.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultCreateAgendaVoteSessionUseCase extends CreateAgendaVoteSessionUseCase {

    private final AgendaGateway agendaGateway;

    public DefaultCreateAgendaVoteSessionUseCase(final AgendaGateway agendaGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
    }

    @Override
    public CreateAgendaVoteSessionOutput execute(final CreateAgendaVoteSessionCommand aCommand) {
        final var agendaId =  aCommand.agendaId();
        final var anEndDate = aCommand.endsAt();

        final var anAgenda = agendaGateway.findById(agendaId)
                .orElseThrow(() -> NotFoundException.with(Agenda.class, agendaId))
                .startVoteSession(anEndDate);

        return CreateAgendaVoteSessionOutput.from(agendaGateway.create(anAgenda));
    }
}
