CREATE TABLE IF NOT EXISTS users (
  id INT,
  name VARCHAR,
  email VARCHAR,
  password VARCHAR,
  created_at DATE,
  modified_at DATE,
  last_login DATE,
  is_active BOOLEAN DEFAULT true,
  token VARCHAR DEFAULT '');

INSERT INTO users (id, name, email, password, created_at, modified_at, last_login, is_active, token) VALUES
  (1, 'Patricio Gonzalez', 'pgonzales@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, ''),
  (2, 'Ismael Correa', 'icorrea@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, ''),
  (3, 'Felipe Correa', 'fcorrea@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, ''),
  (4, 'Mauricio Sepúlveda', 'msepulveda@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, ''),
  (5, 'Paulina Torres', 'ptorres@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, ''),
  (6, 'Francisca Tejos', 'ftejos@gmail.com', 'try1234', CURRENT_DATE(), CURRENT_DATE(), CURRENT_DATE(), true, '');