insert into users (name, password, email,role) values ('admin', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'ADMIN');
insert into users (name, password, email,role) values ('user', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2', 'a@a', 'USER');

insert into publications (id_user, name, description, type, dificulty,can_sold) values (1,'testp', 'hola', 'tipo1','dificulty',false);
