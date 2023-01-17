package br.com.dbccompany.assembleia.domain.agenda;

import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}