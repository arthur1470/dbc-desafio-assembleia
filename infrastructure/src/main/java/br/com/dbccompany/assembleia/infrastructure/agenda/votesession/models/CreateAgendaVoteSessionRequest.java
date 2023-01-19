package br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAgendaVoteSessionRequest(
        @JsonProperty("duration_type") String durationType,
        @JsonProperty("duration") Integer duration
) {
}
