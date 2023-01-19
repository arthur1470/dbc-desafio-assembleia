package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultCreateAgendaVoteUseCase extends CreateAgendaVoteUseCase {

    private final AgendaGateway agendaGateway;
    private final AssociateGateway associateGateway;

    public DefaultCreateAgendaVoteUseCase(final AgendaGateway agendaGateway, final AssociateGateway associateGateway) {
        this.agendaGateway = Objects.requireNonNull(agendaGateway);
        this.associateGateway = Objects.requireNonNull(associateGateway);
    }

    @Override
    public CreateAgendaVoteOutput execute(final CreateAgendaVoteCommand aCommand) {
        final var anAgendaId = aCommand.agendaId();
        final var aVoteSessionId = aCommand.voteSessionId();
        final var anAssociateId = aCommand.associateId();
        final var aVoteType = aCommand.vote();

        if(!associateGateway.existsById(anAssociateId))
            throw NotFoundException.with(Associate.class, anAssociateId);

        if(agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aVoteSessionId))
            throw DomainException.with("Associate already voted for this agenda");

        final var aVote = Vote.newVote(aVoteType, anAssociateId);

        final var anAgenda = agendaGateway.findById(anAgendaId)
                .filter(agenda -> agenda.getVoteSession().getId().equals(aVoteSessionId))
                .orElseThrow(() -> NotFoundException.with(Agenda.class, anAgendaId))
                .addVote(aVote);

        agendaGateway.create(anAgenda);
        return CreateAgendaVoteOutput.from(aVote);
    }
}