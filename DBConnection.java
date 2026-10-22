package payroll.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC connection helper.
 * Architecture: Application -> JDBC API (java.sql) -> DriverManager -> MySQL Type-4 driver -> Database.
 */
public class DBConnection {
    private static final String URL =
        "jdbc:mysql://localhost:3306/payroll_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";  // <-- change to your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");   // Step 1: load the driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver (mysql-connector-j jar) not found on classpath", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);   // Step 2: open connection
    }
}
