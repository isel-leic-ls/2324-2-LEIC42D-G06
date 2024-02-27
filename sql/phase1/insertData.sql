-- Insert into Player table
INSERT INTO Player(name, email) VALUES 
	('Pedro', 'pedro@hotmail.com'),
	('Vasco', 'vasco@gmail.com'),
	('Jonny', 'jonny@live.com.pt');


-- Insert into Game table
INSERT INTO Game(name, developer, genres) VALUES 
	('Lord of the Rings Online', NULL, ARRAY['RPG', 'Adventure']),
	('Apex Legends', NULL, ARRAY['Shooter', 'Action']),
	('Civilization VI', NULL, ARRAY['Turn-Based', 'Strategy']);


-- Insert into Session table
INSERT INTO Session(capacity, session_date, game_id) VALUES 
	(3, '2024-02-25', 100),
	(3, '2024-02-26', 101),
	(2, '2024-02-27', 102);


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