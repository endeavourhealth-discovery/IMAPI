DROP TABLE IF EXISTS casbin_imapi;
DROP TABLE IF EXISTS casbin_query_runner;

CREATE TABLE IF NOT EXISTS casbin_imapi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ptype VARCHAR(256) NOT NULL,
  v0 VARCHAR(256),
  v1 VARCHAR(256),
  v2 VARCHAR(256),
  v3 VARCHAR(256),
  v4 VARCHAR(256),
  v5 VARCHAR(256)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS casbin_query_runner (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ptype VARCHAR(256) NOT NULL,
  v0 VARCHAR(256),
  v1 VARCHAR(256),
  v2 VARCHAR(256),
  v3 VARCHAR(256),
  v4 VARCHAR(256),
  v5 VARCHAR(256)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# IMAPI API guards
INSERT INTO casbin_imapi (ptype, v0, v1, v2, v3)
VALUES
  ('p','ADMIN','*','*','allow'),
  ('p','ADMIN','POLICY','WRITE','allow'),
  ('p','ADMIN','CASDOOR_USER','READ','allow'),
  ('p','EDITOR', 'ENTITY', 'WRITE','allow'),
  ('p','CREATOR', 'ENTITY', 'WRITE','allow'),
  ('p','CREATOR', 'DOCUMENT', 'WRITE','allow'),
  ('p','CREATOR', 'FOLDER', 'WRITE','allow'),
  ('p','ADMIN', 'DELTA', 'READ','allow'),
  ('p','ADMIN', 'GITHUB', 'WRITE','allow'),
  ('p','EXECUTOR','QUERY','EXECUTE','allow'),
  ('p','PUBLISHER','SET','PUBLISH','allow'),
  ('p','EDITOR','SET','WRITE','allow'),
  ('p','ADMIN','USER','WRITE','allow'),
  ('p','CREATOR','BUG_REPORT','WRITE','allow'),
  ('p','EDITOR','BUG_REPORT','WRITE','allow'),
  ('p','TASK_MANAGER','TASK','READ','allow'),
  ('p','DEVELOPER','TASK','READ','allow'),
  ('p','TASK_MANAGER','TASK','DELETE','allow'),
  ('p','TASK_MANAGER','ROLE_REQUEST','READ','allow'),
  ('p','TASK_MANAGER','ROLE_REQUEST','WRITE','allow'),
  ('p','APPROVER','ROLE_REQUEST','APPROVE','allow'),
  ('p','TASK_MANAGER','GRAPH_REQUEST','READ','allow'),
  ('p','TASK_MANAGER','GRAPH_REQUEST','WRITE','allow'),
  ('p','APPROVER','GRAPH_REQUEST','APPROVE','allow'),
  ('p','CREATOR','ENTITY_APPROVAL','WRITE','allow'),
  ('p','TASK_MANAGER','ENTITY_APPROVAL','READ','allow'),
  ('p','TASK_MANAGER','ENTITY_APPROVAL','WRITE','allow'),
  ('p','APPROVER','ENTITY_APPROVAL','APPROVE','allow')
;

# IMQuery API guards
INSERT INTO casbin_query_runner (ptype, v0, v1, v2, v3)
VALUES
  ('p','EXECUTOR','QUERY','EXECUTE','allow'),
  ('p','EXECUTOR','QUERY_RESULTS','READ','allow')
;

# IMDirectory page guards
INSERT INTO casbin_imapi (ptype,v0, v1, v2, v3)
VALUES
  ('p','ADMIN','PAGE_ADMIN','READ','allow'),
  ('p','CREATOR', 'PAGE_CREATOR','READ','allow'),
  ('p','EDITOR','PAGE_EDITOR','READ','allow')
;