package br.com.dbccompany.assembleia.infrastructure.agenda;

import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.AgendaSearchQuery;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
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

    public AgendaDefaultMySQLGateway(final AgendaRepository agendaRepository) {
        this.agendaRepository = Objects.requireNonNull(agendaRepository);
    }

    @Override
    public Agenda create(final Agenda anAgenda) {
        return this.agendaRepository.save(AgendaJpaEntity.from(anAgenda))
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
}
