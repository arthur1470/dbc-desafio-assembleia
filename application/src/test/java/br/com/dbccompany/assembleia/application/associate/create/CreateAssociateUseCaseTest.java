package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAssociateUseCaseTest {

    @InjectMocks
    private DefaultCreateAssociateUseCase useCase;
    @Mock
    private AssociateGateway associateGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(associateGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateAssociate_shouldReturnAssociateId() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(associateGateway.existsByDocument(any()))
                .thenReturn(false);

        when(associateGateway.isDocumentValid(any()))
                .thenReturn(true);

        when(associateGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(associateGateway, times(1)).create(argThat(anAssociate ->
                Objects.equals(expectedName, anAssociate.getName())
                        && Objects.equals(expectedDocument, anAssociate.getDocument())
                        && Objects.equals(expectedIsActive, anAssociate.isActive())
                        && Objects.nonNull(anAssociate.getId())
                        && Objects.nonNull(anAssociate.getCreatedAt())
                        && Objects.nonNull(anAssociate.getUpdatedAt())
                        && Objects.isNull(anAssociate.getDeletedAt())
        ));
    }

    @Test
    void givenAValidInactiveCommand_whenCallsCreateAssociate_shouldReturnAssociateId() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = false;

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(associateGateway.existsByDocument(any()))
                .thenReturn(false);

        when(associateGateway.isDocumentValid(any()))
                .thenReturn(true);

        when(associateGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(associateGateway, times(1)).create(argThat(anAssociate ->
                Objects.equals(expectedName, anAssociate.getName())
                        && Objects.equals(expectedDocument, anAssociate.getDocument())
                        && Objects.equals(expectedIsActive, anAssociate.isActive())
                        && Objects.nonNull(anAssociate.getId())
                        && Objects.nonNull(anAssociate.getCreatedAt())
                        && Objects.nonNull(anAssociate.getUpdatedAt())
                        && Objects.nonNull(anAssociate.getDeletedAt())
        ));
    }

    @Test
    void givenAnInvalidAlreadyRegisteredDocument_whenCallsCreateAssociate_shouldThrowDomainException() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' 12345678911 is already registered";

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(associateGateway.existsByDocument(any()))
                .thenReturn(true);

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDocument_whenCallsCreateAssociate_shouldThrowDomainException() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' 12345678911 is invalid";

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        when(associateGateway.existsByDocument(any()))
                .thenReturn(false);

        when(associateGateway.isDocumentValid(any()))
                .thenReturn(false);

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNullName_whenCallsCreateAssociate_shouldThrowDomainException() {
        // given
        final var expectedDocument = "12345678911";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateAssociateCommand.with(
                null,
                expectedDocument,
                expectedIsActive
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDocumentLengthLessThan11_whenCallsCreateAssociate_shouldThrowDomainException() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "1234567891";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' must have 11 characters";

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidDocumentLengthMoreThan11_whenCallsCreateAssociate_shouldThrowDomainException() {
        // given
        final var expectedName = "Joao da Silva";
        final var expectedDocument = "123456789199";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'document' must have 11 characters";

        final var aCommand = CreateAssociateCommand.with(
                expectedName,
                expectedDocument,
                expectedIsActive
        );

        // when
        final var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}