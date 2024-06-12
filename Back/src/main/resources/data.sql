insert into states (name)
values ('NULL'),
('Buenos Aires'),
('CABA'),
('Capital Federal'),
('Catamarca'),
('Chaco'),
('Chubut'),
('Córdoba'),
('Corrientes'),
('Entre Ríos'),
('Formosa'),
('Jujuy'),
('La Pampa'),
('La Rioja'),
('Mendoza'),
('Misiones'),
('Neuquén'),
('Río Negro'),
('Salta'),
('San Juan'),
('San Luis'),
('Santa Cruz'),
('Santa Fe'),
('Santiago del Estero'),
('Tierra del Fuego'),
('Tucumán');

-- Users (admin, user1/seller, delivery, user.../buyer):
insert into users (username, password,date_time,role,
                   email, name, lastname,phone,dni,dni_type,
                   id_state,direction,number_dir,postal_num,floor,room,
                   cvu)
values ('admin','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-01-01 00:00:01','ADMIN',
        'marcozlanuz@gmail.com','admin','admin', '351647', '449922', 'DNI',
        2, 'Tercero Arriba', '333', '5001','1','2',
        '832032608255661')
       ,
    ('seller','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-02-01 00:00:01','USER'
    ,'test_user_482362958@testuser.com','pablo','perez', '371647', '439922', 'DNI',
    3, 'Tercero Abajo', '222', '5002','3','4',
    '3267837162397314')
       ,
    ('user1','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-02-01 00:00:01','USER'
    ,'test_user_1617525073@testuser.com','servo','marces', '361647', '429922', 'DNI',
    3, 'Tercero Atreas', '111', '5003','','','');
insert into users (username, name, lastname, password, email,role,id_state,date_time)
values ('delivery','del','iver','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'del@a', 'DELIVERY',1,'2023-04-01 00:00:01'),
        ('user2','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a2@a', 'USER',1,'2023-02-02 00:00:01'),
        ('user3','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a3@a', 'USER',1,'2023-06-01 00:00:01'),
        ('user4','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a4@a', 'USER',1,'2023-09-02 00:00:01'),
        ('user5','a','a', '$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a5@a', 'USER',1,'2023-09-04 00:00:01'),
        ('user@a','a','a','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a6@a', 'USER',1,'2023-11-01 00:00:01');

-- Borradores:
insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (1,'Robot bola model', 'Este es un robot bola que cree', 'TECNOLOGIA',1,false,false,true,'2023-6-03 00:00:01');
insert into sections (id_publication, type, text)
values (1,'PHOTO', '');

-- Publications:
insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted,video, draft,date_time)
values (1,'Guitarra Electrica',
        'Esto es una guitarra electrica creada por mi :D, #music',
        'ARTE',1,true,5,5,false,
        'C825cMYBm6k'
       ,false,'2023-1-01 00:00:01');
insert into sections (id_publication, type, text)
values (2,'PHOTO', ''),
       (2,'PHOTO', ''),
       (2,'COND', 'Pongan atención a las cuerdas'),
       (2,'MAT', '5m3 de Madera de balso'),
       (2,'MAT', '6m de Cuerda'),
       (2,'MAT', '500ml de Pintura azul');
insert into sections (id_publication, type, text, number)
values
    (2,'STEP', 'Crear la parte superior del mango', 1),
    (2,'STEP', 'Crear la parte inferior del mango', 2),
    (2,'STEP', 'Unir todas las partes', 3);
insert into califications (id_publication, id_user, points)
values (2,5, 3.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (3,'Casa de pajaros ', 'pajaros blablabla', 'ARTE',1,true,22,400,false,false,'2023-1-03 00:00:01');
insert into sections (id_publication, type, text)
values (3,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (3,5, 2.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (4,'Robot bola STARWARS', 'pajaros', 'TECNOLOGIA',1,true,33,2770,false,false,'2023-1-08 00:00:01');
insert into sections (id_publication, type, text)
values (4,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (4,5, 1.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (5,'Carrito esquiva obstaculos ',
        'robot esquiva obstaculos. Espero que les guste #arduino',
        'TECNOLOGIA',1,true,41,5000,false,false,'2023-1-05 00:00:01');
insert into sections (id_publication, type, text)
values (5,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (5,5, 1.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (2,'Proyecto ciencias Sistema respiratorio', 'esto es ', 'CIENCIA',1,false,false, false,'2023-1-22 00:00:01');
insert into sections (id_publication, type, text)
values (6,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (6,5, 1);

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (3,'ROSTRO ROBOT', 'rostro robotico #arduino', 'TECNOLOGIA',1,false,false,false,'2023-2-09 00:00:01');
insert into sections (id_publication, type, text)
values (7,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (4,'Proyecto de motor cacero', 'proyecto de ciencias', 'OTRO',1,false,false,false,'2023-1-27 00:00:01');
insert into sections (id_publication, type, text)
values (8,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (5,'EXPERIMENTO BATERIAS🤑', 'pajaros', 'CIENCIA',1,false,false,false,'2023-1-25 00:00:01');
insert into sections (id_publication, type, text)
values (9,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (5,'Montañas', 'pajaros', 'CIENCIA',1,false,false,false,'2023-1-05 00:00:01');
insert into sections (id_publication, type, text)
values (10,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (2,'Recreacion del grito', 'esto es ', 'ARTE',1,false,false, false,'2023-7-02 00:00:01');
insert into sections (id_publication, type, text)
values (11,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Artesania', 'esto es #acuarela2023', 'CIENCIA',1,false,false, false,'2023-9-02 00:00:01');
insert into sections (id_publication, type, text)
values (12,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Solucionador de cubos', 'proyecto con #arduino', 'TECNOLOGIA',1,false,false, false,'2023-9-02 00:00:01');
insert into sections (id_publication, type, text)
values (13,'PHOTO', '');

--a
insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Proyecto ciencias Sistema respiratorio', 'esto es ', 'CIENCIA',1,false,false, false,'2023-9-02 00:00:01');
insert into sections (id_publication, type, text)
values (14,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (4,'ROSTRO ROBOT', 'rostro robotico', 'TECNOLOGIA',1,false,false,false,'2023-2-01 00:00:01');
insert into sections (id_publication, type, text)
values (15,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (3,'Proyecto de motor cacero', 'proyecto de ciencias', 'TECNOLOGIA',1,false,false,false,'2022-12-01 00:00:01');
insert into sections (id_publication, type, text)
values (16,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (2,'EXPERIMENTO BATERIAS🤑', 'pajaros', 'CIENCIA',1,false,false,false,'2023-2-05 00:00:01');
insert into sections (id_publication, type, text)
values (17,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (4,'Montañas', 'pajaros', 'CIENCIA',1,false,false,false,'2023-2-05 00:00:01');
insert into sections (id_publication, type, text)
values (18,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (6,'Recreacion del grito', 'esto es ', 'ARTE',1,false,false, false,'2023-5-02 00:00:01');
insert into sections (id_publication, type, text)
values (19,'PHOTO', '');
-- Sales:

insert into sales (merchant_order,id_user,date_time,sale_state)
values (1,1, '2024-4-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (1,2, 15,3);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (1,4, '2024-4-01 00:00:01','ENTREGADO');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (2,1, '2024-3-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (2,2, 522,3);
insert into sale_details (id_sale,id_publication,total,count)
values (2,3, 5,5);
insert into sale_details (id_sale,id_publication,total,count)
values (2,4, 966,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (2,4, '2024-3-22 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (3,2, '2024-6-19 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (3,3, 444,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (3,4, '2024-6-19 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (4,5, '2024-5-18 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (4,4, 5,123);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (4,4, '2024-5-18 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (5,2, '2024-4-11 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (5,5, 5,5);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (5,4, '2024-3-11 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (6,1, '2024-5-16 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (6,2, 50,3);
insert into sale_details (id_sale,id_publication,total,count)
values (6,3, 533,2);
insert into sale_details (id_sale,id_publication,total,count)
values (6,4, 122,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (6,4, '2024-5-16 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (7,2, '2024-4-15 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (7,3, 5,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (7,4, '2024-4-15 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (8,5, '2024-3-13 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (8,4, 522,3);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (8,4, '2024-3-13 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (9,2, '2024-4-20 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (9,5, 5,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (9,4, '2024-4-20 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (10,1, '2024-6-9 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (10,2, 5,3);
insert into sale_details (id_sale,id_publication,total,count)
values (10,3, 8,1);
insert into sale_details (id_sale,id_publication,total,count)
values (10,4, 5,8);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (10,4, '2024-6-9 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (11,2, '2024-5-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (11,3, 5,71);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (11,4, '2024-5-22 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (12,5, '2024-5-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (12,4, 3,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (12,4, '2024-5-01 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (13,2, '2024-4-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (13,5, 88,9);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (13,4, '2024-4-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (14,2, '2024-5-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (14,3, 700,3);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (14,4, '2024-5-22 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (14,5, '2024-5-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (14,4, 500,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (14,4, '2024-5-01 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (15,2, '2024-4-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (15,5, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (15,4, '2024-4-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (16,2, '2024-5-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (16,5, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (16,4, '2024-5-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (17,2, '2024-3-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (17,5, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (17,4, '2024-3-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (18,2, '2024-6-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (18,5, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (18,4, '2024-6-30 00:00:01','CANCELADA');