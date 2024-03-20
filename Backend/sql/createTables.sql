CREATE TABLE Player(
    pid INT GENERATED ALWAYS AS IDENTITY (START WITH 1000) PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    token VARCHAR(36) UNIQUE,
    password VARCHAR(20)
);

CREATE TABLE Game(
    gid INT GENERATED ALWAYS AS IDENTITY (START WITH 100) PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    developer VARCHAR(50),
    genres VARCHAR(20)[]
);

CREATE TABLE Session(
    sid INT GENERATED ALWAYS AS IDENTITY (START WITH 10000) PRIMARY KEY,
    capacity INT,
    session_date DATE,
    game_id INT REFERENCES Game(gid)
);

CREATE TABLE SessionPlayer(
    session_id INT REFERENCES Session(sid),
    player_id INT REFERENCES Player(pid),
    PRIMARY KEY (session_id, player_id)
);