DROP TABLE germantv.film;

CREATE TABLE germantv.film (
  id          BIGSERIAL NOT NULL,
  title       VARCHAR(30),
  stars       DOUBLE PRECISION,
  description VARCHAR(255),
  image       CHARACTER VARYING(255),
  CONSTRAINT film_id_pk PRIMARY KEY (id)
);

INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Slipknot', 9, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/corey.jpg');
INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Linkin Park', 8.5, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/chester.jpg');
INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Avenged Sevenfold', 9, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/matt.jpg');
INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Rammstein', 8.5, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/till.jpg');
INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Slipknot', 9.6, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/corey.jpg');
INSERT INTO germantv.film (title, stars, description, image)
VALUES ('Slipknot', 9.7, 'SOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERESOME INFO HERE',
        'img/corey.jpg');


