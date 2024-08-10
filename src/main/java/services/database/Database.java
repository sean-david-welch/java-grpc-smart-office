package services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:src/main/database/database.db";
    private static final System.Logger logger = System.getLogger(Database.class.getName());

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                logger.log(System.Logger.Level.INFO, "Database connection established.");
            }
        } catch (SQLException e) {
            logger.log(System.Logger.Level.ERROR, "Error with SQLite database connection: {0}", e.getMessage());
        }
    }
}
