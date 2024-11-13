-- user_status - recreated each launch for population of initial data
-- 1 - Active
-- 2 - Deleted
insert into user_status(name)
values ('Active'),('Deleted');

-- friendship_status - recreated each launch for population of initial data
-- 1 - Friendly
-- 2 - Requested
-- 3 - Received
-- 4 - Deleted
insert into friendship_status(name)
values ('Friendly'),('Requested'),('Received'),('Deleted');

-- film_status - recreated each launch for population of initial data
-- 1 - Active
-- 2 - Deleted
insert into film_status(name)
values ('Active'),('Deleted');

-- likes_status - recreated each launch for population of initial data
-- 1 - Active
-- 2 - Deleted
insert into likes_status(name)
values ('Active'),('Deleted');