package br.com.dbccompany.assembleia.infrastructure.api.v1.clients;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateDocumentResponse(
        @JsonProperty("taxNumber") String document,
        @JsonProperty("valid") boolean valid,
        @JsonProperty("exists") boolean exists
) {
}
