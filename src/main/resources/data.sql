

insert into member (id, first_name, last_name, email, password, enabled, createdOn, modifiedOn, createdBy, modifiedBy) values
(1, 'admin', 'admin', 'admin@admin.com', '$2a$10$swK6dfjfSwe9Ql0twZnvCecSYCu2v4Sp659pVg2DdIoWtoDcr/xMq', true,
'2012-06-02 00:00:00','2012-06-02 00:00:00','admin','admin');

insert into member (id, first_name, last_name, email, password, enabled, createdOn, modifiedOn, createdBy, modifiedBy) values
    (2, 'guest','guest', 'guest@guest.com', '$2a$10$mYqHoY9ku7MfbKBzrLBkT.NOwbFkXlYDDdns6XwFY0nRA1EdJPWTi', true,
     '2019-10-15 00:00:00','2019-10-15 00:00:00','admin','admin');

insert into member (id, first_name, last_name, email, password, enabled, createdOn, modifiedOn, createdBy, modifiedBy) values
    (3, 'manager1','manager1', 'manager@manager.com', '$2a$10$sOirKTjm2JSRLlkOTik6FeGnXJJG1PH5UtQtYUfHccRo0xB086YiC', true,
     '2019-10-15 00:00:00','2019-10-15 00:00:00','admin','admin');

insert into member (id, first_name, last_name, email, password, enabled, createdOn, modifiedOn, createdBy, modifiedBy) values
    (4, 'manager2','manager2', 'manager.2@manager.com', '$2a$10$f6GSOdoeKUsVobCrb4dVn.RqWNXps.fQOyE5IWxHFld5nsxwQjnhS', true,
     '2019-10-15 00:00:00','2019-10-15 00:00:00','admin','admin');

insert into roles (id, name, description, createdOn, modifiedOn, createdBy, modifiedBy) values
    (1, 'ADMIN', 'Administrator of the Openclassroom','2022-12-12 00:00:00','2022-12-12 06:56:00','admin','admin');

insert into roles (id, name, description, createdOn, modifiedOn, createdBy, modifiedBy) values
    (2, 'MANAGER', 'Manager for a single company','2022-12-12 00:00:00','2022-12-12 06:56:00','admin','admin');

insert into roles (id, name, description, createdOn, modifiedOn, createdBy, modifiedBy) values
    (3, 'FRONT_DESK', 'Front desk executive for a single company','2022-12-12 00:00:00','2022-12-12 06:56:00','admin','admin');

insert into roles (id, name, description, createdOn, modifiedOn, createdBy, modifiedBy) values
    (4, 'CUSTOMER', 'Customer for a single company','2022-12-12 00:00:00','2022-12-12 06:56:00','admin','admin');

insert into users_roles(user_id, role_id) values (1, 1);
insert into users_roles(user_id, role_id) values (2, 2);
insert into users_roles(user_id, role_id) values (3, 3);
insert into users_roles(user_id, role_id) values (4, 4);
