insert into states (name)
values ('Córdoba');
insert into users (username, name, lastname, password, email,role,id_state)
values ('admin','admin','admin', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'ADMIN',1);
insert into users (username, name, lastname, password, email,role,id_state)
values ('user','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER',1);

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted)
values (1,'Panal artesanal', 'esto es un panal de abejas artesanal #bees', 'ARTE',1,false,false);

insert into sections (id_publication, type, text)
values (1,'PHOTO', 'esto es un panal de abejas');

insert into sections (id_publication, type, text)
values (1,'MAT', 'madera');
insert into sections (id_publication, type, text)
values (1,'COND', 'tenga cuidaddo con el panal');

insert into sections (id_publication, type, text, number)
values (1,'STEP', 'Abrir es un panal de abejas', 1);
