package br.com.dbccompany.assembleia.infrastructure.agenda.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<AgendaJpaEntity, String> {
    Page<AgendaJpaEntity> findAll(Specification<AgendaJpaEntity> whereClause, Pageable page);
}
