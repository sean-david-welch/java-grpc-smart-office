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
    userid text primary key,
    accesstime text not null,
    status text not null
) strict;

-- smart coffee service
create table if not exists inventoryTtem (
    id text primary key,
    item text not null,
    quantity text not null
) strict;

-- smart meeting room service
create table if not exists roomDetails (
    roomId text primary key,
    name text not null,
    location text not null
    capacity text not null
    status text not null
) strict;

create table if not exists booking (
    bookingId text primary key
    roomId text not null,
    userId text not null,
    timeslot text not null
) strict;
