package br.com.dbccompany.assembleia.infrastructure.agenda.vote.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAgendaVoteRequest(
        @JsonProperty("vote") String vote,
        @JsonProperty("associate_id") String associateId
) {
}
