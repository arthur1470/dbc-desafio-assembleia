package br.com.dbccompany.assembleia.domain.clients;

public record ValidateDocumentOutput(
        boolean isValid,
        String status
) {
}
