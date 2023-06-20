create table estate_agent (
	id serial not null primary key,
	name varchar not null,
	address varchar,
	login varchar not null unique,
	password varchar not null
);

CREATE TABLE estate (
	id serial NOT null primary KEY,
	City varchar NOT null,
	Postal_Code int,
	Street varchar,
	Street_Number int,
	Square_Area varchar,
	estate_agent_id serial not null unique, 
	FOREIGN KEY (estate_agent_id) REFERENCES estate_agent(id)
);

create table apartment (
	id serial PRIMARY KEY REFERENCES estate(id),
	Floor integer not null,
	Rent NUMERIC(10, 2) not null,
	Rooms integer not null, 
	Balcony boolean not null, 
	Built_in_kitchen boolean not null
);

CREATE TABLE house (
  id serial PRIMARY KEY REFERENCES estate(id),
  price NUMERIC(10, 2) NOT NULL,
  floors INTEGER NOT NULL,
  garden BOOLEAN NOT null,
  id_person serial not null unique,
  foreign key (id_person) references person(person_id)
);

create table person (
	person_id serial primary key,
	first_name varchar not null,
	name varchar not null,
	adress varchar
);

create table contract (
	contract_number serial primary key,
	contract_date date not null,
	place varchar not null	
);

create table purchase_contract(
	contract_number serial primary key references contract(contract_number),
	number_installments integer not null,
	intrest_rate numeric(10, 2) not null,
	house_id INTEGER UNIQUE REFERENCES house(id)
);

create table tenancy_contract(
	contract_number serial primary key references contract(contract_number),
	strart_date date not null,
	duration integer not null,
	add_costs numeric(10, 2),
	apartment_id INTEGER UNIQUE REFERENCES apartment(id)
);


