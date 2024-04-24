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


-- Insert into Session table
INSERT INTO Session(capacity, session_date, closed, game_id) VALUES
                                                                 (3, '2025-02-25 18:00:00', true, 100),
                                                                 (3, '2025-02-26 18:00:00', true, 101),
                                                                 (2, '2025-02-27 20:00:00', false, 102);



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


create or replace function check_player_count()
    returns trigger
    language plpgsql
as $$

declare
    current_count INT;
    closed_flag BOOLEAN;
begin
    select count(sp.*)
    into current_count
    from SessionPlayer sp
    where sp.session_id = OLD.session_id;

    select closed into closed_flag from Session where sid = OLD.session_id;

    if current_count = 0 then
        delete from Session where sid = OLD.session_id;
    elsif closed_flag = true then
        update Session set closed = false where sid = OLD.session_id;
    end if;

    return null;
end;
$$;

create trigger after_delete_session_player
    after delete on SessionPlayer
    for each row
execute function check_player_count();


create or replace function check_session_capacity_update()
    returns trigger
    language plpgsql
as $$

declare
    current_players_count INT;
begin
    select count(sp.*)
    into current_players_count
    from SessionPlayer sp
    where sp.session_id = NEW.sid;

    if NEW.capacity > OLD.capacity And OLD.closed = true then
        update Session set closed = false where sid = NEW.sid;
    elsif NEW.capacity < OLD.capacity And current_players_count = NEW.capacity then
        update Session set closed = true where sid = NEW.sid;
    end if;

    return NEW;
end;
$$;


create trigger after_update_session
    after update on Session
    for each row
execute function check_session_capacity_update();

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