package br.com.dbccompany.assembleia.application.agenda.vote.create;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
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

        validateAssociateConstraints(anAssociateId, aVoteSessionId);

        final var aVote = Vote.newVote(aVoteType, anAssociateId);
        final var anAgenda = findAgendaByIdAndSession(anAgendaId, aVoteSessionId)
                .addVote(aVote);

        agendaGateway.create(anAgenda);
        return CreateAgendaVoteOutput.from(aVote);
    }

    private Agenda findAgendaByIdAndSession(final AgendaID anAgendaId, final VoteSessionID aVoteSessionId) {
        return agendaGateway.findById(anAgendaId)
                .filter(agenda -> agenda.getVoteSession().getId().equals(aVoteSessionId))
                .orElseThrow(() -> NotFoundException.with(Agenda.class, anAgendaId));
    }

    private void validateAssociateConstraints(final AssociateID anAssociateId, final VoteSessionID aSessionId) {
        final var anAssociate = associateGateway.findById(anAssociateId)
                .orElseThrow(() -> NotFoundException.with(Associate.class, anAssociateId));

        if (!anAssociate.isActive())
            throw DomainException.with("Associate %s is currently inactive".formatted(anAssociateId.getValue()));

        if (agendaGateway.existsByAssociateAndVoteSession(anAssociateId, aSessionId))
            throw DomainException.with("Associate already voted for this agenda");

        final var aDocumentValidation =
                associateGateway.isDocumentValid(anAssociate.getDocument());

        if (aDocumentValidation.status().equalsIgnoreCase("UNABLE_TO_VOTE"))
            throw DomainException.with("Associate %s is unable to vote".formatted(anAssociate.getDocument()));
    }
}
