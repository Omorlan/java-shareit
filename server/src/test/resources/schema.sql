DROP TABLE IF EXISTS comments, bookings, requests, items, users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGSERIAL PRIMARY KEY,
    description  VARCHAR(1000) NOT NULL,
    requestor_id BIGINT,
    created      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000) NOT NULL,
    owner_id    BIGINT,
    available   BOOLEAN,
    request_id  BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE RESTRICT,
    FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS bookings
(
    id            BIGSERIAL PRIMARY KEY,
    start_booking TIMESTAMP NOT NULL,
    end_booking   TIMESTAMP NOT NULL,
    item_id       BIGINT,
    booker_id     BIGINT,
    status        VARCHAR(50),
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE RESTRICT,
    FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGSERIAL PRIMARY KEY,
    text      VARCHAR(1000) NOT NULL,
    item_id   BIGINT        NOT NULL,
    author_id BIGINT        NOT NULL,
    created   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE RESTRICT,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE RESTRICT
);