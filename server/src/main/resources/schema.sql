CREATE TABLE IF NOT EXISTS users (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 name VARCHAR(255) NOT NULL,
 email VARCHAR(512) NOT NULL UNIQUE,
 activation BOOLEAN DEFAULT FALSE,
 CONSTRAINT pk_user PRIMARY KEY (id),
 CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT UQ_NAME UNIQUE (name)
);

create type event_state as enum ('PENDING', 'PUBLISHED', 'CANCELED');

CREATE TABLE IF NOT EXISTS events (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 title VARCHAR(50) NOT NULL,
 annotation VARCHAR(255) NOT NULL,
 category_id BIGINT NOT NULL,
 confirmed_requests BIGINT,
 created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 description VARCHAR(4000),
 event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 initiator_id BIGINT NOT NULL,
 paid BOOLEAN,
 participant_limit INT,
 published_on TIMESTAMP WITHOUT TIME ZONE,
 request_moderation BOOLEAN,
 state event_state,
 views BIGINT,
 CONSTRAINT pk_event PRIMARY KEY (id),
 CONSTRAINT FK_EVENT_ON_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id),
 CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE CAST (CHARACTER VARYING as event_state) WITH INOUT AS IMPLICIT;

CREATE TYPE request_status AS ENUM ('WAITING', 'ACCEPTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  event_id BIGINT NOT NULL,
  requester_id BIGINT NOT NULL,
  status request_status,
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT FK_REQUEST_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id),
  CONSTRAINT FK_REQUEST_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE CAST (CHARACTER VARYING as request_status) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    events BIGINT[],
    title VARCHAR(255) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_in_compilations (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_events_in_compilations PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT FK_events_in_compilations_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT FK_events_in_compilations_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);