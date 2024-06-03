select *
from player;
select *
from game;
select *
from session;
select *
from session_player;


drop table if exists player;
drop table if exists game;
drop table if exists session;
drop table if exists session_player;

select *
from player;
INSERT INTO player (pname, pemail, ptoken, pusername)
VALUES ('manu', 'manu@gmail.com', '4d259054-dbfc-4f5d-945e-b5c33b89a2c5', 'manu');
INSERT INTO game (gname, gdeveloper, ggenre)
VALUES ('Dota 2', 'Valve', ARRAY [1, 2, 3]);

delete
from session_player s
where s.session_id > 1;
delete
from session
where sid > 1;
delete
from game
where gid = 6;



SELECT s.sid,
       s.snofplayers,
       s.sessiondate,
       s.state,
       g.gid,
       g.gname,
       g.gdeveloper,
       g.ggenre,
       p.pid,
       p.pname,
       p.pemail,
       p.ptoken,
       p.pusername
FROM session s
         JOIN game g ON s.sgame_id = g.gid
         JOIN session_player sp ON s.sid = sp.session_id
         JOIN player p ON sp.player_id = p.pid
WHERE s.sid = 1;


SELECT s.sid,
       s.snofplayers,
       s.sessiondate,
       s.state,
       g.gid,
       g.gname,
       g.gdeveloper,
       g.ggenre

FROM session s
         JOIN game g ON s.sgame_id = g.gid

WHERE s.sid = 1


