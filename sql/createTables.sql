CREATE TABLE Player (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE
);

CREATE TABLE Game (
    id INT PRIMARY KEY,
    name VARCHAR(40) UNIQUE,
    genres VARCHAR(20)[]
);

CREATE TABLE Session (
    id INT PRIMARY KEY,
    number_of_players INT,
    session_date DATE,
    game_id INT REFERENCES Game(game_id)
);

CREATE TABLE Player_Session(
    session_id INT REFERENCES Session(session_id),
    player_id INT REFERENCES Player(player_id),
    PRIMARY KEY (session_id, player_id)
);