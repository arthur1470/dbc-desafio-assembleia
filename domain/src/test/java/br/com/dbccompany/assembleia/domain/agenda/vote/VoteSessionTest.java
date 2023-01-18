package br.com.dbccompany.assembleia.domain.agenda.vote;

import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSession;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static br.com.dbccompany.assembleia.domain.agenda.vote.Vote.newVote;
import static br.com.dbccompany.assembleia.domain.agenda.vote.VoteType.NO;
import static br.com.dbccompany.assembleia.domain.agenda.vote.VoteType.YES;
import static br.com.dbccompany.assembleia.domain.associate.AssociateID.unique;

class VoteSessionTest {

    @Test
    void givenAValidParams_whenCallNewVoteSession_thenInstantiateAVoteSession() {
        // given
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(1));
        final var expectedVotesSize = 0;

        // when
        final var actualVoteSession = Assertions.assertDoesNotThrow(
                () -> VoteSession.newVoteSession(expectedEndDate)
        );

        // then
        Assertions.assertNotNull(actualVoteSession);
        Assertions.assertNotNull(actualVoteSession.getId());
        Assertions.assertNotNull(actualVoteSession.getStartedAt());
        Assertions.assertEquals(expectedEndDate, actualVoteSession.getEndedAt());
        Assertions.assertEquals(expectedVotesSize, actualVoteSession.getVotes().size());
    }

    @Test
    void givenAValidParamsWithNullEndDate_whenCallNewVoteSession_thenInstantiateAVoteSessionWithEndDate1MinDefault() {
        // given
        final var expectedTimeDifference = 1;
        final var expectedVotesSize = 0;

        // when
        final var actualVoteSession = Assertions.assertDoesNotThrow(
                () -> VoteSession.newVoteSession(null)
        );

        // then
        Assertions.assertNotNull(actualVoteSession);
        Assertions.assertNotNull(actualVoteSession.getId());
        Assertions.assertNotNull(actualVoteSession.getStartedAt());
        Assertions.assertNotNull(actualVoteSession.getEndedAt());
        Assertions.assertEquals(expectedTimeDifference, actualVoteSession.getEndedAt().compareTo(actualVoteSession.getStartedAt()));
        Assertions.assertEquals(expectedVotesSize, actualVoteSession.getVotes().size());
    }

    @Test
    void givenAValidVoteSession_whenCallAddVote_thenShouldReturnSessionWithVotes() {
        // given
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(10));
        final var expectedVotesSize = 3;

        // when
        final var aVoteSession = VoteSession.newVoteSession(expectedEndDate);

        final var actualVoteSession = aVoteSession
                .addVote(newVote(YES, unique()))
                .addVote(newVote(NO, unique()))
                .addVote(newVote(YES, unique()));

        // then
        Assertions.assertNotNull(actualVoteSession);
        Assertions.assertNotNull(actualVoteSession.getId());
        Assertions.assertNotNull(actualVoteSession.getStartedAt());
        Assertions.assertEquals(expectedEndDate, actualVoteSession.getEndedAt());
        Assertions.assertEquals(expectedVotesSize, actualVoteSession.getVotes().size());
    }

    @Test
    void givenAValidVoteSession_whenCallAddVoteWithNullVote_thenShouldReturnSessionWithoutVotes() {
        // given
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(10));
        final var expectedVotesSize = 0;

        // when
        final var aVoteSession = VoteSession.newVoteSession(expectedEndDate);

        final var actualVoteSession = aVoteSession
                .addVote(null)
                .addVote(null);

        // then
        Assertions.assertNotNull(actualVoteSession);
        Assertions.assertNotNull(actualVoteSession.getId());
        Assertions.assertNotNull(actualVoteSession.getStartedAt());
        Assertions.assertEquals(expectedEndDate, actualVoteSession.getEndedAt());
        Assertions.assertEquals(expectedVotesSize, actualVoteSession.getVotes().size());
    }
}