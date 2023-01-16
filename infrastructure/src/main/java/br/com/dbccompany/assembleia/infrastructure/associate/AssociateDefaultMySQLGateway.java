package br.com.dbccompany.assembleia.infrastructure.associate;

import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.associate.AssociateGateway;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AssociateDefaultMySQLGateway implements AssociateGateway {

    private final AssociateRepository associateRepository;

    public AssociateDefaultMySQLGateway(final AssociateRepository associateRepository) {
        this.associateRepository = Objects.requireNonNull(associateRepository);
    }

    @Override
    public Associate create(final Associate anAssociate) {
        return this.associateRepository.save(AssociateJpaEntity.from(anAssociate))
                .toAggregate();
    }

    @Override
    public boolean existsByDocument(final String document) {
        return this.associateRepository.existsByDocument(document);
    }

    // TODO procurar uma API que receba o CPF e retorne se é válido ou não
    @Override
    public boolean isDocumentValid(final String document) {
        return true;
    }
}