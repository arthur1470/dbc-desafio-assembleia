package br.com.dbccompany.assembleia.infrastructure.api.v1.models.associate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAssociateRequest(
        @JsonProperty("name") String name,
        @JsonProperty("document") String document,
        @JsonProperty("is_active") Boolean active
) {
}
