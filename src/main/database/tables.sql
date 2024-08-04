-- enable foreign key constraints
PRAGMA foreign_keys = ON;

-- set journal mode to wal
PRAGMA journal_mode = WAL;

-- Smart Access Service
CREATE TABLE IF NOT EXISTS door
(
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT NOT NULL CHECK (status IN ('LOCKED', 'UNLOCKED'))
) STRICT;

CREATE TABLE IF NOT EXISTS access_credentials
(
    user_id      INTEGER PRIMARY KEY,
    access_level TEXT NOT NULL CHECK (access_level IN ('UNKNOWN_LEVEL', 'GENERAL', 'ADMIN'))
) STRICT;

CREATE TABLE IF NOT EXISTS access_log
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    door_id     INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    access_time TEXT    NOT NULL,
    FOREIGN KEY (door_id) REFERENCES door (id),
    FOREIGN KEY (user_id) REFERENCES access_credentials (user_id)
) STRICT;

-- Smart Coffee Service
CREATE TABLE IF NOT EXISTS inventory_item
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    item     TEXT    NOT NULL UNIQUE CHECK (item IN ('MILK', 'WATER', 'COFFEE_BEANS')),
    quantity INTEGER NOT NULL CHECK (quantity >= 0)
) STRICT;

CREATE TABLE IF NOT EXISTS coffee_order
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    coffee_type TEXT NOT NULL CHECK (coffee_type IN ('AMERICANO', 'FLAT_WHITE', 'CORTADO'))
) STRICT;

-- Smart Meeting Room Service
CREATE TABLE IF NOT EXISTS room_details
(
    room_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    name     TEXT NOT NULL,
    location TEXT NOT NULL,
    status   TEXT NOT NULL CHECK (status IN ('UNAVAILABLE', 'AVAILABLE', 'OCCUPIED'))
) STRICT;

CREATE TABLE IF NOT EXISTS booking
(
    booking_id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_id    INTEGER NOT NULL,
    user_id    INTEGER NOT NULL,
    time_slot  TEXT    NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room_details (room_id)
) STRICT;

-- dummy data creation --

-- Smart Access Service
INSERT INTO door (id, status)
VALUES (1, 'LOCKED'),
       (2, 'UNLOCKED'),
       (3, 'LOCKED');

INSERT INTO access_credentials (user_id, access_level)
VALUES (101, 'GENERAL'),
       (102, 'ADMIN'),
       (103, 'GENERAL'),
       (104, 'ADMIN');

-- Corrected access_log with valid door_id references
INSERT INTO access_log (id, door_id, user_id, access_time)
VALUES (1, 1, 101, '2024-08-04 09:00'),
       (2, 2, 102, '2024-08-04 10:00'),
       (3, 3, 103, '2024-08-04 11:00'),
       (4, 1, 104, '2024-08-04 13:00'); -- Changed door_id to an existing value

-- Smart Coffee Service
INSERT INTO inventory_item (item, quantity)
VALUES ('MILK', 1000),
       ('WATER', 5000),
       ('COFFEE_BEANS', 2000);

INSERT INTO coffee_order (id, coffee_type)
VALUES (1, 'AMERICANO'),
       (2, 'FLAT_WHITE'),
       (3, 'CORTADO'),
       (4, 'AMERICANO');

-- Smart Meeting Room Service
INSERT INTO room_details (room_id, name, location, status)
VALUES (1, 'Boardroom A', 'Floor 2', 'AVAILABLE'),
       (2, 'Conference Room B', 'Floor 3', 'OCCUPIED'),
       (3, 'Meeting Room C', 'Floor 4', 'AVAILABLE');

-- Corrected booking with valid room_id references
INSERT INTO booking (booking_id, room_id, user_id, time_slot)
VALUES (1, 1, 101, '2024-08-04 14:00-15:00'),
       (2, 2, 102, '2024-08-04 11:00-12:00'),
       (3, 3, 103, '2024-08-05 09:00-10:00'),
       (4, 1, 104, '2024-08-05 13:00-14:00'); -- Changed room_id to an existing value