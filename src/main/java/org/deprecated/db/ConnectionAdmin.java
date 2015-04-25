package org.deprecated.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class ConnectionAdmin {
    
    private final static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private final static String DRIVER_URL = "jdbc:mysql://localhost:3306/stw-prediccion";
    private final static String USER = "root";
    private final static String PASSWORD = "toor";

    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
        }
    }

    private ConnectionAdmin() {
    }

    public final static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DRIVER_URL, USER, PASSWORD);
    }
}
