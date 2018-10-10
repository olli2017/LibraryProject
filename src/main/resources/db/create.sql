drop table if exists users;
CREATE TABLE IF NOT EXISTS users(
	id SERIAL PRIMARY KEY,
	name VARCHAR(45),
	password VARCHAR(512),
	email VARCHAR(64)
);

drop table if exists books;
CREATE TABLE IF NOT EXISTS books(
	id SERIAL PRIMARY KEY,
    isbn VARCHAR(45),
	title VARCHAR(45),
	author VARCHAR(45),
	user_id INTEGER,
    state INTEGER
);
