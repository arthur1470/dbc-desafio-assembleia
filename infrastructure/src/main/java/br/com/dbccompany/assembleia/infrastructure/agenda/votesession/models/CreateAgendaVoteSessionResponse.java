package br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAgendaVoteSessionResponse(
        @JsonProperty("id") String id
) {
}
