package services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:src/main/database/database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                // Initialize the database schema if necessary
                System.out.println("Database connection established.");
            }
        } catch (SQLException e) {
            System.out.println("Error with SQLite database connection: " + e.getMessage());
        }
    }
}
