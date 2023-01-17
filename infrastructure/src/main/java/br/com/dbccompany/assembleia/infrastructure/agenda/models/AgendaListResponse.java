package br.com.dbccompany.assembleia.infrastructure.agenda.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record AgendaListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
