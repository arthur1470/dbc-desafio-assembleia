package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import java.time.Instant;

public record VoteSessionOutput(
        String voteSessionId,
        boolean isVoteSessionActive,
        Instant voteSessionStartedAt,
        Instant voteSessionEndAt
){
    public static VoteSessionOutput with(
            String aSessionId,
            boolean isVoteSessionActive,
            Instant aSessionStartDate,
            Instant aSessionEndDate
    ) {
        return new VoteSessionOutput(
                aSessionId,
                isVoteSessionActive,
                aSessionStartDate,
                aSessionEndDate
        );
    }
}
