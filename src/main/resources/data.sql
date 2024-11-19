-- user_status - recreated each launch for population of initial data
-- 0 - Active
-- 1 - Deleted
INSERT ignore
INTO   entry_status
       (
              id,
              NAME
       )
       VALUES
       (
              0,
              'Active'
       )
       ,
       (
              1,
              'Deleted'
       );

-- friendship_status - recreated each launch for population of initial data
-- 0 - Friendly
-- 1 - Requested
-- 2 - Received
-- 3 - Deleted
INSERT ignore
INTO   friendship_status
       (
              id,
              NAME
       )
       VALUES
       (
              0,
              'None'
       )
       ,
       (
              1,
              'Friendly'
       )
       ,
       (
              2,
              'Deleted'
       );

-- rating - recreated each launch for population of initial data
INSERT ignore
INTO   rating
       (
              id,
              NAME
       )
       VALUES
       (
              1,
              'G'
       )
       ,
       (
              2,
              'PG'
       )
       ,
       (
              3,
              'PG-13'
       )
       ,
       (
              4,
              'R'
       )
       ,
       (
              5,
              'NC-17'
       );

-- rating - recreated each launch for population of initial data
INSERT ignore
INTO   genre
       (
              id,
              NAME
       )
       VALUES
       (
              1,
              'Комедия'
       )
       ,
       (
              2,
              'Драма'
       )
       ,
       (
              3,
              'Мультфильм'
       )
       ,
       (
              4,
              'Триллер'
       )
       ,
       (
              5,
              'Документальный'
       )
       ,
       (
              6,
              'Боевик'
       );
