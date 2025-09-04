DROP SCHEMA IF EXISTS query_runner CASCADE;

CREATE SCHEMA query_runner;

DROP TABLE IF EXISTS query_runner.query_queue;

CREATE TABLE query_runner.query_queue (
  id UUID PRIMARY KEY,        -- Unique run identifier
  query_iri VARCHAR(255) NOT NULL,
  query_name VARCHAR(255) NOT NULL,
  query_request JSONB NOT NULL,
  user_id UUID NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  queued_at TIMESTAMP NOT NULL,
  started_at TIMESTAMP,
  pid INT,                    -- Internal (postgres) process ID (for killing)
  finished_at TIMESTAMP,
  killed_at TIMESTAMP,
  status TEXT,
  error TEXT
);


DROP TABLE IF EXISTS query_runner.query_result;

CREATE TABLE query_runner.query_result (
  iri VARCHAR(255) NOT NULL,
  id UUID NOT NULL
);

CREATE INDEX idx_query_result_iri ON query_runner.query_result(iri);
