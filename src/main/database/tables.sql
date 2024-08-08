-- Enable foreign key constraints
pragma foreign_keys = on;

-- Set journal mode to wal
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
    room_id         integer primary key autoincrement,
    location        text,
    status          text check (status in ('unavailable', 'available', 'occupied')),
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

-- Dummy data creation --

-- Smart Access Service
insert into door (id, status)
values (1, 'locked'),
       (2, 'unlocked'),
       (3, 'locked'),
       (4, 'unlocked'),
       (5, 'locked');

insert into access_credentials (user_id, access_level)
values (1, 'general'),
       (2, 'admin'),
       (3, 'general'),
       (4, 'admin'),
       (5, 'general'),
       (6, 'unknown_level');

insert into access_log (door_id, user_id, access_time)
values (1, 1, '09:00'),
       (2, 2, '10:00'),
       (3, 3, '11:00'),
       (1, 4, '13:00'),
       (2, 5, '14:00'),
       (3, 6, '15:00'),
       (4, 1, '16:00'),
       (5, 2, '17:00');

-- Smart Coffee Service
insert into inventory_item (item, quantity)
values ('milk', 1000),
       ('water', 5000),
       ('coffee_beans', 2000);

insert into coffee_order (id, coffee_type)
values (1, 'americano'),
       (2, 'flat_white'),
       (3, 'cortado'),
       (4, 'americano'),
       (5, 'cortado'),
       (6, 'flat_white'),
       (7, 'americano');

-- Smart Meeting Room Service
insert into room_details (room_id, location, status, available_times)
values (1, 'floor 5', 'available', '["08:00", "13:00", "16:00"]'),
       (2, 'floor 6', 'unavailable', '[]'),
       (3, 'floor 7', 'available', '["10:00", "15:00"]'),
       (4, 'floor 8', 'occupied', '["11:00", "14:00"]'),
       (5, 'floor 9', 'available', '["09:00", "12:00", "15:00"]'),
       (6, 'floor 10', 'available', '["07:00", "09:00", "11:00"]'),
       (7, 'floor 11', 'occupied', '["13:00", "15:00"]'),
       (8, 'floor 12', 'available', '["08:00", "10:00", "14:00"]'),
       (9, 'floor 13', 'unavailable', '[]'),
       (10, 'floor 14', 'occupied', '["09:00", "11:00"]');

-- Corrected booking with updated room_id references
insert into booking (booking_id, room_id, user_id, time_slot)
values (1, 1, 1, '14:00'),
       (2, 3, 2, '11:00'),
       (3, 5, 3, '09:00'),
       (4, 6, 4, '13:00'),
       (5, 7, 5, '15:00'),
       (6, 8, 6, '10:00'),
       (7, 10, 7, '11:00');
