-- enable foreign key constraints
pragma foreign_keys = on;

-- set journal mode to wal
pragma journal_mode = wal;

-- Smart Access Service
create table if not exists door
(
    id     integer primary key autoincrement,
    status text check (status in ('locked', 'unlocked'))
) strict;

create table if not exists access_credentials
(
    user_id      integer primary key,
    access_level text check (access_level in ('unknown_level', 'general', 'admin'))
) strict;

create table if not exists access_log
(
    id          integer primary key autoincrement,
    door_id     integer,
    user_id     integer,
    access_time text,
    foreign key (door_id) references door (id),
    foreign key (user_id) references access_credentials (user_id)
) strict;

-- Smart Coffee Service
create table if not exists inventory_item
(
    id       integer primary key autoincrement,
    item     text unique check (item in ('milk', 'water', 'coffee_beans')),
    quantity integer check (quantity >= 0)
) strict;

create table if not exists coffee_order
(
    id          integer primary key autoincrement,
    coffee_type text check (coffee_type in ('americano', 'flat_white', 'cortado'))
) strict;

-- Smart Meeting Room Service
create table if not exists room_details
(
    room_id  integer primary key autoincrement,
    location text,
    status   text check (status in ('unavailable', 'available', 'occupied')),
    available_times text
) strict;

create table if not exists booking
(
    booking_id integer primary key autoincrement,
    room_id    integer,
    user_id    integer,
    time_slot  text,
    foreign key (room_id) references room_details (room_id)
) strict;

-- dummy data creation --

-- Smart Access Service
insert into door (id, status)
values (1, 'locked'),
       (2, 'unlocked'),
       (3, 'locked');

insert into access_credentials (user_id, access_level)
values (101, 'general'),
       (102, 'admin'),
       (103, 'general'),
       (104, 'admin');

insert into access_log (door_id, user_id, access_time)
values (1, 101, '09:00'),
       (2, 102, '10:00'),
       (3, 103, '11:00'),
       (1, 104, '13:00');

-- Smart Coffee Service
insert into inventory_item (item, quantity)
values ('milk', 1000),
       ('water', 5000),
       ('coffee_beans', 2000);

insert into coffee_order (id, coffee_type)
values (1, 'americano'),
       (2, 'flat_white'),
       (3, 'cortado'),
       (4, 'americano');

-- Smart Meeting Room Service
insert into room_details (room_id, location, status, available_times)
values
    (4, 'floor 5', 'available', '["08:00", "13:00", "16:00"]'),
    (5, 'floor 6', 'unavailable', '[]'),
    (6, 'floor 7', 'available', '["10:00", "15:00"]'),
    (7, 'floor 8', 'occupied', '["11:00", "14:00"]'),
    (8, 'floor 9', 'available', '["09:00", "12:00", "15:00"]'),
    (9, 'floor 10', 'available', '["07:00", "09:00", "11:00"]'),
    (10, 'floor 11', 'occupied', '["13:00", "15:00"]');

-- Corrected booking with valid room_id references
insert into booking (booking_id, room_id, user_id, time_slot)
values (1, 4, 101, '14:00'),
       (2, 6, 102, '11:00'),
       (3, 8, 103, '09:00'),
       (4, 9, 104, '13:00');
