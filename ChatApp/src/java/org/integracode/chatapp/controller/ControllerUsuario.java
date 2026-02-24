/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.integracode.chatapp.controller;

/**
 *
 * @author Raniel025
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.integracode.chatapp.bd.ConexionMySQL;
import org.integracode.chatapp.model.Usuario;

public class ControllerUsuario
{
    public Usuario validate(String nombre, String contrasenia) throws Exception
    {
        String sql = "SELECT * FROM v_usuario WHERE nombreUsuario=? AND contrasenia=?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        Usuario u = null;
        
        pstmt.setString(1, nombre);
        pstmt.setString(2, contrasenia);
        
        rs = pstmt.executeQuery();
        
        if (rs.next())
            u = fill(rs);
        
        rs.close();
        pstmt.close();
        conn.close();
        
        return u;
        
    }
    
    private Usuario fill(ResultSet rs) throws Exception
    {
        Usuario u = new Usuario();
        
        
        
        u.setContrasenia(rs.getString("contrasenia"));
        u.setId(rs.getInt("idUsuario"));
        u.setNombre(rs.getString("nombreUsuario"));
      
        
        return u;
    }
}
