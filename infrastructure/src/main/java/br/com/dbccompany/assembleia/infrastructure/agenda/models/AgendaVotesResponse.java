package br.com.dbccompany.assembleia.infrastructure.agenda.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AgendaVotesResponse(
        @JsonProperty("total_votes") int totalVotes,
        @JsonProperty("yes_votes") long yes,
        @JsonProperty("no_votes") long no,
        @JsonProperty("yes_votes_percentage") String yesPercentage,
        @JsonProperty("no_votes_percentage") String noPercentage
) {
}
