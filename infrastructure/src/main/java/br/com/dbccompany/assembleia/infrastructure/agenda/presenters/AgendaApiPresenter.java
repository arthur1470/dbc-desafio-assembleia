package br.com.dbccompany.assembleia.infrastructure.agenda.presenters;

import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.get.AgendaOutput;
import br.com.dbccompany.assembleia.application.agenda.retrieve.list.AgendaListOutput;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaListResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.AgendaVotesResponse;
import br.com.dbccompany.assembleia.infrastructure.agenda.models.CreateAgendaResponse;

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
        final var votes = output.votes();
        final var df = new DecimalFormat("0.00");
        final var yesPercentage = df.format(votes.yesPercentage());
        final var noPercentage = df.format(votes.noPercentage());

        final var voteResponse = new AgendaVotesResponse(
                votes.totalVotes(),
                votes.yes(),
                votes.no(),
                yesPercentage,
                noPercentage
        );

        return new AgendaResponse(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt(),
                output.isVoteSessionActive(),
                output.voteSessionEndAt(),
                voteResponse
        );
    }
}
