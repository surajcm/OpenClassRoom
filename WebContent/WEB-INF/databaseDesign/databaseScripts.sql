-- expecting to use mysql
create schema superspringdb;
use superspringdb;
create table user
    (id bigint not null auto_increment,
    name varchar(255) not null ,
    pass varchar(255) not null,
    role varchar(255) not null
    createdOn datetime not null,
    modifiedOn datetime not null,
    createdBy varchar(45),
    modifiedBy varchar(45),
    primary key (id));
insert into user values (1,"admin","admin","admin",CURDATE(),CURDATE(),"admin","admin");
insert into user values (2,"guest","guest","guest",CURDATE(),CURDATE(),"admin","admin");
