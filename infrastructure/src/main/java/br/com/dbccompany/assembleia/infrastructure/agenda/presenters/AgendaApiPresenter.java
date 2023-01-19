package br.com.dbccompany.assembleia.infrastructure.agenda.presenters;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.AgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.VoteOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.VoteSessionOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.AgendaListOutput;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.*;

import java.text.DecimalFormat;

public interface AgendaApiPresenter {

    static CreateAgendaResponse present(final CreateAgendaOutput output) {
        return new CreateAgendaResponse(
                output.id()
        );
    }

    static AgendaListResponse present(final AgendaListOutput output) {
        return new AgendaListResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

    static AgendaResponse present(final AgendaOutput output) {
        final var voteSessionResponse = getVoteSessionResponse(output.voteSession());
        final var votesResponse = getVotesResponse(output.votes());

        return new AgendaResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt(),
                voteSessionResponse,
                votesResponse
        );
    }

    private static AgendaVoteSessionResponse getVoteSessionResponse(final VoteSessionOutput voteSession) {
        if (voteSession == null) return null;

        return new AgendaVoteSessionResponse(
                voteSession.voteSessionId(),
                voteSession.isVoteSessionActive(),
                voteSession.voteSessionStartedAt(),
                voteSession.voteSessionEndAt()
        );
    }

    private static AgendaVotesResponse getVotesResponse(final VoteOutput votes) {
        if (votes == null) return null;

        final var df = new DecimalFormat("0.00");
        final var yesPercentage = df.format(votes.yesPercentage());
        final var noPercentage = df.format(votes.noPercentage());

        return new AgendaVotesResponse(
                votes.totalVotes(),
                votes.yes(),
                votes.no(),
                yesPercentage,
                noPercentage
        );
    }
}
