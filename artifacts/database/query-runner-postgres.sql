DROP SCHEMA IF EXISTS query_runner CASCADE;

CREATE SCHEMA query_runner;

DROP TABLE IF EXISTS query_queue;

CREATE TABLE query_queue (
  id UUID PRIMARY KEY,        -- Unique run identifier
  iri VARCHAR(255) NOT NULL,  -- Query IRI
  name VARCHAR(255) NOT NULL, -- Query name
  user_id UUID NOT NULL,         -- User UUID
  user_name VARCHAR(255) NOT NULL,
  queued TIMESTAMP NOT NULL,
  started TIMESTAMP,
  pid INT,                    -- Internal (postgres) process ID (for killing)
  finished TIMESTAMP,
  killed TIMESTAMP,
  status TEXT
);


DROP TABLE IF EXISTS query_result;

CREATE TABLE query_result (
  iri VARCHAR(255) NOT NULL,
  id UUID NOT NULL,
);

CREATE INDEX idx_query_result_iri ON query_result(iri);