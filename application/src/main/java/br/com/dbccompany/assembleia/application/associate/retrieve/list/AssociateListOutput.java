package br.com.dbccompany.assembleia.application.associate.retrieve.list;

import br.com.dbccompany.assembleia.domain.associate.Associate;

import java.time.Instant;

public record AssociateListOutput(
        String id,
        String name,
        String document,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {
    public static AssociateListOutput from(final Associate anAssociate) {
        return new AssociateListOutput(
                anAssociate.getId().getValue(),
                anAssociate.getName(),
                anAssociate.getDocument(),
                anAssociate.isActive(),
                anAssociate.getCreatedAt(),
                anAssociate.getDeletedAt()
        );
    }
}
