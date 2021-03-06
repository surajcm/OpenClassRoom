create table user (
    id bigint generated by default as identity (start with 1) primary key,
    name varchar(225),
    logInId varchar(225),
    password varchar(255),
    role varchar(45),
    createdOn datetime,
    modifiedOn datetime,
    createdBy varchar(45),
    modifiedBy varchar(45)
);
