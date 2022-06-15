CREATE TABLE IF NOT EXISTS customer(
	id serial not null,
	name varchar(255) null,
	email varchar(255) null,
	primary key (id)
);