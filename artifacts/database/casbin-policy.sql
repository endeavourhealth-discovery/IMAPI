CREATE TABLE IF NOT EXISTS casbin_rule (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ptype VARCHAR(256) NOT NULL,
  v0 VARCHAR(256),
  v1 VARCHAR(256),
  v2 VARCHAR(256),
  v3 VARCHAR(256),
  v4 VARCHAR(256),
  v5 VARCHAR(256)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO casbin_rule (ptype, v0, v1, v2)
VALUES
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles','*','*'),
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles','POLICY','WRITE'),
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles ','CASDOOR_USER','READ'),
  ('p', 'r.sub != null && ''EDITOR'' in r.sub.roles', 'ENTITY', 'WRITE'),
  ('p','r.sub != null', 'ENTITY', 'READ'),
  ('p', 'r.sub != null && ''CREATOR'' in r.sub.roles', 'ENTITY', 'WRITE'),
  ('p','r.sub != null && ''CREATOR'' in r.sub.roles', 'DOCUMENT', 'WRITE'),
  ('p','r.sub != null && ''CREATOR'' in r.sub.roles', 'FOLDER', 'WRITE'),
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles', 'DELTA', 'READ'),
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles', 'GITHUB', 'WRITE'),
  ('p','r.sub != null && ''EXECUTOR'' in r.sub.roles','QUERY','EXECUTE'),
  ('p', 'r.sub != null && ''PUBLISHER'' in r.sub.roles','SET','PUBLISH'),
  ('p','r.sub != null && ''EDITOR'' in r.sub.roles','SET','WRITE'),
  ('p','r.sub != null && ''ADMIN'' in r.sub.roles','USER','WRITE'),
  ('p','r.sub != null && ''CREATOR'' in r.sub.roles','BUG_REPORT','WRITE'),
  ('p','r.sub != null && ''EDITOR'' in r.sub.roles','BUG_REPORT','WRITE'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','TASK','READ'),
  ('p','r.sub != null && ''DEVELOPER'' in r.sub.roles','TASK','READ'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','TASK','DELETE'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','ROLE_REQUEST','READ'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','ROLE_REQUEST','WRITE'),
  ('p','r.sub != null && ''APPROVER'' in r.sub.roles','ROLE_REQUEST','APPROVE'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','GRAPH_REQUEST','TASK_MANAGER','READ'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','GRAPH_REQUEST','WRITE'),
  ('p','r.sub != null && ''APPROVER'' in r.sub.roles','GRAPH_REQUEST','APPROVE'),
  ('p','r.sub != null && ''CREATOR'' in r.sub.roles','ENTITY_APPROVAL','WRITE'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','ENTITY_APPROVAL','READ'),
  ('p','r.sub != null && ''TASK_MANAGER'' in r.sub.roles','ENTITY_APPROVAL','WRITE'),
  ('p','r.sub != null && ''APPROVER'' in r.sub.roles','ENTITY_APPROVAL','APPROVE')
;

INSERT INTO casbin_rule (ptype, v0, v1, v2)
VALUES
  ('p','r.sub != null && ''EXECUTOR'' in r.sub.roles','QUERY','EXECUTE'),
  ('p','r.sub != null && ''EXECUTOR'' in r.sub.roles','QUERY_RESULTS','READ')



