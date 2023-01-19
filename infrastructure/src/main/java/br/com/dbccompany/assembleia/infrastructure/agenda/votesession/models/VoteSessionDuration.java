package br.com.dbccompany.assembleia.infrastructure.agenda.votesession.models;

import br.com.dbccompany.assembleia.domain.utils.InstantUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

public enum VoteSessionDuration {
    SECONDS() {
        @Override
        public Instant getEndDate(final Integer duration) {
            if(duration == null || duration < 0) return null;
            return InstantUtils.now().plus(Duration.ofSeconds(duration));
        }
    },
    MINUTES() {
        @Override
        public Instant getEndDate(final Integer duration) {
            if(duration == null || duration < 0) return null;
            return InstantUtils.now().plus(Duration.ofMinutes(duration));
        }
    },
    HOURS() {
        @Override
        public Instant getEndDate(final Integer duration) {
            if(duration == null || duration < 0) return null;
            return InstantUtils.now().plus(Duration.ofHours(duration));
        }
    };

    public abstract Instant getEndDate(final Integer duration);

    public static Optional<VoteSessionDuration> of(final String value) {
        return Arrays.stream(VoteSessionDuration.values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
