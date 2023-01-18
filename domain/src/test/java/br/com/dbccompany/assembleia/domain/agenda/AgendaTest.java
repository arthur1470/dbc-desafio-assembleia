package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static br.com.dbccompany.assembleia.domain.agenda.vote.Vote.newVote;
import static br.com.dbccompany.assembleia.domain.agenda.vote.VoteType.YES;
import static br.com.dbccompany.assembleia.domain.associate.AssociateID.unique;

class AgendaTest {

    @Test
    void givenAValidParams_whenCallNewAgenda_thenInstantiateAnAgenda() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;

        // when
        final var actualAgenda = Assertions.assertDoesNotThrow(
                () -> Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
        );

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNull(actualAgenda.getDeletedAt());
    }

    @Test
    void givenAValidParams_whenCallNewAgendaWithActiveFalse_thenInstantiateAnAgendaWithDeletedAtNotNull() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = false;

        // when
        final var actualAgenda = Assertions.assertDoesNotThrow(
                () -> Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
        );

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNotNull(actualAgenda.getDeletedAt());
    }

    @Test
    void givenAValidActiveAssociate_whenCallDeactivate_thenShouldDeactivateAgenda() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = false;

        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, true);

        Assertions.assertTrue(anAgenda.isActive());
        Assertions.assertNull(anAgenda.getDeletedAt());

        // when
        final var actualAgenda = Assertions.assertDoesNotThrow(
                () -> anAgenda.createClone().deactivate()
        );

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNotNull(actualAgenda.getDeletedAt());
    }

    @Test
    void givenAValidInactiveAgenda_whenCallActivate_thenShouldActivateAgenda() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, false);

        Assertions.assertFalse(anAgenda.isActive());
        Assertions.assertNotNull(anAgenda.getDeletedAt());

        // when
        final var actualAgenda = Assertions.assertDoesNotThrow(
                () -> anAgenda.createClone().activate()
        );

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNull(actualAgenda.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewAgenda_thenShouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Agenda.newAgenda(null, "uma descricao", true)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewAgenda_thenShouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Agenda.newAgenda("    ", "uma descricao", true)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewAgenda_thenShouldThrowDomainException() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Agenda.newAgenda("12", "uma descricao", true)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallNewAgenda_thenShouldThrowDomainException() {
        // given
        final var anInvalidName = """
                dasmkdasdklasdkasklmdaskmldmksadasdasdaudhaiudhsuhffdsiojaioj
                fdsklnfdsafndslkfnasdlkfnksdlfnfdsijfjioasifjoadsijofdjisoafij
                fnsdfndskjfnaskdfndskjfnkjnfnsdkfsdfhidashufdsuhfhdsiufhsdiuf
                fnsdjkfndksajnfkdjsnfksdnkjfnajsfhsdfhsduhfisudfhiasdhfdshuhfu
                fnsdjkfndksajnfkdjsnfksdnkjfnajsfhsdfhsduhfisudfhiasdhfdshuhfu
                """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Agenda.newAgenda(anInvalidName, "uma descricao", true)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAValidParamsWithNullDescription_whenCallNewAgenda_thenInstantiateAgenda() {
        // given
        final var expectedName = "Agenda Importante";
        final String expectedDescription = null;
        final var expectedIsActive = true;

        // when
        final var actualAgenda = Assertions.assertDoesNotThrow(
                () -> Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
        );

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertNull(actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNull(actualAgenda.getDeletedAt());
    }

    @Test
    void givenAValidAgenda_whenCallStartVoteSession_thenShouldReturnAgendaWithVoteSession() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(10));

        // when
        final var actualAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
                .startVoteSession(expectedEndDate);

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNull(actualAgenda.getDeletedAt());
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertEquals(expectedEndDate, actualAgenda.getVoteSession().getEndedAt());
    }

    @Test
    void givenAValidAgendaWithVoteSessionStarted_whenCallStartVoteSessionAgain_thenShouldThrowDomainException() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(1));
        final var actualAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
                .startVoteSession(expectedEndDate);

        final var expectedErrorMessage = "This agenda already has a voting session";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> actualAgenda.startVoteSession(expectedEndDate)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAValidAgendaWithVoteSession_whenCallAddVote_thenShouldReturnAgendaWithVote() {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMinutes(10));
        final var expectedVotesSize = 1;

        // when
        final var actualAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
                .startVoteSession(expectedEndDate)
                .addVote(newVote(YES, unique()));

        // then
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertNotNull(actualAgenda.getId());
        Assertions.assertEquals(expectedName, actualAgenda.getName());
        Assertions.assertEquals(expectedDescription, actualAgenda.getDescription());
        Assertions.assertEquals(expectedIsActive, actualAgenda.isActive());
        Assertions.assertNotNull(actualAgenda.getCreatedAt());
        Assertions.assertNotNull(actualAgenda.getUpdatedAt());
        Assertions.assertNull(actualAgenda.getDeletedAt());
        Assertions.assertNotNull(actualAgenda);
        Assertions.assertEquals(expectedEndDate, actualAgenda.getVoteSession().getEndedAt());
        Assertions.assertEquals(expectedVotesSize, actualAgenda.getVoteSession().getVotes().size());
    }

    @Test
    void givenAValidAgendaWithExpiredVoteSession_whenCallAddVote_thenShouldThrowDomainException() throws InterruptedException {
        // given
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma agenda muito importante que todos devem votar!";
        final var expectedIsActive = true;
        final var expectedEndDate = InstantUtils.now().plus(Duration.ofMillis(10));
        final var anAgenda = Agenda.newAgenda(expectedName, expectedDescription, expectedIsActive)
                .startVoteSession(expectedEndDate);

        final var expectedErrorMessage = "This agenda does not have an active voting session";

        Thread.sleep(12);

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> anAgenda.addVote(newVote(YES, unique()))
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}