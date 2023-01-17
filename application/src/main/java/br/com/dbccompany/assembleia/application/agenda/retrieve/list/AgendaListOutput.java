package br.com.dbccompany.assembleia.application.agenda.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;

import java.time.Instant;

public record AgendaListOutput(
        String id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {
    public static AgendaListOutput from(final Agenda anAgenda) {
        return new AgendaListOutput(
                anAgenda.getId().getValue(),
                anAgenda.getName(),
                anAgenda.getDescription(),
                anAgenda.isActive(),
                anAgenda.getCreatedAt(),
                anAgenda.getDeletedAt()
        );
    }
}
