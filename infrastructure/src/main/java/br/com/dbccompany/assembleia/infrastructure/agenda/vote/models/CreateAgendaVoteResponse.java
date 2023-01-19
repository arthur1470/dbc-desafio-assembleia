package br.com.dbccompany.assembleia.infrastructure.agenda.vote.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAgendaVoteResponse(@JsonProperty("id") String id) {
}
