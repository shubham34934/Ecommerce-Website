# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table shop_item_model (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  image                     varchar(255),
  creation_date             datetime(6),
  constraint pk_shop_item_model primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table shop_item_model;

SET FOREIGN_KEY_CHECKS=1;

