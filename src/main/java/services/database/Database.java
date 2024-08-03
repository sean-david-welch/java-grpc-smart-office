package services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
    private static final String DB_URL = "jdbc:sqlite:smart_access_control.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
            }
        } catch (SQLException e) {
            System.out.println("error with sqlite database connection" + e);
        }
    }
}
