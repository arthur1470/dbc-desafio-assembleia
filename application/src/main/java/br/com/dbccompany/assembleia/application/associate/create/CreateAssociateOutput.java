package br.com.dbccompany.assembleia.application.associate.create;

import br.com.dbccompany.assembleia.domain.associate.Associate;

public record CreateAssociateOutput(
        String id
) {
    public static CreateAssociateOutput from(final Associate anAssociate) {
        return new CreateAssociateOutput(
                anAssociate.getId().getValue()
        );
    }

    public static CreateAssociateOutput from(final String anId) {
        return new CreateAssociateOutput(
                anId
        );
    }
}
