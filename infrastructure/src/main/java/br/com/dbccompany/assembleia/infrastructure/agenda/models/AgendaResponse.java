package br.com.dbccompany.assembleia.infrastructure.agenda.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AgendaResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt,
        @JsonProperty("is_vote_session_active") boolean isVoteSessionActive,
        @JsonProperty("vote_session_end_at") Instant voteSessionEndAt,
        @JsonProperty("votes") AgendaVotesResponse votes
) {
}
