package br.com.dbccompany.assembleia.infrastructure.agenda.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AgendaVoteSessionResponse(
        @JsonProperty("id") String voteSessionId,
        @JsonProperty("is_active") boolean isVoteSessionActive,
        @JsonProperty("started_at") Instant voteSessionStartedAt,
        @JsonProperty("end_at") Instant voteSessionEndAt
) {
}
