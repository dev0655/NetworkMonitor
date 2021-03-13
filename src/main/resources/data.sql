DROP TABLE IF EXISTS hosts;

CREATE TABLE hosts (
  HOST_NAME VARCHAR(250) PRIMARY KEY,
  USER_NAME VARCHAR(250) NOT NULL,
  PASSWORD VARCHAR(250) NOT NULL,
  STATUS VARCHAR(250) NOT NULL
);

INSERT INTO hosts (HOST_NAME, USER_NAME, PASSWORD, STATUS) VALUES
  ('10.0.0.22', 'dev', 'demon123', false),
  ('10.0.0.253', 'dev', 'demon123', false),
  ('10.0.0.15', 'dev', 'demon123', false);
  