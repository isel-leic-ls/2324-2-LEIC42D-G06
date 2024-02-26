CREATE TABLE Students (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(50) UNIQUE
);

INSERT INTO Students(id, name , email)
VALUES
(47718, 'Pedro Diz', '47718@alunos.isel.pt')

DROP TABLE IF EXISTS Students;