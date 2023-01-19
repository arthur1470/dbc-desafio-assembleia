package br.com.dbccompany.assembleia.domain.associate;

import br.com.dbccompany.assembleia.domain.clients.ValidateDocumentOutput;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;

import java.util.Optional;

public interface AssociateGateway {
    Associate create(Associate anAssociate);

    Optional<Associate> findById(AssociateID anId);

    boolean existsById(AssociateID anId);

    boolean existsByDocument(String aDocument);

    ValidateDocumentOutput isDocumentValid(String aDocument);

    Pagination<Associate> findAll(AssociateSearchQuery aQuery);
}
