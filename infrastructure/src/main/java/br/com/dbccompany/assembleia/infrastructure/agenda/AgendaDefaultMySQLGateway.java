package br.com.dbccompany.assembleia.infrastructure.agenda;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.persistence.VoteJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.vote.persistence.VoteRepository;
import br.com.dbccompany.assembleia.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static br.com.dbccompany.assembleia.infrastructure.utils.SpecificationUtils.like;

@Component
public class AgendaDefaultMySQLGateway implements AgendaGateway {
    private final AgendaRepository agendaRepository;
    private final VoteRepository voteRepository;

    public AgendaDefaultMySQLGateway(final AgendaRepository agendaRepository, final VoteRepository voteRepository) {
        this.agendaRepository = Objects.requireNonNull(agendaRepository);
        this.voteRepository = Objects.requireNonNull(voteRepository);
    }

    @Override
    public Agenda create(final Agenda anAgenda) {
        return agendaRepository.save(AgendaJpaEntity.from(anAgenda))
                .toAggregate();
    }

    @Override
    public Optional<Agenda> findById(final AgendaID anId) {
        return this.agendaRepository.findById(anId.getValue())
                .map(AgendaJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Agenda> findAll(final AgendaSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(term ->
                        SpecificationUtils.<AgendaJpaEntity>like("name", term)
                                .or(like("description", term))
                )
                .orElse(null);

        final var pageResult =
                this.agendaRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(AgendaJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public Pagination<Vote> findAll(final VoteSessionID anId, final AgendaVotesSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<VoteJpaEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("voteSession").get("id"), anId.getValue());

        final var pageResult =
                this.voteRepository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(VoteJpaEntity::toDomain).toList()
        );
    }

    @Override
    public boolean existsByAssociateAndVoteSession(
            final AssociateID anAssociateId,
            final VoteSessionID aVoteSessionId
    ) {
        return this.voteRepository.existsByAssociateIdAndVoteSessionId(anAssociateId.getValue(), aVoteSessionId.getValue());
    }
}
