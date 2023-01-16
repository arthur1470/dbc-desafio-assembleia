package br.com.dbccompany.assembleia.application.associate.create;

public record CreateAssociateCommand(
        String name,
        String document,
        boolean isActive
) {
    public static CreateAssociateCommand with(
            final String aName,
            final String aDocument,
            final boolean isActive
    ) {
        return new CreateAssociateCommand(
                aName,
                aDocument,
                isActive
        );
    }
}
