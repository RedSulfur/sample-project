DROP TABLE app_user_user_profile;
DROP TABLE app_user;
DROP TABLE user_profile;
DROP TABLE project;
DROP TABLE technology;
DROP TABLE projects_technologies;

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
);

CREATE TABLE technology (
  id BIGSERIAL NOT NULL,
  technology CHARACTER VARYING(45),
  CONSTRAINT technologies_pk PRIMARY KEY (id)
);

INSERT INTO technology (id, technology) VALUES (1, 'Spring Data'); /*1*/
INSERT INTO technology (id, technology) VALUES (2, 'JPA');
INSERT INTO technology (id, technology) VALUES (3, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (4, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (5, 'Maven');

INSERT INTO technology (id, technology) VALUES (6, 'MongoDB'); /*2*/
INSERT INTO technology (id, technology) VALUES (7, 'JPA');
INSERT INTO technology (id, technology) VALUES (8, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (9, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (10, 'Gradle');

INSERT INTO technology (id, technology) VALUES (11, 'HTML'); /*3*/
INSERT INTO technology (id, technology) VALUES (12, 'CSS');
INSERT INTO technology (id, technology) VALUES (13, 'AngularJS');
INSERT INTO technology (id, technology) VALUES (14, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (15, 'Maven');

INSERT INTO technology (id, technology) VALUES (16, 'Spting Batch'); /*4*/
INSERT INTO technology (id, technology) VALUES (17, 'JPA');
INSERT INTO technology (id, technology) VALUES (18, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (19, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (20, 'Ant');

INSERT INTO technology (id, technology) VALUES (21, 'Spring MVC'); /*5*/
INSERT INTO technology (id, technology) VALUES (22, 'JPA');
INSERT INTO technology (id, technology) VALUES (23, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (24, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (25, 'Gradle');

INSERT INTO technology (id, technology) VALUES (26, 'Servlets'); /*6*/
INSERT INTO technology (id, technology) VALUES (27, 'Struts');
INSERT INTO technology (id, technology) VALUES (28, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (29, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (30, 'Maven');

INSERT INTO technology (id, technology) VALUES (31, 'Grails'); /*7*/
INSERT INTO technology (id, technology) VALUES (32, 'JPA');
INSERT INTO technology (id, technology) VALUES (33, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (34, 'Algorithms');
INSERT INTO technology (id, technology) VALUES (35, 'Maven');

INSERT INTO technology (id, technology) VALUES (36, 'Spring Security'); /*8*/
INSERT INTO technology (id, technology) VALUES (37, 'JPA');
INSERT INTO technology (id, technology) VALUES (38, 'Hibernate');
INSERT INTO technology (id, technology) VALUES (39, 'Spring Boot');
INSERT INTO technology (id, technology) VALUES (40, 'Gradle');

INSERT INTO technology (id, technology) VALUES (41, 'Rest'); /*9*/
INSERT INTO technology (id, technology) VALUES (42, 'JPA');
INSERT INTO technology (id, technology) VALUES (43, 'Git');
INSERT INTO technology (id, technology) VALUES (44, 'Linux');
INSERT INTO technology (id, technology) VALUES (45, 'Ant');

INSERT INTO project (id, logo, user_id) VALUES (1, 'img/boot.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (2, 'img/git.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (3, 'img/gradle.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (4, 'img/hibernate.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (5, 'img/maven.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (6, 'img/postgres.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (7, 'img/spring.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (8, 'img/sql.png', 1);
INSERT INTO project (id, logo, user_id) VALUES (9, 'img/thymeleaf.png', 1);

CREATE TABLE projects_technologies (
  project_id bigint,
  technology_id bigint,
  CONSTRAINT project_technologies_project_fk FOREIGN KEY (project_id)
  REFERENCES project(id),
  CONSTRAINT project_technologies_technology_fk FOREIGN KEY (technology_id)
  REFERENCES technology(id)
);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (1, 1);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (1, 2);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (1, 3);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (1, 4);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (1, 5);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (2, 6);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (2, 7);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (2, 8);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (2, 9);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (2, 10);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (3, 11);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (3, 12);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (3, 13);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (3, 14);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (3, 15);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (4, 16);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (4, 17);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (4, 18);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (4, 19);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (4, 20);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (5, 21);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (5, 22);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (5, 23);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (5, 24);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (5, 25);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (6, 26);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (6, 27);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (6, 28);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (6, 29);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (6, 30);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (7, 31);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (7, 32);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (7, 33);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (7, 34);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (7, 35);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (8, 36);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (8, 37);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (8, 38);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (8, 39);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (8, 40);

INSERT INTO projects_technologies (project_id, technology_id) VALUES (9, 41);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (9, 42);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (9, 43);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (9, 44);
INSERT INTO projects_technologies (project_id, technology_id) VALUES (9, 45);