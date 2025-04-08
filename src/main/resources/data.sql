CREATE TABLE IF NOT EXISTS users (
  id INT,
  firstname VARCHAR,
  lastname VARCHAR,
  email VARCHAR);

INSERT INTO users (id, firstname, lastname, email) VALUES
  (1, 'Patricio', 'Gonzalez', 'pgonzales@gmail.com'),
  (2, 'Ismael', 'Correa', 'icorrea@gmail.com'),
  (3, 'Felipe', 'Correa', 'fcorrea@gmail.com'),
  (4, 'Mauricio', 'Sepúlveda', 'msepulveda@gmail.com'),
  (5, 'Paulina', 'Torres', 'ptorres@gmail.com'),
  (6, 'Francisca', 'Tejos', 'ftejos@gmail.com');