MERGE INTO mpa KEY (mpa_id)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

MERGE INTO genre KEY (genre_id)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Ужасы'),
       (5, 'Триллер'),
       (6, 'Детектив');