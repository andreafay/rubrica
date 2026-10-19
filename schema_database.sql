CREATE DATABASE IF NOT EXISTS rubrica
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE rubrica;

CREATE TABLE IF NOT EXISTS persone (
    id        INT          NOT NULL AUTO_INCREMENT,
    nome      VARCHAR(100) NOT NULL,
    cognome   VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(255) NOT NULL DEFAULT '',
    telefono  VARCHAR(30)  NOT NULL DEFAULT '',
    eta       INT          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS utente (
    id       INT         NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password CHAR(64)    NOT NULL,
    PRIMARY KEY (id)
);

INSERT IGNORE INTO utente (username, password) VALUES ('admin', SHA2('admin', 256));

--INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES
--     ('Steve', 'Jobs',   'via Cupertino 13',  '0612344',  56),
--     ('Bill',  'Gates',  'via Redmond 10',    '06688989', 60),
--     ('Babbo', 'Natale', 'via del Polo Nord', '00000111', 99);
