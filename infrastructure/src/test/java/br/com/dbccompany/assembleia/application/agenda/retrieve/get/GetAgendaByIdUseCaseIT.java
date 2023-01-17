package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.application.agenda.create.CreateAgendaCommand;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.exceptions.DomainException;
import br.com.dbccompany.assembleia.domain.exceptions.NotFoundException;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class GetAgendaByIdUseCaseIT {

    @Autowired
    private GetAgendaByIdUseCase useCase;
    @Autowired
    private AgendaRepository agendaRepository;
    @SpyBean
    private AgendaGateway agendaGateway;

    @Test
    void givenAValidId_whenCallsGetAgenda_shouldReturnAgenda() {
        final var expectedName = "Agenda Importante";
        final var expectedDescription = "Essa é uma pauta muito importante que todos devem votar!";
        final var expectedIsActive = true;

        final var anAgenda = Agenda.newAgenda(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var expectedId = anAgenda.getId();

        assertEquals(0, agendaRepository.count());

        agendaRepository.save(AgendaJpaEntity.from(anAgenda));

        assertEquals(1, agendaRepository.count());

        final var actualCategory = useCase.execute(expectedId.getValue());

        Mockito.verify(agendaGateway, Mockito.times(1)).findById(expectedId);

        assertEquals(expectedId.getValue(), actualCategory.id());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(anAgenda.getCreatedAt(), actualCategory.createdAt());
        assertEquals(anAgenda.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(anAgenda.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAnInvalidId_whenCallsGetAgenda_shouldThrowNotFound() {
        final var expectedId = AgendaID.from("123");
        final var expectedErrorMessage = "Agenda with ID 123 was not found";

        assertEquals(0, agendaRepository.count());

        final var actualException = assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(0, agendaRepository.count());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
