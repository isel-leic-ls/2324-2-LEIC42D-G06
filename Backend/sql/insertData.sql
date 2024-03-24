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
	(2, '2025-02-27 20:00:00', true, 102);


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