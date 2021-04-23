--DROP TABLE pokemon IF EXISTS;

CREATE TABLE IF NOT EXISTS pokemon (
    id              VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    pokedex_id      VARCHAR(255),
    name            VARCHAR(255),
    types           VARCHAR(255),
    height          DOUBLE,
    weight          DOUBLE,
    abilities       VARCHAR(255),
    egg_groups      VARCHAR(255),
    stats           VARCHAR(255),
    genus           VARCHAR(255),
    description     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user (
    id              VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    username        VARCHAR(255),
    password        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_to_pokemon (
    id              VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id         VARCHAR(255),
    pokedex_id      VARCHAR(255)
);