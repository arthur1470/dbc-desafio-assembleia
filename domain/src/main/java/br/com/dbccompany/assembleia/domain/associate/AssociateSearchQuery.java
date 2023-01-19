
package br.com.dbccompany.assembleia.domain.associate;

public record AssociateSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
