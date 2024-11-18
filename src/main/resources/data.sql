-- user_status - recreated each launch for population of initial data
-- 0 - Active
-- 1 - Deleted
insert ignore into entry_status(id,name)
values (0,'Active'),(1,'Deleted');

-- friendship_status - recreated each launch for population of initial data
-- 0 - Friendly
-- 1 - Requested
-- 2 - Received
-- 3 - Deleted
insert ignore into friendship_status(id,name)
values (0,'Friendly'),(1,'Requested'),(2,'Received'),(3,'Deleted');

-- rating - recreated each launch for population of initial data
insert ignore into rating(id, name)
values (1,'G'),(2,'PG'),(3,'PG-13'),(4,'R'),(5,'NC-17');

-- rating - recreated each launch for population of initial data
insert ignore into genre(id,name)
values (0,'None'),(1,'Action'),(2,'Comedy'),(3,'Drama'),(4,'Fantasy'),
(5,'Horror'),(6,'Mystery'),(7,'Romance'),(8,'Thriller'),(9,'Western');
