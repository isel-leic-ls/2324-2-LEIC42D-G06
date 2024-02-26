INSERT INTO Player(name, email)
VALUES 
('Pedro', 'pedro@hotmail.com'),
('Vasco', 'vasco@gmail.com'),
('Jonny', 'jonny@live.com.pt');

INSERT INTO Game(id, name, genres)
VALUES 
(1,'Lord of the Rings Online', ARRAY['RPG', 'Adventure']),
(2,'Apex Legends', ARRAY['Shooter', 'Action']),
(3,'Civilization VI', ARRAY['Turn-Based', 'Strategy']);

INSERT INTO Session (id, number_of_players, session_date, game_id)
VALUES 
(1, 3, '2024-02-25', 1),
(2, 3, '2024-02-26', 2),
(3, 2, '2024-02-27', 3);


INSERT INTO Player_Session (session_id, player_id) 
VALUES 
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(2, 3),
(3, 1),
(3, 3);