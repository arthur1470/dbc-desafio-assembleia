package br.com.dbccompany.assembleia.domain.associate;

import java.util.Optional;

public interface AssociateGateway {
    Associate create(Associate anAssociate);
    Optional<Associate> findById(AssociateID anId);
    boolean existsById(AssociateID anId);
    boolean existsByDocument(String aDocument);
    boolean isDocumentValid(String aDocument);
}
