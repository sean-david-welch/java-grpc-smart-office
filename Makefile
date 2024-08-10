client: src/main/proto/smartCoffee.proto src/main/proto/smartAccess.proto src/main/proto/smartMeeting.proto
    grpcui -plaintext -import-path ./src/main/proto -proto smartCoffee.proto -proto smartAccess.proto -proto smartMeeting.proto localhost:8080

database: src/main/database/tables.sql
	rm -f src/main/database/database.db
	cd src/main/database && sqlite3 database.db < tables.sql
