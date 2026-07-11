package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://10.36.142.82:3307/inventario_db";
    private static final String USERNAME = "admin_inventario";
    private static final String PASSWORD = "secretpassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
