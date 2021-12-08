drop table if exists tbl_score;
create table tbl_score
(
    id bigint auto_increment comment 'primary key'
        primary key,
    player varchar(64) not null comment 'player name',
    score int default 0 not null comment 'score',
    time datetime not null comment 'the time when player got score'
);
insert into tbl_score (player, score, `time`) values ('Player1', 230, '2021-12-01 14:23:09');
insert into tbl_score (player, score, `time`) values ('Keanu', 80, '2021-12-12 03:23:09');
insert into tbl_score (player, score, `time`) values ('keaNu', 124, '2021-12-01 00:00:00');
insert into tbl_score (player, score, `time`) values ('KEanu', 16, '2021-12-10 00:00:00');
insert into tbl_score (player, score, `time`) values ('Justin', 190, '2021-12-10 23:59:59');
insert into tbl_score (player, score, `time`) values ('jusTIN', 305, '2021-12-10 20:01:09');
insert into tbl_score (player, score, `time`) values ('JuStin', 206, '2021-12-08 13:47:09');
insert into tbl_score (player, score, `time`) values ('PlaYer1', 231, '2021-12-7 14:23:09');
insert into tbl_score (player, score, `time`) values ('Monica', 60, '2021-12-06 14:23:09');
insert into tbl_score (player, score, `time`) values ('moNicA', 180, '2021-12-05 14:23:09');
insert into tbl_score (player, score, `time`) values ('monica', 149, '2021-12-04 14:23:09');
insert into tbl_score (player, score, `time`) values ('keanu', 138, '2021-12-03 14:23:09');