package br.com.dbccompany.assembleia.infrastructure.associate;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.associate.AssociateSearchQuery;
import br.com.dbccompany.assembleia.domain.clients.UsersClient;
import br.com.dbccompany.assembleia.domain.clients.ValidateDocumentOutput;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import br.com.dbccompany.assembleia.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static br.com.dbccompany.assembleia.infrastructure.utils.SpecificationUtils.like;

@Component
public class AssociateDefaultMySQLGateway implements AssociateGateway {

    private final AssociateRepository associateRepository;
    private final UsersClient usersClient;

    public AssociateDefaultMySQLGateway(final AssociateRepository associateRepository, final UsersClient usersClient) {
        this.associateRepository = Objects.requireNonNull(associateRepository);
        this.usersClient = Objects.requireNonNull(usersClient);
    }

    @Override
    public Associate create(final Associate anAssociate) {
        return this.associateRepository.save(AssociateJpaEntity.from(anAssociate))
                .toAggregate();
    }

    @Override
    public Optional<Associate> findById(final AssociateID anId) {
        return this.associateRepository.findById(anId.getValue())
                .map(AssociateJpaEntity::toAggregate);
    }

    @Override
    public boolean existsById(AssociateID anId) {
        return this.associateRepository.existsById(anId.getValue());
    }

    @Override
    public boolean existsByDocument(final String document) {
        return this.associateRepository.existsByDocument(document);
    }

    @Override
    public ValidateDocumentOutput isDocumentValid(final String document) {
        return usersClient.validateDocument(document);
    }

    @Override
    public Pagination<Associate> findAll(final AssociateSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(term ->
                        SpecificationUtils.<AssociateJpaEntity>like("name", term)
                                .or(like("document", term))
                )
                .orElse(null);

        final var pageResult =
                this.associateRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(AssociateJpaEntity::toAggregate).toList()
        );
    }
}
