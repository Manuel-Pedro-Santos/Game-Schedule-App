drop table if exists session_player;
drop table if exists session;
drop table if exists game;
drop table if exists player;


create table player
(
    pid       serial primary key,
    pname     varchar(80),
    pusername varchar(80),
    pemail    varchar(80),
    ppassword varchar(80),
    ptoken    uuid
);

create table game
(
    gid        serial primary key,
    gname      varchar(80),
    gdeveloper varchar(80),
    ggenre     int[]
);

create table session
(
    sid         serial primary key,
    snofplayers int,
    sessionDate timestamp,
    state       varchar(6),
    sgame_id    int references game (gid)
);



CREATE TABLE session_player
(
    session_id INT REFERENCES session (sid),
    player_id  INT REFERENCES player (pid),
    PRIMARY KEY (session_id, player_id)
);
drop table if exists session_player;

delete
from game
where game.gid >= 0;

/*For Testing purposes*/
INSERT INTO game (gname, gdeveloper, ggenre)
VALUES ('Dota 2', 'Valve', ARRAY [1, 2, 3]);
INSERT INTO session (snofplayers, sessionDate, sgame_id)
VALUES (5, '2020-12-12 12:12:12', 1);
delete
from game
where gid = 2;
INSERT INTO game (gname, gdeveloper, ggenre)
VALUES ('The Witcher 3', 'CD Projekt Red', ARRAY [1, 3]),
       ('Final Fantasy VII Remake', 'Square Enix', ARRAY [2, 4]),
       ('Among Us', 'Innersloth', ARRAY [5]);



INSERT INTO session (snofplayers, sessionDate, state, sgame_id)
VALUES (4, '2024-03-20 15:00:00', 'OPEN', 1),
       (2, '2024-03-21 18:30:00', 'OPEN', 2),
       (6, '2024-03-22 14:45:00', 'OPEN', 3);

INSERT INTO session_player (session_id, player_id)
VALUES (1, 1);

INSERT INTO game (gname, gdeveloper, ggenre)
VALUES ('CS', 'Valve', ARRAY [1, 2, 3]);





