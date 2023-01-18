CREATE TABLE agendas_vote_sessions(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    started_at DATETIME(6)  NOT NULL,
    ended_at   DATETIME(6)  NOT NULL
);

CREATE TABLE agendas_vote_sessions_votes(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    vote       VARCHAR(3)  NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    associate_id CHAR(36) NOT NULL,
    vote_session_id CHAR(36) NOT NULL,
    CONSTRAINT fk_avsv_associate_id FOREIGN KEY (associate_id) REFERENCES associates (id),
    CONSTRAINT fk_avsv_vote_session_id FOREIGN KEY (vote_session_id) REFERENCES agendas_vote_sessions (id)
);

CREATE TABLE agendas(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    deleted_at DATETIME(6),
    vote_session_id CHAR(36) NULL,
    CONSTRAINT fk_a_vote_session_id FOREIGN KEY (vote_session_id) REFERENCES agendas_vote_sessions (id) ON DELETE CASCADE
);