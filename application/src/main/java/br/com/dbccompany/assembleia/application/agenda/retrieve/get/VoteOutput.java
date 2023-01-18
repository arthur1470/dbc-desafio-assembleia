package br.com.dbccompany.assembleia.application.agenda.retrieve.get;

public record VoteOutput(
        int totalVotes,
        long yes,
        long no,
        Double yesPercentage,
        Double noPercentage
) {
    public static VoteOutput with(
            int totalVotes,
            long yes,
            long no
    ) {
        final Double yesPercentage = (double) (yes * 100) / totalVotes;
        final Double noPercentage = (double) (no * 100) / totalVotes;

        return new VoteOutput(
                totalVotes,
                yes,
                no,
                yesPercentage,
                noPercentage
        );

    }
}
