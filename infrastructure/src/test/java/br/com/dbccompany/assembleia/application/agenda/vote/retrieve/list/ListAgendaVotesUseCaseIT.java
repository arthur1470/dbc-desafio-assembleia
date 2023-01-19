package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.IntegrationTest;
import br.com.dbccompany.assembleia.domain.agenda.Agenda;
import br.com.dbccompany.assembleia.domain.agenda.AgendaID;
import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.Associate;
import br.com.dbccompany.assembleia.domain.utils.InstantUtils;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateJpaEntity;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class ListAgendaVotesUseCaseIT {

    @Autowired
    private ListAgendaVotesUseCase useCase;

    @Autowired
    private AssociateRepository associateRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @BeforeEach
    void mockup() {
        final var associateJoao = Associate.newAssociate("Joao", "12345667891", true);
        final var associatePedro = Associate.newAssociate("Pedro", "12345667892", true);
        final var associateMaria = Associate.newAssociate("Maria", "12345667893", true);

        final var associates = Stream.of(associateJoao, associatePedro, associateMaria)
                .map(AssociateJpaEntity::from)
                .toList();

        associateRepository.saveAllAndFlush(associates);

        final var now = InstantUtils.now();

        final var agendaSalarios = Agenda.with(
                        AgendaID.from("1"),
                        "Pauta salarios",
                        "Pauta de salarios",
                        true,
                        now,
                        now,
                        null,
                        null
                )
                .startVoteSession(InstantUtils.now().plusSeconds(20))
                .addVote(Vote.newVote(VoteType.YES, associateJoao.getId()))
                .addVote(Vote.newVote(VoteType.YES, associatePedro.getId()))
                .addVote(Vote.newVote(VoteType.YES, associateMaria.getId()));

        final var agendaSaude = Agenda.with(
                        AgendaID.from("2"),
                        "Pauta saude",
                        "Pauta de saude",
                        true,
                        now,
                        now,
                        null,
                        null
                )
                .startVoteSession(InstantUtils.now().plusSeconds(20));

        final var agendas = Stream.of(agendaSalarios, agendaSaude)
                .map(AgendaJpaEntity::from)
                .toList();

        agendaRepository.saveAllAndFlush(agendas);
    }

    @Test
    void givenAValidSearchQuery_whenAgendaHasVotes_shouldReturnVotesPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 3;
        final var expectedTotal = 3;

        final var aSessionId = VoteSessionID.from(
                agendaRepository.findById("1").get().getVoteSession().getId()
        );

        final var aQuery =
                new AgendaVotesSearchQuery(expectedPage, expectedPerPage, expectedSort, expectedDirection);

        final var aCommand = ListAgendaVotesCommand.with(
                aSessionId,
                aQuery
        );

        final var actualResult = useCase.execute(aCommand);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @Test
    void givenAValidSearchQuery_whenAgendaHasNoVotes_shouldReturnEmptyVotesPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aSessionId = VoteSessionID.from(
                agendaRepository.findById("2").get().getVoteSession().getId()
        );

        final var aQuery =
                new AgendaVotesSearchQuery(expectedPage, expectedPerPage, expectedSort, expectedDirection);

        final var aCommand = ListAgendaVotesCommand.with(
                aSessionId,
                aQuery
        );

        final var actualResult = useCase.execute(aCommand);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }
}
