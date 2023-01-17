package br.com.dbccompany.assembleia.application.agenda.create;

public record CreateAgendaCommand(
        String name,
        String description,
        boolean active
) {
    public static CreateAgendaCommand with(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) {
        return new CreateAgendaCommand(
                aName,
                aDescription,
                isActive
        );
    }
}
