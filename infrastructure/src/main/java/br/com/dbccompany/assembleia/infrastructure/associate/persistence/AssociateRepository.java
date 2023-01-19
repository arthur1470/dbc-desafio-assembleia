package br.com.dbccompany.assembleia.infrastructure.associate.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociateRepository extends JpaRepository<AssociateJpaEntity, String> {
    boolean existsByDocument(String document);
    Page<AssociateJpaEntity> findAll(Specification<AssociateJpaEntity> whereClause, Pageable page);
}
