package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSession;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Instant;

public record AgendaOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,
        VoteSessionOutput voteSession,
        VoteOutput votes
) {
    public static AgendaOutput from(final Agenda anAgenda) {
        final var aPossibleNullVoteSession = anAgenda.getVoteSession();

        final var voteSession = getVoteSession(aPossibleNullVoteSession);
        final var votesOutput = getVotes(aPossibleNullVoteSession);

        return new AgendaOutput(
                anAgenda.getId().getValue(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getUpdatedAt(),
                anAgenda.getDeletedAt(),
                voteSession,
                votesOutput
        );
    }

    private static VoteSessionOutput getVoteSession(final VoteSession aVoteSession) {
        if (aVoteSession == null) return null;

        return VoteSessionOutput.with(
                aVoteSession.getId().getValue(),
                aVoteSession.getEndedAt().isAfter(InstantUtils.now()),
                aVoteSession.getStartedAt(),
                aVoteSession.getEndedAt()
        );
    }

    private static VoteOutput getVotes(final VoteSession aVoteSession) {
        if (aVoteSession == null || aVoteSession.getVotes().isEmpty()) return null;

        final var votes = aVoteSession.getVotes();
        final var totalVotes = votes.size();
        final var positiveVotes = votes.stream().filter(vote -> vote.getVote().equals(VoteType.YES)).count();
        final var negativeVotes = totalVotes - positiveVotes;

        return VoteOutput.with(totalVotes, positiveVotes, negativeVotes);
    }
}
