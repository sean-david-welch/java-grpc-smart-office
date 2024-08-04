-- enable foreign key constraints
pragma foreign_keys = on;

-- set journal mode to wal
pragma journal_mode = wal;

-- create tables with safety features

-- smart access service
create table if not exists door (
    id text primary key,
    status text not null
) strict;

create table if not exists accessCredentials (
    badgeid text primary key,
    pin text not null,
    accesslevel text not null
) strict;

create table if not exists logEntry (
    doorId text primary key,
    accesstime text not null,
    status text not null
) strict;

-- smart coffee service
create table if not exists inventoryItem (
    id text primary key,
    item text not null,
    quantity text not null
) strict;

-- smart meeting room service
create table if not exists roomDetails (
    roomId text primary key,
    name text not null,
    location text not null,
    capacity text not null,
    status text not null
) strict;

create table if not exists booking (
    bookingId text primary key,
    roomId text not null,
    userId text not null,
    timeslot text not null
) strict;

-- dummy data creation
insert into door (id, status) values
('door1', 'locked'),
('door2', 'unlocked'),
('door3', 'locked');

insert into accessCredentials (badgeid, pin, accesslevel) values
('badge1', '1234', 'admin'),
('badge2', '5678', 'user'),
('badge3', '9101', 'user');

insert into logEntry (doorId, accesstime, status) values
('door1', '2024-08-03 09:00:00', 'granted'),
('door2', '2024-08-03 10:00:00', 'denied'),
('door3', '2024-08-03 11:00:00', 'granted');

insert into inventoryItem (id, item, quantity) values
('item1', 'Coffee Beans', '10'),
('item2', 'Sugar', '20'),
('item3', 'Milk', '15');

insert into roomDetails (roomId, name, location, capacity, status) values
('room1', 'Conference Room A', 'First Floor', '10', 'available'),
('room2', 'Conference Room B', 'Second Floor', '8', 'occupied'),
('room3', 'Meeting Room C', 'First Floor', '5', 'available');

insert into booking (bookingId, roomId, userId, timeslot) values
('booking1', 'room1', 'user1', '2024-08-03 10:00:00 - 11:00:00'),
('booking2', 'room2', 'user2', '2024-08-03 11:00:00 - 12:00:00'),
('booking3', 'room3', 'user3', '2024-08-03 12:00:00 - 13:00:00');
