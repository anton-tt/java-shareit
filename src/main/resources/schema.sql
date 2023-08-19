DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS comments;

CREATE TABLE IF NOT EXISTS users (
  id    BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name  VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
  id           BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name         VARCHAR(255) NOT NULL,
  description  VARCHAR(512) NOT NULL,
  is_available BOOLEAN      NOT NULL,
  owner_id     BIGINT       NOT NULL REFERENCES users (id)   ON DELETE CASCADE ON UPDATE CASCADE,
  request_id   BIGINT       NOT NULL REFERENCES request (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
  id         BIGINT                      NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  status     VARCHAR(20)                 NOT NULL,
  item_id    BIGINT                      NOT NULL REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
  booker_id  BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS requests (
  id           BIGINT                      NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  description  VARCHAR(512)                NOT NULL,
  created_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  requestor_id BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
  id           BIGINT                      NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  text         VARCHAR(512)                NOT NULL,
  created_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id      BIGINT                      NOT NULL REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
  author_id    BIGINT                      NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);