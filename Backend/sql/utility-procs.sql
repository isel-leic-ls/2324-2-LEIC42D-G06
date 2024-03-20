CREATE OR REPLACE PROCEDURE createTables()
LANGUAGE PLPGSQL
AS $$
begin

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
    session_date VARCHAR(19),
    closed BOOLEAN,
    game_id INT REFERENCES Game(gid)
);

CREATE TABLE SessionPlayer(
    session_id INT REFERENCES Session(sid),
    player_id INT REFERENCES Player(pid),
    PRIMARY KEY (session_id, player_id)
);

end;
$$;

CREATE OR REPLACE PROCEDURE populateTables()
LANGUAGE PLPGSQL
AS $$
begin


-- Insert into Player table
INSERT INTO Player(name, email, token, password) VALUES
	('Pedro', 'pedro@hotmail.com', '3ad7db4b-c5a9-42ee-9094-852f94c57cb7', 'p3dr0'),
	('Vasco', 'vasco@gmail.com', '3ad7db4b-c5a9-42ee-9094-852f94c57cb8', 'v4sc0'),
	('Jonny', 'jonny@live.com.pt', '3ad7db4b-c5a9-42ee-9094-852f94c57cb9', 'j0nny');


-- Insert into Game table
INSERT INTO Game(name, developer, genres) VALUES 
	('Lord of the Rings Online', NULL, ARRAY['RPG', 'Adventure']),
	('Apex Legends', NULL, ARRAY['Shooter', 'Action']),
	('Civilization VI', NULL, ARRAY['Turn-Based', 'Strategy']);


-- Insert into Session table
INSERT INTO Session(capacity, session_date, closed, game_id) VALUES
	(3, '2024-02-25 18:00:00', true, 100),
	(3, '2024-02-26 18:00:00', true, 101),
	(2, '202402--27 20:00:00', true, 102);


-- Insert into SessionPlayer table
INSERT INTO SessionPlayer(session_id, player_id) VALUES 
	(10000, 1000),
	(10000, 1001),
	(10000, 1002),
	(10001, 1000),
	(10001, 1001),
	(10001, 1002),
	(10002, 1000),
	(10002, 1002);


end;
$$;

CREATE OR REPLACE PROCEDURE removeTables()
LANGUAGE PLPGSQL
AS $$
begin

DROP TABLE IF EXISTS SessionPlayer;
DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS Game;
DROP TABLE IF EXISTS Player;

end;
$$;



