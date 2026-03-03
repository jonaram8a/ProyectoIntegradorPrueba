package org.integracode.chatapp.bd;
import java.sql.*;

public class ConexionMySQL {
    Connection conn;
    public Connection open() throws Exception {
        String url = "jdbc:mysql://localhost:3306/chatapp";
        String usuario = "root";
        String password = "jonayeder"; // Tu contraseña de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, usuario, password);
        return conn;
    }
    public void close() throws Exception {
        if (conn != null) conn.close();
    }
}