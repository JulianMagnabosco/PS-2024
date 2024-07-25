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
        'julian.magnabosco@gmail.com','Admin','Admin', '351647', '449922', 'DNI',
        2, 'Tercero Arriba', '333', '5001','1','2',
        '832032608255661')
        ,
        ('seller','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-02-01 00:00:01','USER'
        ,'test_user_482362958@testuser.com','Pablo','Perez', '371647', '439922', 'DNI',
        3, 'Rafael Nuñez', '222', '5002','3','4',
        '3267837162397314')
        ,
       ('user1','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-02-01 00:00:01','USER'
           ,'test_user_1617525073@testuser.com','Fabian','Marces', '361647', '429922', 'DNI',
        3, 'Tercero Arriba', '111', '5003','','','')
        ,
       ('damian','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2','2023-02-01 00:00:01','USER'
           ,'test_user_1617525073@testuser.com','Damian','Martines', '361647', '429922', 'DNI',
        3, 'Marcos Juarez', '111', '5003','','','');
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
        ('user@a','del2','iver','$2a$10$HYrDVRKzhpB0MRSSz5/lgeOiRdiFxD9bYuG3cFFe9tdDRcjjNUyX2',
        'a6@a', 'DELIVERY',1,'2023-11-01 00:00:01');

--notificaciones
insert into notifications (deleted,id_user,date_time, code,title,text)
values (false,1,'2023-6-10 00:00:01','pass_1','Cambio de contraseña', 'Se realizo un cambio');

insert into notifications (deleted,id_user,date_time, code,title,text)
values (false,1,'2023-6-07 00:00:01','req_1','Peticion de cambio de contraseña', 'Se solicito un cambio');

-- Borradores:
insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (1,'Robot bola model', 'Este es un robot bola que cree', 'TECNOLOGIA',1,false,false,true,'2023-2-03 00:00:01');
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
       (2,'COND', 'Pongan atención a las cuerdas, no se les vayan a romper'),
       (2,'MAT', '5m3 de Madera de balso'),
       (2,'MAT', '6m de Cuerda'),
       (2,'MAT', '500ml de Pintura azul(opcional)');
insert into sections (id_publication, type, text, number)
values
    (2,'STEP', 'Dibujar o descargar los siguientes planos', 1),
    (2,'STEP', 'Cortar la madera segun los planos y preparar los electronicos', 2),
    (2,'STEP', 'Unir todas las partes', 3);
insert into califications (id_publication, id_user, points)
values (2,5, 3.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (3,'Casa de pajaros', 'pajaros blablabla', 'ARTE',1,true,20,111,false,false,'2023-1-03 00:00:01');
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
values (4,'Carrito esquiva obstaculos',
        'robot esquiva obstaculos. Espero que les guste #arduino',
        'TECNOLOGIA',1,true,41,5000,false,false,'2023-1-05 00:00:01');
insert into sections (id_publication, type, text)
values (5,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (5,5, 1.5);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted, draft,date_time)
values (2,'Proyecto ciencias Sistema respiratorio', 'esto es ', 'CIENCIA',1,true,50,33,false, false,'2023-1-22 00:00:01');
insert into sections (id_publication, type, text)
values (6,'PHOTO', '');
insert into califications (id_publication, id_user, points)
values (6,5, 1);

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (3,'ROSTRO ROBOT', 'rostro robotico #arduino', 'TECNOLOGIA',1,true,230,200,false,false,'2023-2-09 00:00:01');
insert into sections (id_publication, type, text)
values (7,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,count,price,deleted ,draft,date_time)
values (4,'Proyecto de motor cacero', 'proyecto de ciencias', 'OTRO',1,true,230,200,false,false,'2023-1-27 00:00:01');
insert into sections (id_publication, type, text)
values (8,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (3,'EXPERIMENTO BATERIAS🤑', 'pajaros', 'CIENCIA',1,false,false,false,'2023-1-25 00:00:01');
insert into sections (id_publication, type, text)
values (9,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (2,'Montañas', 'pajaros', 'CIENCIA',1,false,false,false,'2023-1-05 22:00:01');
insert into sections (id_publication, type, text)
values (10,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (7,'Recreacion del grito', 'esto es ', 'ARTE',1,false,false, false,'2024-1-02 00:00:01');
insert into sections (id_publication, type, text)
values (11,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Artesania', 'esto es #acuarela2023', 'CIENCIA',1,false,false, false,'2024-1-02 00:00:01');
insert into sections (id_publication, type, text)
values (12,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (6,'Solucionador de cubos', 'proyecto con #arduino', 'TECNOLOGIA',1,false,false, false,'2023-2-02 22:00:01');
insert into sections (id_publication, type, text)
values (13,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Danbo yahoo', 'hice el robot de amazon yuohoohhh', 'ARTE',1,false,false, false,'2023-1-02 22:00:01');
insert into sections (id_publication, type, text)
values (14,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (8,'MACETAS', 'contecte para macetas', 'ARTE',1,false,false, false,'2023-8-02 22:00:01');
insert into sections (id_publication, type, text)
values (15,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (6,'Escupe humo', 'proyecto de botella escupe humo circular', 'CIENCIA',1,false,false, false,'2023-1-02 22:00:01');
insert into sections (id_publication, type, text)
values (16,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Reloj impreso en 3D', 'es relog impreso en 3d', 'CIENCIA',1,false,false, false,'2023-8-02 22:00:01');
insert into sections (id_publication, type, text)
values (17,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (9,'Reloj esculpido en piedra', 'Hola, esculpi un reloj en piedra', 'CIENCIA',1,false,false, false,'2023-8-02 22:00:01');
insert into sections (id_publication, type, text)
values (18,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (5,'Mano robot', 'Mano robot impresa en 3d #mano #arduino #robot', 'TECNOLOGIA',1,false,false, false,'2023-8-02 22:00:01');
insert into sections (id_publication, type, text)
values (19,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (6,'DIY Caminante', 'Mano robot impresa en 3d #mano #arduino #robot', 'TECNOLOGIA',1,false,false, false,'2023-2-02 22:00:01');
insert into sections (id_publication, type, text)
values (20,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (4,'Arduino Balancin', 'Mano robot impresa en 3d #mano #arduino #robot', 'TECNOLOGIA',1,false,false, false,'2023-8-02 22:00:01');
insert into sections (id_publication, type, text)
values (21,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (7,'Vaquita de tela', '#telares', 'ARTE',1,false,false, false,'2023-2-02 22:00:01');
insert into sections (id_publication, type, text)
values (22,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (7,'POCOYO', 'Pocoyo de tela', 'ARTE',1,false,false, false,'2023-1-02 22:00:01');
insert into sections (id_publication, type, text)
values (23,'PHOTO', '');
--a
insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (9,'Proyecto ciencias Sistema respiratorio', 'esto es ', 'CIENCIA',1,false,false, false,'2023-2-02 05:00:01');
insert into sections (id_publication, type, text)
values (24,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (4,'ROSTRO ROBOT', 'rostro robotico', 'TECNOLOGIA',1,false,false,false,'2023-2-01 00:00:01');
insert into sections (id_publication, type, text)
values (25,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (3,'Proyecto de motor cacero', 'proyecto de ciencias', 'TECNOLOGIA',1,false,false,false,'2024-1-01 00:00:01');
insert into sections (id_publication, type, text)
values (26,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (2,'EXPERIMENTO BATERIAS🤑', 'pajaros', 'CIENCIA',1,false,false,false,'2023-2-05 00:00:01');
insert into sections (id_publication, type, text)
values (27,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted ,draft,date_time)
values (4,'Montañas', 'pajaros', 'CIENCIA',1,false,false,false,'2023-2-05 23:00:01');
insert into sections (id_publication, type, text)
values (28,'PHOTO', '');

insert into publications (id_user, name, description, type, difficulty,can_sold,deleted, draft,date_time)
values (1,'Recreacion del grito ELIMINADO', 'esto es ', 'ARTE',1,false,true, false,'2024-1-02 00:00:01');
insert into sections (id_publication, type, text)
values (29,'PHOTO', '');
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
values (6,6, 50,3);
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
values (8,7, 522,3);
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
values (13,7, 88,9);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (13,4, '2024-4-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (14,2, '2024-5-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (14,3, 700,3);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (14,4, '2024-5-22 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (15,2, '2024-4-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (15,5, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (15,4, '2024-4-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (16,2, '2024-5-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (16,6, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (16,4, '2024-5-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (17,2, '2024-3-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (17,6, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (17,4, '2024-3-30 00:00:01','CANCELADA');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (18,2, '2024-6-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (18,7, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (18,4, '2024-6-30 00:00:01','CANCELADA');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (19,5, '2024-5-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (19,4, 500,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (19,4, '2024-5-01 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (20,5, '2024-5-07 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (20,4, 500,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (20,4, '2024-5-07 00:00:01','PENDIENTE');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (21,2, '2024-6-30 00:00:01','CANCELADA');
insert into sale_details (id_sale,id_publication,total,count)
values (21,7, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (21,4, '2024-6-30 00:00:01','CANCELADA');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (22,5, '2024-2-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (22,4, 3,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (22,4, '2024-2-01 00:00:01','PENDIENTE');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (23,5, '2024-7-08 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (23,4, 3,4);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (23,4, '2024-7-08 00:00:01','PENDIENTE');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (24,5, '2024-7-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (24,6, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (24,4, '2024-7-22 00:00:01','PENDIENTE');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (25,5, '2024-2-12 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (25,6, 222,1);
insert into sale_details (id_sale,id_publication,total,count)
values (25,3, 700,3);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (25,4, '2024-2-12 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (26,5, '2024-2-22 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (26,6, 222,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (26,4, '2024-2-22 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (27,5, '2024-2-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (27,6, 2555,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (27,4, '2024-2-20 00:00:01','PENDIENTE');

---8
insert into sales (merchant_order,id_user,date_time,sale_state)
values (28,5, '2024-2-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (28,8, 2555,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (28,4, '2024-2-20 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (29,5, '2024-2-29 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (29,8, 3333,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (29,4, '2024-2-29 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (30,5, '2024-5-01 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (30,8, 2555,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (30,4, '2024-5-01 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (31,5, '2024-6-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (31,8, 2555,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (31,4, '2024-6-20 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (32,5, '2024-7-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (32,8, 444,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (32,4, '2024-7-20 00:00:01','PENDIENTE');


insert into sales (merchant_order,id_user,date_time,sale_state)
values (33,5, '2024-3-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (33,8, 200,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (33,4, '2024-3-20 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (34,5, '2024-3-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (34,8, 200,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (34,4, '2024-3-20 00:00:01','PENDIENTE');

insert into sales (merchant_order,id_user,date_time,sale_state)
values (35,5, '2024-6-20 00:00:01','APROBADA');
insert into sale_details (id_sale,id_publication,total,count)
values (35,8, 2555,1);
insert into deliveries (id_sale,id_dealer,date_time,delivery_state)
values (35,4, '2024-6-20 00:00:01','PENDIENTE');