create schema if not exists `hotel_database`;
 use `hotel_database`;
 
 create table if not exists `Role`(
 `id` int auto_increment not null,
 `name` varchar(50) not null unique,
  constraint `PK_Role` primary key (`id` ASC) 
 );
 
 create table if not exists `Account`(
 `id` int auto_increment not null,
 `roleId` int not null,
 `login` varchar(50) not null unique,
 `password` varchar(50) not null,
 `isActive` bool not null,
  constraint `PK_Account` primary key (`id` ASC),
  constraint `FK_Account_Role` foreign key(`roleId`) references `Role` (`id`)
 );
 
  create table if not exists `Hotel`(
 `id` int auto_increment not null,
 `name` varchar(50) not null unique,
 `description` multilinestring not null,
 `imagePath` varchar(256) not null,
  constraint `PK_Hotel` primary key (`id` ASC) 
 );
 
create table if not exists `EmployeeProfile`(
 `id` int auto_increment not null,
 `accountId` int not null,
 `hotelId` int not null,
 `credentials` varchar(100) not null,
 `contactPhone` varchar(10) not null,
  constraint `PK_EmployeeProfile` primary key (`id` ASC),
  constraint `FK_EmployeeProfile_Account` foreign key(`accountId`) references `Account` (`id`),
  constraint `FK_EmployeeProfile_Hotel` foreign key(`hotelId`) references `Hotel` (`id`)
 );
 
create table if not exists `UserProfile`(
 `id` int auto_increment not null,
 `accountId` int not null,
 `credentials` varchar(100) not null,
 `contactPhone` varchar(10) not null,
 `passport` varchar(10) not null,
  constraint `PK_UserProfile` primary key (`id` ASC),
  constraint `FK_UserProfile_Account` foreign key(`accountId`) references `Account` (`id`)
 );
 
 create table if not exists `RoomType`(
 `id` int auto_increment not null,
 `hotelId` int not null,
 `name` varchar(50) not null,
 `description` varchar(50) not null,
 `costPerDay` float not null,
  constraint `PK_RoomType` primary key (`id` ASC),
  constraint `FK_RoomType_Hotel` foreign key(`hotelId`) references `Hotel` (`id`)
 );
 
  create table if not exists `Room`(
 `id` int auto_increment not null,
 `typeId` int not null,
 `number` varchar(10) not null,
  constraint `PK_Room` primary key (`id` ASC),
  constraint `FK_Room_RoomType` foreign key(`typeId`) references `RoomType` (`id`)
 );
 
create table if not exists `masters_records`(
 `recordId` int not null,
 `masterId` int not null,
  constraint `FK_masters_records_records` foreign key(`recordId`) references `records` (`id`),
  constraint `FK_records_masters` foreign key(`masterId`) references `masters` (`id`)
 );

insert into `admins` (login, password) values ('admin', 'admin');