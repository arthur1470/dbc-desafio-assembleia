package br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AgendaVotesResponse(
        @JsonProperty("total_votes") int totalVotes,
        @JsonProperty("yes_votes") long yes,
        @JsonProperty("no_votes") long no,
        @JsonProperty("yes_votes_percentage") String yesPercentage,
        @JsonProperty("no_votes_percentage") String noPercentage
) {
}
