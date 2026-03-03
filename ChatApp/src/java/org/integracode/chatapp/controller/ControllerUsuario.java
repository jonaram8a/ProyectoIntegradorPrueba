package org.integracode.chatapp.controller;

import java.sql.*;
import org.integracode.chatapp.bd.ConexionMySQL;
import org.integracode.chatapp.model.Usuario;

public class ControllerUsuario {
    public Usuario validate(String nombre, String contrasenia) throws Exception {
        String sql = "SELECT * FROM v_usuario WHERE nombreUsuario=? AND contrasenia=?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setString(1, nombre);
        pstmt.setString(2, contrasenia);
        
        ResultSet rs = pstmt.executeQuery();
        Usuario u = null;
        
        if (rs.next()) {
            u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombreUsuario"));
            u.setContrasenia(rs.getString("contrasenia"));
            u.setIdRol(rs.getInt("idRol")); // Extraemos el rol del RS
        }
        
        rs.close();
        pstmt.close();
        conn.close();
        return u;
    }
}