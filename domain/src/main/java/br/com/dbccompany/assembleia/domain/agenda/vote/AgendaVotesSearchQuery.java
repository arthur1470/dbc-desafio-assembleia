package br.com.dbccompany.assembleia.domain.agenda.vote;

public record AgendaVotesSearchQuery(
        int page,
        int perPage,
        String sort,
        String direction
) {
}
