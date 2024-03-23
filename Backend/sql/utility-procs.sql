CREATE OR REPLACE PROCEDURE createTables()
LANGUAGE PLPGSQL
AS $$
begin

CREATE TABLE Player(
    pid INT GENERATED ALWAYS AS IDENTITY (START WITH 1000) PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    email VARCHAR(50) CHECK (email SIMILAR to '_%@_%') UNIQUE,
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
    session_date VARCHAR(19) CHECK (session_date SIMILAR TO '^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$'),
    closed BOOLEAN,
    game_id INT REFERENCES Game(gid)
);

CREATE TABLE SessionPlayer(
    session_id INT REFERENCES Session(sid),
    player_id INT REFERENCES Player(pid),
    PRIMARY KEY (session_id, player_id)
);


create or replace function check_session_capacity()
returns trigger
language plpgsql
as $$

declare
    current_capacity INT;
    max_capacity INT;

begin

    select count(sp.*), max(s.capacity)
    into current_capacity, max_capacity
    from Session s
    left join SessionPlayer sp on s.sid = sp.session_id
    where s.sid = NEW.session_id;

    if exists(select 1 from Session where sid = NEW.session_id and closed = true) then
        raise exception 'Session is already closed';
    end if;

    current_capacity := current_capacity + 1;
    if current_capacity = max_capacity then
        update Session set closed = true where sid = NEW.session_id;
    end if;

    return NEW;
end;
$$;

create trigger before_insert_session_player
before insert on SessionPlayer
for each row
execute function check_session_capacity();


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




