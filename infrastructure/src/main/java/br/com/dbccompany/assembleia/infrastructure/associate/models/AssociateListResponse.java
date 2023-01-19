package br.com.dbccompany.assembleia.infrastructure.associate.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AssociateListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("document") String document,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
