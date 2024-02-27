CREATE TABLE Player(
    id SERIAL PRIMARY KEY START WITH 1000,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE
);

CREATE TABLE Game(
    id SERIAL PRIMARY KEY START WITH 100,
    name VARCHAR(50) UNIQUE,
    developer VARCHAR(50),
    genres VARCHAR(20)[]
);

CREATE TABLE Session(
    id SERIAL PRIMARY KEY START WITH 10000,
    capacity INT,
    session_date DATE,
    game_id INT REFERENCES Game(id)
);

CREATE TABLE SessionPlayer(
    session_id INT REFERENCES Session(id),
    player_id INT REFERENCES Player(id),
    PRIMARY KEY (session_id, player_id)
);