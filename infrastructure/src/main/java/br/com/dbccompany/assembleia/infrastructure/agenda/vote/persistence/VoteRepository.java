package br.com.dbccompany.assembleia.infrastructure.agenda.vote.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<VoteJpaEntity, String> {
    Page<VoteJpaEntity> findAll(Specification<VoteJpaEntity> whereClause, Pageable page);
    boolean existsByAssociateIdAndVoteSessionId(String associateId, String voteSessionId);
}
