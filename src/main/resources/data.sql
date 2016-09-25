DROP TABLE app_user_user_profile;
DROP TABLE app_user;
DROP TABLE user_profile;

CREATE TABLE app_user (
  id bigserial NOT NULL,
  sso_id character varying(30),
  password character varying(100),
  first_name character varying(30),
  last_name character varying(30),
  email character varying(30),
  state character varying(30),
  CONSTRAINT app_user_pk PRIMARY KEY (id)
);

CREATE TABLE user_profile (
  id bigserial NOT NULL,
  type character varying(30),
  CONSTRAINT user_profile_pk PRIMARY KEY (id)
);

CREATE TABLE app_user_user_profile (
  user_id bigint,
  user_profile_id bigint,
  CONSTRAINT app_user_fk FOREIGN KEY (user_id)
  REFERENCES app_user (id)
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_profile_fk FOREIGN KEY (user_profile_id)
  REFERENCES user_profile (id)
  ON UPDATE CASCADE ON DELETE CASCADE
);

--  Populate USER_PROFILE Table

INSERT INTO user_profile(type)
VALUES ('USER');

INSERT INTO user_profile(type)
VALUES ('ADMIN');

INSERT INTO user_profile(type)
VALUES ('DBA');

--  Populate one Admin User which will further create other users for the application using GUI

INSERT INTO app_user(sso_id, password, first_name, last_name, email, state)
VALUES ('RedSulfur','$2a$10$Fb7/daJELSrdgJ4gkNL9Gu1KPS.2aYrqLXN38tNXzFV8mfHeA80mi', 'John','Doe','sulfur@gmail.com', 'Active');

--  Populate JOIN Table

INSERT INTO app_user_user_profile (user_id, user_profile_id)
SELECT app_user.id, profile.id FROM app_user app_user, user_profile profile
where app_user.sso_id='RedSulfur' and profile.type='ADMIN';