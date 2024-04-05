insert into users (name, password, email,role)
values ('admin', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'ADMIN');
insert into users (name, password, email,role)
values ('user', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER');

insert into publications (id_user, name, description, type, difficulty,can_sold)
values (1,'Panal de abejas', 'esto es un panal de abejas', 'ARTE',0,false);

insert into sections (id_publication, type, text)
values (1,'COND', 'esto es un panal de abejas');

insert into sections (id_publication, type, text, number)
values (1,'STEP', 'Abrir es un panal de abejas', 1);
