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
VALUES ('sulfur','$2a$10$rNSLPUnJZZZfu6otudhFB.Uz8IahfsGqRaiQalCwsNghF10N7Dy9q', 'Raymond','Sulfur','sulfur@gmail.com', 'Active');

--  Populate JOIN Table

INSERT INTO app_user_user_profile (user_id, user_profile_id)
SELECT app_user.id, profile.id FROM app_user app_user, user_profile profile
where app_user.sso_id='sulfur' and profile.type='ADMIN';

DROP TABLE film;

CREATE TABLE film (
  id          BIGSERIAL NOT NULL,
  title       VARCHAR(30),
  stars       DOUBLE PRECISION,
  description VARCHAR(255),
  image       CHARACTER VARYING(255),
  CONSTRAINT film_id_pk PRIMARY KEY (id)
);

INSERT INTO film (title, stars, description, image)
VALUES ('Dr. House', 9, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/house.jpg');
INSERT INTO film (title, stars, description, image)
VALUES ('Fringe', 8.5, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/fringe.jpg');
INSERT INTO film (title, stars, description, image)
VALUES ('Breaking Bad', 9, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/breakingbad.jpg');
INSERT INTO film (title, stars, description, image)
VALUES ('Sherlock', 8.5, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/sher.jpg');
INSERT INTO film (title, stars, description, image)
VALUES ('Game of Thrones', 9.6, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/thrones.jpg');
INSERT INTO film (title, stars, description, image)
VALUES ('Dexter', 9.7, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/dexter.jpg');