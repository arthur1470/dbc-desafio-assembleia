package br.com.dbccompany.assembleia.infrastructure.agenda.vote.models;

import br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list.AgendaVotesListOutput;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AgendaVotesListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("vote") String vote,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("associate_id") String associateId
) {
    public static AgendaVotesListResponse from(final AgendaVotesListOutput anOutput) {
        return new AgendaVotesListResponse(
                anOutput.id(),
                anOutput.vote(),
                anOutput.createdAt(),
                anOutput.associateId().getValue()
        );
    }
}
