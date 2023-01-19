package br.com.dbccompany.assembleia.infrastructure.agenda.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AgendaVoteSessionResponse(
        @JsonProperty("vote_session_id") String voteSessionId,
        @JsonProperty("is_vote_session_active") boolean isVoteSessionActive,
        @JsonProperty("vote_session_started_at") Instant voteSessionStartedAt,
        @JsonProperty("vote_session_end_at") Instant voteSessionEndAt
) {
}
