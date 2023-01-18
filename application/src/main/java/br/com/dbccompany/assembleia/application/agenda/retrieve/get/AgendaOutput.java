package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSession;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

public record AgendaOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        boolean isVoteSessionActive,
        Instant voteSessionEndAt,
        VoteOutput votes
) {
    public static AgendaOutput from(final Agenda anAgenda) {
        final var voteSession = Optional.ofNullable(anAgenda.getVoteSession());

        final var voteSessionEndAt = voteSession.map(VoteSession::getEndedAt)
                .orElse(null);

        final var isVoteSessionActive = voteSessionEndAt != null && voteSessionEndAt.isAfter(InstantUtils.now());

        final var votes = voteSession.isPresent() ? voteSession.get().getVotes() : Collections.<Vote>emptySet();
        final var totalVotes = votes.size();
        final var positiveVotes = votes.stream().filter(vote -> vote.getVote().equals(VoteType.YES)).count();
        final var negativeVotes = totalVotes - positiveVotes;

        final var votesOutput = VoteOutput.with(totalVotes, positiveVotes, negativeVotes);

        return new AgendaOutput(
                anAgenda.getId().getValue(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getUpdatedAt(),
                anAgenda.getDeletedAt(),
                isVoteSessionActive,
                voteSessionEndAt,
                votesOutput
        );
    }
}
