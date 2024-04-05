insert into Game(name, developer, genres) values
    ('GTA', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('GTA 2', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('GTA III', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('GTA IV', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('GTA San Andreas', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('GTA Vice City', 'Rockstar', ARRAY['Action', 'Adventure']),
    ('Rocket League', 'Psyonix', ARRAY ['Cars', 'Sports', 'Football']),
    ('Minecraft', 'Mojang', ARRAY['Sandbox', 'Survival']);

insert into Session(capacity, session_date, closed, game_id) values
    (2, '2024-09-01 18:00:00', false, 102),
    (4, '2024-09-01 19:00:00', false, 102),
    (2, '2024-10-06 10:00:00', true, 101),
	(2, '2024-10-06 11:00:00', true, 101),
	(2, '2024-10-06 12:00:00', true, 101),
	(2, '2024-10-09 19:00:00', true, 101),
	(2, '2024-10-04 20:00:00', true, 101);

insert into SessionPlayer(session_id, player_id) values
    (10003, 1000),
    (10004, 1001),
    (10005, 1000),
    (10005, 1001),
    (10006, 1000),
    (10006, 1001),
    (10007, 1000),
    (10007, 1001),
    (10008, 1000),
    (10008, 1001),
    (10009, 1000),
    (10009, 1001);
