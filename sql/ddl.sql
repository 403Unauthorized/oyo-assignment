drop database if exists oyo;
create database oyo;

drop table if exists tbl_score;
create table tbl_score(
  id bigint auto_increment comment 'primary key',
  player varchar(64) not null comment 'player name',
  score int not null default 0 comment 'score',
  time datetime not null comment 'the time when player got score',
  primary key (id),
  unique key (player)
);