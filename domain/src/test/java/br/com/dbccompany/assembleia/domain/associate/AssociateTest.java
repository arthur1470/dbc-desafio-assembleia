package br.com.dbccompany.assembleia.domain.associate;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class AssociateTest {

    @Test
    void givenAValidParams_whenCallNewAssociate_thenInstantiateAnAssociate() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;

        // when
        final var actualAssociate = Assertions.assertDoesNotThrow(
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertNotNull(actualAssociate);
        Assertions.assertNotNull(actualAssociate.getId());
        Assertions.assertEquals(expectedName, actualAssociate.getName());
        Assertions.assertEquals(expectedDocument, actualAssociate.getDocument());
        Assertions.assertEquals(expectedIsActive, actualAssociate.isActive());
        Assertions.assertNotNull(actualAssociate.getCreatedAt());
        Assertions.assertNotNull(actualAssociate.getUpdatedAt());
        Assertions.assertNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAValidParams_whenCallNewAssociateWithActiveFalse_thenInstantiateAnAssociateWithDeletedAtNotNull() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = false;

        // when
        final var actualAssociate = Assertions.assertDoesNotThrow(
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertNotNull(actualAssociate);
        Assertions.assertNotNull(actualAssociate.getId());
        Assertions.assertEquals(expectedName, actualAssociate.getName());
        Assertions.assertEquals(expectedDocument, actualAssociate.getDocument());
        Assertions.assertEquals(expectedIsActive, actualAssociate.isActive());
        Assertions.assertNotNull(actualAssociate.getCreatedAt());
        Assertions.assertNotNull(actualAssociate.getUpdatedAt());
        Assertions.assertNotNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAValidActiveAssociate_whenCallDeactivate_thenShouldDeactivateAssociate() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = false;

        final var anAssociate = Associate.newAssociate(expectedName, expectedDocument, true);

        Assertions.assertTrue(anAssociate.isActive());
        Assertions.assertNull(anAssociate.getDeletedAt());

        // when
        final var actualAssociate = Assertions.assertDoesNotThrow(
                () -> anAssociate.createClone().deactivate()
        );

        // then
        Assertions.assertNotNull(actualAssociate);
        Assertions.assertNotNull(actualAssociate.getId());
        Assertions.assertEquals(anAssociate.getId(), actualAssociate.getId());
        Assertions.assertEquals(expectedName, actualAssociate.getName());
        Assertions.assertEquals(expectedDocument, actualAssociate.getDocument());
        Assertions.assertEquals(expectedIsActive, actualAssociate.isActive());
        Assertions.assertEquals(anAssociate.getCreatedAt(), actualAssociate.getCreatedAt());
        Assertions.assertTrue(anAssociate.getUpdatedAt().isBefore(actualAssociate.getUpdatedAt()));
        Assertions.assertNotNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAValidInactiveAssociate_whenCallActivate_thenShouldActivateAssociate() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;

        final var anAssociate = Associate.newAssociate(expectedName, expectedDocument, false);

        Assertions.assertFalse(anAssociate.isActive());
        Assertions.assertNotNull(anAssociate.getDeletedAt());

        // when
        final var actualAssociate = Assertions.assertDoesNotThrow(
                () -> anAssociate.createClone().activate()
        );

        // then
        Assertions.assertNotNull(actualAssociate);
        Assertions.assertNotNull(actualAssociate.getId());
        Assertions.assertEquals(anAssociate.getId(), actualAssociate.getId());
        Assertions.assertEquals(expectedName, actualAssociate.getName());
        Assertions.assertEquals(expectedDocument, actualAssociate.getDocument());
        Assertions.assertEquals(expectedIsActive, actualAssociate.isActive());
        Assertions.assertEquals(anAssociate.getCreatedAt(), actualAssociate.getCreatedAt());
        Assertions.assertTrue(anAssociate.getUpdatedAt().isBefore(actualAssociate.getUpdatedAt()));
        Assertions.assertNull(actualAssociate.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewAssociate_thenShouldThrowDomainException() {
        // given
        final String expectedName = null;
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewAssociate_thenShouldThrowDomainException() {
        // given
        final var expectedName = "   ";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan100_whenCallNewAssociate_thenShouldThrowDomainException() {
        // given
        final var expectedName = """
                carlos pedro joao augusto
                camila maria cilene joana
                diogo jonas cleiton allan
                debora beatriz liz louane
                +
                """;
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 100 characters";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullDocument_whenCallNewAssociate_thenShouldThrowDomainException() {
        final var expectedName = "Joao da Silva";
        final String expectedDocument = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' should not be null";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyDocument_whenCallNewAssociate_thenShouldThrowDomainException() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "  ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' should not be empty";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDocumentLengthMoreThan11_whenCallNewAssociate_thenShouldThrowDomainException() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "123456789112";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' must have 11 characters";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDocumentLengthLessThan11_whenCallNewAssociate_thenShouldThrowDomainException() {
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "1234567890";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' must have 11 characters";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.newAssociate(expectedName, expectedDocument, expectedIsActive)
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidParamsWithNullAuditoryDates_whenCallAssociateWith_thenShouldThrowDomainException() {
        final var expectedId = AssociateID.from("123");
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = false;
        final Instant expectedCreatedAt = null;
        final Instant expectedUpdatedAt = null;
        final Instant expectedDeletedAt = null;

        final var expectedErrorCount = 3;
        final var expectedErrorMessage1 = "'createdAt' should not be null";
        final var expectedErrorMessage2 = "'updatedAt' should not be null";
        final var expectedErrorMessage3 = "'deletedAt' should not be null when associate is inactive";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.with(
                        expectedId,
                        expectedName,
                        expectedDocument,
                        expectedIsActive,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessage3, actualException.getErrors().get(2).message());
    }

    @Test
    void givenAnInvalidInactiveAssociateWithNullDeletedAt_whenCallAssociateWith_thenShouldThrowDomainException() {
        final var expectedId = AssociateID.from("123");
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678901";
        final var expectedIsActive = false;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final Instant expectedDeletedAt = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'deletedAt' should not be null when associate is inactive";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Associate.with(
                        expectedId,
                        expectedName,
                        expectedDocument,
                        expectedIsActive,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedDeletedAt
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}