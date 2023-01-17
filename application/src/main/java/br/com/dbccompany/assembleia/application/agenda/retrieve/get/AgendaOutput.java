package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;

import java.time.Instant;

public record AgendaOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static AgendaOutput from(final Agenda anAgenda) {
        return new AgendaOutput(
                anAgenda.getId().getValue(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getUpdatedAt(),
                anAgenda.getDeletedAt()
        );
    }
}
