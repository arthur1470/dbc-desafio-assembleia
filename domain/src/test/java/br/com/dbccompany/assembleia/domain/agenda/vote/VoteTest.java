package br.com.dbccompany.assembleia.domain.agenda.vote;

import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void givenAValidVoteYes_whenCallNewVote_thenInstantiateAVote() {
        // given
        final var expectedVote = VoteType.YES;
        final var expectedAssociate = AssociateID.from("123");

        // when
        final var actualVote = Assertions.assertDoesNotThrow(
                () -> Vote.newVote(expectedVote, expectedAssociate)
        );

        // then
        Assertions.assertNotNull(actualVote);
        Assertions.assertNotNull(actualVote.getId());
        Assertions.assertEquals(expectedVote, actualVote.getVote());
        Assertions.assertEquals(expectedAssociate, actualVote.getAssociate());
        Assertions.assertNotNull(actualVote.getCreatedAt());
    }

    @Test
    void givenAValidVoteNo_whenCallNewVote_thenInstantiateAVote() {
        // given
        final var expectedVote = VoteType.NO;
        final var expectedAssociate = AssociateID.from("123");

        // when
        final var actualVote = Assertions.assertDoesNotThrow(
                () -> Vote.newVote(expectedVote, expectedAssociate)
        );

        // then
        Assertions.assertNotNull(actualVote);
        Assertions.assertNotNull(actualVote.getId());
        Assertions.assertEquals(expectedVote, actualVote.getVote());
        Assertions.assertEquals(expectedAssociate, actualVote.getAssociate());
        Assertions.assertNotNull(actualVote.getCreatedAt());
    }

    @Test
    void givenAnInvalidNullVote_whenCallNewVote_thenShouldThrowDomainException() {
        // given
        final VoteType expectedVote = null;
        final var expectedAssociate = AssociateID.from("123");
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'vote' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Vote.newVote(expectedVote, expectedAssociate)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullAssociate_whenCallNewVote_thenShouldThrowDomainException() {
        // given
        final var expectedVote = VoteType.YES;
        final AssociateID expectedAssociate = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'associate' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Vote.newVote(expectedVote, expectedAssociate)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullAssociateAndNullVote_whenCallNewVote_thenShouldThrowDomainExceptionWith2Messages() {
        // given
        final var expectedErrorCount = 2;
        final var expectedErrorMessage1 = "'vote' should not be null";
        final var expectedErrorMessage2 = "'associate' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Vote.newVote(null, null)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
    }
}