CREATE TABLE Player (
    pid         INT GENERATED ALWAYS AS IDENTITY (START WITH 1000) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    email       VARCHAR(50) NOT NULL CHECK (email SIMILAR to '_%@_%') UNIQUE,
    token       VARCHAR(36) NOT NULL UNIQUE,
    password    VARCHAR(20) NOT NULL
);

CREATE TABLE Game (
    gid         INT GENERATED ALWAYS AS IDENTITY (START WITH 100) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    developer   VARCHAR(50) NOT NULL,
    genres      VARCHAR(20)[] NOT NULL
);

CREATE TABLE Session (
    sid         INT GENERATED ALWAYS AS IDENTITY (START WITH 10000) PRIMARY KEY,
    capacity    INT NOT NULL CHECK (capacity > 1),
    session_date VARCHAR(19) NOT NULL /*CHECK (session_date SIMILAR TO '^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$')*/,
    closed      BOOLEAN NOT NULL,
    game_id     INT REFERENCES Game (gid)
);

CREATE TABLE SessionPlayer (
    session_id  INT REFERENCES Session (sid),
    player_id   INT REFERENCES Player (pid),
    PRIMARY KEY (session_id, player_id)
);