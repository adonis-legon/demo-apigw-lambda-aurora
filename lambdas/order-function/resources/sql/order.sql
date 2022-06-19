CREATE TABLE IF NOT EXISTS customer_order(
	id serial not null,
    customer_name varchar(255) null,
	total_amount decimal not null,
	primary key (id)
);