package br.com.dbccompany.assembleia.domain.associate;

public interface AssociateGateway {
    Associate create(Associate anAssociate);
    boolean existsByDocument(String document);
    boolean isDocumentValid(String document);
}
