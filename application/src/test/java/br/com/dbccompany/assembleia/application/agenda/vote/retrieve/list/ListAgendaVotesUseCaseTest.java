package br.com.dbccompany.assembleia.application.agenda.vote.retrieve.list;

import br.com.dbccompany.assembleia.domain.agenda.AgendaGateway;
import br.com.dbccompany.assembleia.domain.agenda.vote.AgendaVotesSearchQuery;
import br.com.dbccompany.assembleia.domain.agenda.vote.Vote;
import br.com.dbccompany.assembleia.domain.agenda.vote.VoteType;
import br.com.dbccompany.assembleia.domain.agenda.votesession.VoteSessionID;
import br.com.dbccompany.assembleia.domain.associate.AssociateID;
import br.com.dbccompany.assembleia.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ListAgendaVotesUseCaseTest {

    @InjectMocks
    private DefaultListAgendaVotesUseCase useCase;
    @Mock
    private AgendaGateway agendaGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(agendaGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListVotes_thenShouldReturnVotes() {
        final var votes = of(
                Vote.newVote(VoteType.YES, AssociateID.from("123")),
                Vote.newVote(VoteType.YES, AssociateID.from("122"))
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaVotesSearchQuery(expectedPage, expectedPerPage, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, votes.size(), votes);
        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(AgendaVotesListOutput::from);

        final var aCommand = ListAgendaVotesCommand.with(
                VoteSessionID.from("123"),
                aQuery
        );

        Mockito.when(agendaGateway.findAll(any(), any()))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aCommand);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(votes.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenCallsListVotesAndHasNoResults_thenShouldReturnEmptyVotes() {
        final var anId = VoteSessionID.from("123");
        final var votes = List.<Vote>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new AgendaVotesSearchQuery(expectedPage, expectedPerPage, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, votes.size(), votes);

        final var expectedItemsCount = 0;

        final var expectedResult = expectedPagination.map(AgendaVotesListOutput::from);

        final var aCommand = ListAgendaVotesCommand.with(
                VoteSessionID.from("123"),
                aQuery
        );

        Mockito.when(agendaGateway.findAll(anId, aQuery))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aCommand);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(votes.size(), actualResult.total());
    }
}