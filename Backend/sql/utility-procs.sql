CREATE OR REPLACE PROCEDURE createTables()
LANGUAGE PLPGSQL
AS $$
begin
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

	create trigger before_insert_session_player
	before insert on SessionPlayer
	for each row
	execute function check_session_capacity();

	create trigger after_delete_session_player
    after delete on SessionPlayer
    for each row
    execute function check_player_count();

    create trigger after_update_session
    after update on Session
    for each row
    execute function check_session_capacity_update();

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
		('Lord of the Rings Online', 'Dev123', ARRAY['RPG', 'Adventure']),
		('Apex Legends', 'Rampage', ARRAY['Shooter', 'Action']),
		('Civilization VI', 'DevABC', ARRAY['Turn-Based', 'Strategy']);

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