package br.com.dbccompany.assembleia.domain.agenda;

public record AgendaSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
