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


