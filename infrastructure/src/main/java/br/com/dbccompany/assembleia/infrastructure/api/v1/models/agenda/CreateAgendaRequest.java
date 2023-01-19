package br.com.dbccompany.assembleia.infrastructure.api.v1.models.agenda;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAgendaRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
}
