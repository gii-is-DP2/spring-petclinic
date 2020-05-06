-- Authorities
INSERT INTO authorities VALUES (1,'admin');
INSERT INTO authorities VALUES (2,'owner');

-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin','admin',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('admin',1);

-- Owners and their respective users and authorities_users
INSERT INTO users(username,password,enabled) VALUES ('george','george',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('george',2);
INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'george');

INSERT INTO users(username,password,enabled) VALUES ('betty','betty',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('betty',2);
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'betty');

INSERT INTO users(username,password,enabled) VALUES ('eduardo','eduardo',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('eduardo',2);
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'eduardo');

INSERT INTO users(username,password,enabled) VALUES ('harold','harold',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('harold',2);
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'harold');

INSERT INTO users(username,password,enabled) VALUES ('peter','peter',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('peter',2);
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'peter');

INSERT INTO users(username,password,enabled) VALUES ('jean','jean',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('jean',2);
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'jean');

INSERT INTO users(username,password,enabled) VALUES ('jeff','jeff',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('jeff',2);
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'jeff');

INSERT INTO users(username,password,enabled) VALUES ('maria','maria',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('maria',2);
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'maria');

INSERT INTO users(username,password,enabled) VALUES ('david','david',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('david',2);
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'david');

INSERT INTO users(username,password,enabled) VALUES ('carlos','carlos',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('carlos',2);
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'carlos');

INSERT INTO users(username,password,enabled) VALUES ('fede','fede',TRUE);
INSERT INTO authorities_users(user_username, authorities_id) VALUES ('fede',2);
INSERT INTO owners VALUES (11, 'Federico', 'Sartori', '2335 Independence La.', 'Sartori', '6085555487', 'fede');

INSERT INTO trainer VALUES (1, 'Mario', 'Balotelli', '45', 'mb@mb.com', '45', '45', '45', '45');
INSERT INTO trainer VALUES (2, 'Charlie', 'Austin', '28', 'chaz@chaz.com', '28', '28', '28', '28');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (14, 'Iker', '2012-06-08', 1, 11);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO hairdressings VALUES (1, '2021-01-01', 'Pelado a mi mascota', 0, '9:00', 1);
INSERT INTO hairdressings VALUES (99, '2022-02-02', 'TEST', 0, '7:00',1);

INSERT INTO review VALUES (1, 'Pésimo servicio', '2020-01-03', 1, 2, 'jeff');
INSERT INTO review VALUES (2, 'El mejor servicio que recibí en mi vida', '2020-03-13', 5, 0, 'peter');
INSERT INTO review VALUES (3, 'Mi iguana nunca se vio tan bonita', '2019-07-13', 5, 2, 'david');
INSERT INTO review VALUES (4, 'A fido le encantaron los ejercicios', '2020-03-13', 5, 0, 'david');
INSERT INTO review VALUES (5, 'Ni fu ni fa', '2020-04-13', 3, 1, 'carlos');

INSERT INTO training VALUES (1, '2021-03-03', 'Descripcion', 2, 1, 14, 1);