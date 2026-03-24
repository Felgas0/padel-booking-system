DROP TABLE IF EXISTS rentals CASCADE;
DROP TABLE IF EXISTS courts CASCADE;
DROP TABLE IF EXISTS clubs CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(60)         NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL CHECK ( email ~ '^[A-Za-z0-9+_.-]+@(.+)$' ),
    token UUID UNIQUE         NOT NULL,
    password_hash TEXT        NOT NULL
);



CREATE TABLE clubs
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(60) NOT NULL,
    owner_id INT         NOT NULL REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE courts
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(60) NOT NULL,
    club_id INT         NOT NULL REFERENCES clubs (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE rentals
(
    id       SERIAL PRIMARY KEY,
    date     TIMESTAMP NOT NULL,
    duration INT       NOT NULL CHECK (duration > 0),
    user_id  INT       NOT NULL REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    court_id INT       NOT NULL REFERENCES courts (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE clubs_id_seq RESTART WITH 1;
ALTER SEQUENCE courts_id_seq RESTART WITH 1;
ALTER SEQUENCE rentals_id_seq RESTART WITH 1;
