-- DROP TABLE security.app_user_user_profile;
-- DROP TABLE security.app_user;
-- DROP TABLE security.user_profile;

CREATE TABLE germantv.app_user
(
  id bigserial NOT NULL,
  sso_id character varying(30),
  password character varying(100),
  first_name character varying(30),
  last_name character varying(30),
  email character varying(30),
  state character varying(30),
  CONSTRAINT app_user_pk PRIMARY KEY (id)
);

CREATE TABLE germantv.user_profile
(
  id bigserial NOT NULL,
  type character varying(30),
  CONSTRAINT user_profile_pk PRIMARY KEY (id)
);

CREATE TABLE germantv.app_user_user_profile
(
  user_id bigint,
  user_profile_id bigint,
  CONSTRAINT app_user_fk FOREIGN KEY (user_id)
  REFERENCES security.app_user (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_profile_fk FOREIGN KEY (user_profile_id)
  REFERENCES security.user_profile (id) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE
);

--  Populate USER_PROFILE Table

INSERT INTO germantv.user_profile(type)
VALUES ('USER');

INSERT INTO germantv.user_profile(type)
VALUES ('ADMIN');

INSERT INTO germantv.user_profile(type)
VALUES ('DBA');


--  Populate one Admin User which will further create other users for the application using GUI

INSERT INTO germantv.app_user(sso_id, password, first_name, last_name, email, state)
VALUES ('sulfur','$2a$10$rNSLPUnJZZZfu6otudhFB.Uz8IahfsGqRaiQalCwsNghF10N7Dy9q', 'Raymond','Sulfur','sulfur@gmail.com', 'Active');


--  Populate JOIN Table

INSERT INTO germantv.app_user_user_profile (user_id, user_profile_id)
  SELECT app_user.id, profile.id FROM security.app_user app_user, security.user_profile profile
where app_user.sso_id='sulfur' and profile.type='ADMIN';
