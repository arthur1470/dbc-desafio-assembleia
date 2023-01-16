package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateAssociateUseCaseTest {

    @InjectMocks
    CreateAssociateUseCase useCase;
    @Mock
    AssociateGateway associateGateway;

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

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(associateGateway, times(1)).create(Mockito.argThat(anAssociate ->
                Objects.equals(expectedName, anAssociate.getName())
                && Objects.equals(expectedDocument, anAssociate.getDocument())
                && Objects.equals(expectedIsActive, anAssociate.isActive())
                && Objects.nonNull(anAssociate.getId())
                && Objects.nonNull(anAssociate.getCreatedAt())
                && Objects.nonNull(anAssociate.getUpdatedAt())
                && Objects.isNull(anAssociate.getDeletedAt())
        ));
    }
}