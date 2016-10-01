DROP TABLE app_user_user_profile;
DROP TABLE user_profile;
DROP TABLE app_user;
DROP TABLE project;
DROP TABLE technology;

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
VALUES ('RedSulfur','$2a$10$tq5k2IeIfFEQcvqUgP2QAOYpF7CsnXnxDJsdwwG1bNs9vL0IrKQ7e', 'John','Doe','sulfur@gmail.com', 'Active');

--  Populate JOIN Table

INSERT INTO app_user_user_profile (user_id, user_profile_id)
SELECT app_user.id, profile.id FROM app_user app_user, user_profile profile
where app_user.sso_id='RedSulfur' and profile.type='ADMIN';

CREATE TABLE project (
  id bigserial NOT NULL,
  logo character varying(255),
  user_id INTEGER,
  CONSTRAINT project_pk PRIMARY KEY (id),
  CONSTRAINT project_fk FOREIGN KEY (user_id)
  REFERENCES app_user(id)
  ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE technology (
  id BIGSERIAL NOT NULL,
  name CHARACTER VARYING(45),
  project_id INTEGER,
  CONSTRAINT technology_pk PRIMARY KEY (id),
  CONSTRAINT technology_fk FOREIGN KEY (project_id)
  REFERENCES project (id)
  ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO project (id, logo, user_id) VALUES (1, 'img/boot.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (2, 'img/git.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (3, 'img/gradle.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (4, 'img/hibernate.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (5, 'img/maven.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (6, 'img/postgres.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (7, 'img/spring.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (8, 'img/sql.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (9, 'img/thymeleaf.png', 1);

INSERT INTO technology (id, name, project_id) VALUES (1, 'Spring Data', 1);
INSERT INTO technology (id, name, project_id) VALUES (2, 'JPA', 1);
INSERT INTO technology (id, name, project_id) VALUES (3, 'Hibernate', 1);
INSERT INTO technology (id, name, project_id) VALUES (4, 'Spring Boot', 1);
INSERT INTO technology (id, name, project_id) VALUES (5, 'Maven', 1);

INSERT INTO technology (id, name, project_id) VALUES (6, 'MongoDB', 2);
INSERT INTO technology (id, name, project_id) VALUES (7, 'JPA', 2);
INSERT INTO technology (id, name, project_id) VALUES (8, 'Hibernate', 2);
INSERT INTO technology (id, name, project_id) VALUES (9, 'Spring Boot', 2);
INSERT INTO technology (id, name, project_id) VALUES (10, 'Gradle', 2);

INSERT INTO technology (id, name, project_id) VALUES (11, 'HTML', 3);
INSERT INTO technology (id, name, project_id) VALUES (12, 'CSS', 3);
INSERT INTO technology (id, name, project_id) VALUES (13, 'AngularJS', 3);
INSERT INTO technology (id, name, project_id) VALUES (14, 'Spring Boot', 3);
INSERT INTO technology (id, name, project_id) VALUES (15, 'Maven', 3);

INSERT INTO technology (id, name, project_id) VALUES (16, 'Spring Batch', 4);
INSERT INTO technology (id, name, project_id) VALUES (17, 'JPA', 4);
INSERT INTO technology (id, name, project_id) VALUES (18, 'Hibernate', 4);
INSERT INTO technology (id, name, project_id) VALUES (19, 'Spring Boot', 4);
INSERT INTO technology (id, name, project_id) VALUES (20, 'Ant', 4);

INSERT INTO technology (id, name, project_id) VALUES (21, 'Spring MVC', 5);
INSERT INTO technology (id, name, project_id) VALUES (22, 'JPA', 5);
INSERT INTO technology (id, name, project_id) VALUES (23, 'Hibernate', 5);
INSERT INTO technology (id, name, project_id) VALUES (24, 'Spring Boot', 5);
INSERT INTO technology (id, name, project_id) VALUES (25, 'Gradle', 5);

INSERT INTO technology (id, name, project_id) VALUES (26, 'Servlets', 6);
INSERT INTO technology (id, name, project_id) VALUES (27, 'Struts', 6);
INSERT INTO technology (id, name, project_id) VALUES (28, 'Hibernate', 6);
INSERT INTO technology (id, name, project_id) VALUES (29, 'Spring Boot', 6);
INSERT INTO technology (id, name, project_id) VALUES (30, 'Maven', 6);

INSERT INTO technology (id, name, project_id) VALUES (31, 'Grails', 7);
INSERT INTO technology (id, name, project_id) VALUES (32, 'JPA', 7);
INSERT INTO technology (id, name, project_id) VALUES (33, 'Hibernate', 7);
INSERT INTO technology (id, name, project_id) VALUES (34, 'Algorithms', 7);
INSERT INTO technology (id, name, project_id) VALUES (35, 'Maven', 7);

INSERT INTO technology (id, name, project_id) VALUES (36, 'Spring Security', 8);
INSERT INTO technology (id, name, project_id) VALUES (37, 'JPA', 8);
INSERT INTO technology (id, name, project_id) VALUES (38, 'Hibernate', 8);
INSERT INTO technology (id, name, project_id) VALUES (39, 'Spring Boot', 8);
INSERT INTO technology (id, name, project_id) VALUES (40, 'Gradle', 8);

INSERT INTO technology (id, name, project_id) VALUES (41, 'Rest', 9);
INSERT INTO technology (id, name, project_id) VALUES (42, 'JPA', 9);
INSERT INTO technology (id, name, project_id) VALUES (43, 'Git', 9);
INSERT INTO technology (id, name, project_id) VALUES (44, 'Linux', 9);
INSERT INTO technology (id, name, project_id) VALUES (45, 'Ant', 9);