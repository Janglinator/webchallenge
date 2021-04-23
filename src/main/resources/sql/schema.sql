--DROP TABLE pokemon IF EXISTS;
--DROP TABLE user IF EXISTS;

CREATE TABLE IF NOT EXISTS pokemon (
    id              VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    pokemon_id      VARCHAR(255),
    name            VARCHAR(255),
    types           VARCHAR(255),
    height          DOUBLE,
    weight          DOUBLE,
    abilities       VARCHAR(255),
    egg_groups      VARCHAR(255),
    stats           VARCHAR(255),
    genus           VARCHAR(255),
    description     VARCHAR(255),
    captured        BIT
);

CREATE TABLE IF NOT EXISTS user (
    id              VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    username        VARCHAR(255),
    password   VARCHAR(255)
);