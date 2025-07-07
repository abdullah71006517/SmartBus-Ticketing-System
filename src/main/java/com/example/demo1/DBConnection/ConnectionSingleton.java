package com.example.demo1.DBConnection;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {

    // Final constants for DB config
    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DATABASE = "smartbus";
    private static final String USER = "root";
    private static final String PASSWORD = "abdullah";

    // Simplified URL without extra params
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

    private static Connection connection;
    private static final ConnectionSingleton instance = new ConnectionSingleton();

    private ConnectionSingleton() {
        try {
            // Load driver (optional)
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to create DB connection", e);
        }
    }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get DB connection", e);
        }
        return connection;
    }

    // Optional: method to close connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println(" Database connection closed.");
            } catch (SQLException e) {
                System.err.println(" Failed to close database connection.");
                e.printStackTrace();
            }
        }
        connection = null;
    }
}
