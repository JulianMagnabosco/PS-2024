insert into states (name)
values ('Córdoba');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('admin','admin','admin','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'ADMIN',1,'2023-01-01 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-02-01 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user2','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-02-02 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user3','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-06-01 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user4','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-09-02 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user5','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-09-04 00:00:01');
insert into users (username, name, lastname, password, email,role,id_state,creation_time)
values ('user6','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1,'2023-11-01 00:00:01');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted,creation_time)
values (1,'Panal artesanal', 'esto es un panal de abejas artesanal #bees', 'ARTE',1,false,false,'2023-1-01 00:00:01');

insert into sections (id_publication, type, text)
values (1,'PHOTO', '');

insert into sections (id_publication, type, text)
values (1,'MAT', 'madera');
insert into sections (id_publication, type, text)
values (1,'COND', 'tenga cuidaddo con el panal');

insert into sections (id_publication, type, text, number)
values (1,'STEP', 'Abrir es un panal de abejas', 1);



insert into publications (id_user, name, description, type, difficulty,can_sold,deleted,creation_time)
values (1,'Casa de pajaros ', 'pajaros', 'ARTE',1,false,false,'2023-1-01 00:00:01');

insert into sections (id_publication, type, text)
values (2,'PHOTO', '');


insert into publications (id_user, name, description, type, difficulty,can_sold,deleted,creation_time)
values (2,'Casa de perros ', 'pajaros', 'TECNOLOGIA',1,false,false,'2023-1-01 00:00:01');

insert into sections (id_publication, type, text)
values (3,'PHOTO', '');