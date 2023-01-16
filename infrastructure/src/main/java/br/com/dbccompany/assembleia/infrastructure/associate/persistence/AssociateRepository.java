package br.com.dbccompany.assembleia.infrastructure.associate.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociateRepository extends JpaRepository<AssociateJpaEntity, String> {
    boolean existsByDocument(String document);
}
