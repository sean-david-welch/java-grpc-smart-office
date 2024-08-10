client:
	grpcui -plaintext -import-path ./src/main/proto -proto smartCoffee.proto -proto smartAccess.proto -proto smartMeeting.proto localhost:8080

database:
	cd src/main/database && sqlite3 database.db < tables.sql

resetDB:
	cd src/main/database && rm -rf database.db