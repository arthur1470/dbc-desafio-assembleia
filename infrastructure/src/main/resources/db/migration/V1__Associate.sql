CREATE TABLE associate
(
    id         VARCHAR(36)  NOT NULL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    document   VARCHAR(11)  NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,
    deleted_at DATETIME(6)
);