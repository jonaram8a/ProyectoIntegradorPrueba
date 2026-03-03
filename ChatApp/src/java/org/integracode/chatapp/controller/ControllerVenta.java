package org.integracode.chatapp.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.integracode.chatapp.bd.ConexionMySQL;
import org.integracode.chatapp.model.Reciclaje;
import org.integracode.chatapp.model.Producto;

public class ControllerVenta {
    
    // 1. Guardar la recolección del ciudadano
    public void registrarVenta(Reciclaje r) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        // Buscamos el precio actual del material en la base de datos
        String sqlProd = "SELECT id_producto, precio_compra FROM productos WHERE nombre_material = ?";
        PreparedStatement pstmtProd = conn.prepareStatement(sqlProd);
        pstmtProd.setString(1, r.getMaterial());
        ResultSet rsProd = pstmtProd.executeQuery();
        
        int idProducto = 1;
        double precioCompra = 0.0;
        if(rsProd.next()) {
            idProducto = rsProd.getInt("id_producto");
            precioCompra = rsProd.getDouble("precio_compra");
        }
        
        // Calculamos el total a pagar y lo guardamos en el objeto
        r.setPago(r.getCantidad() * precioCompra);
        
        // Insertamos en la tabla ventas
        String sqlVenta = "INSERT INTO ventas (idUsuario, id_producto, cantidad_kg, pago_usuario, latitud, longitud, estatus) "
                        + "VALUES (?, ?, ?, ?, ?, ?, 'Pendiente')";
        PreparedStatement pstmtVenta = conn.prepareStatement(sqlVenta);
        pstmtVenta.setInt(1, r.getIdUsuario());
        pstmtVenta.setInt(2, idProducto);
        pstmtVenta.setDouble(3, r.getCantidad());
        pstmtVenta.setDouble(4, r.getPago());
        pstmtVenta.setDouble(5, r.getLat());
        pstmtVenta.setDouble(6, r.getLon());
        
        pstmtVenta.executeUpdate();
        
        rsProd.close();
        pstmtProd.close();
        pstmtVenta.close();
        conn.close();
    }
    
    // 2. Obtener las rutas pendientes para el mapa del Administrador
    public List<Reciclaje> obtenerRutas() throws Exception {
        List<Reciclaje> lista = new ArrayList<>();
        String sql = "SELECT v.id_venta, u.nombreUsuario, p.nombre_material, v.cantidad_kg, v.pago_usuario, v.latitud, v.longitud " +
                     "FROM ventas v " +
                     "INNER JOIN usuario u ON v.idUsuario = u.idUsuario " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE v.estatus = 'Pendiente'";
                     
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        while(rs.next()){
            Reciclaje r = new Reciclaje();
            r.setIdVenta(rs.getInt("id_venta"));
            r.setNombreUsuario(rs.getString("nombreUsuario"));
            r.setMaterial(rs.getString("nombre_material"));
            r.setCantidad(rs.getDouble("cantidad_kg"));
            r.setPago(rs.getDouble("pago_usuario"));
            r.setLat(rs.getDouble("latitud"));
            r.setLon(rs.getDouble("longitud"));
            lista.add(r);
        }
        rs.close();
        pstmt.close();
        conn.close();
        return lista;
    }

    // 3. Obtener el catálogo completo de materiales
    public List<Producto> obtenerProductos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_material, precio_compra FROM productos ORDER BY nombre_material ASC";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        while(rs.next()){
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre_material"));
            p.setPrecio(rs.getDouble("precio_compra"));
            lista.add(p);
        }
        rs.close(); 
        pstmt.close(); 
        conn.close();
        return lista;
    }

    // 4. Actualizar el precio de un material desde la Tienda
    public void actualizarPrecio(int idProducto, double nuevoPrecio) throws Exception {
        String sql = "UPDATE productos SET precio_compra = ? WHERE id_producto = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, nuevoPrecio);
        pstmt.setInt(2, idProducto);
        pstmt.executeUpdate();
        pstmt.close(); 
        conn.close();
    }

    // 5. Marcar una recolección como Atendida/Finalizada
    public void atenderVenta(int idVenta) throws Exception {
        String sql = "UPDATE ventas SET estatus = 'Finalizado' WHERE id_venta = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idVenta);
        pstmt.executeUpdate();
        pstmt.close(); 
        conn.close();
    }

    // 6. Verificar si el usuario ya tiene una solicitud sin atender
    public boolean tienePendiente(int idUsuario) throws Exception {
        boolean pendiente = false;
        String sql = "SELECT id_venta FROM ventas WHERE idUsuario = ? AND estatus = 'Pendiente'";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idUsuario);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) { 
            pendiente = true; 
        }
        rs.close(); 
        pstmt.close(); 
        conn.close();
        return pendiente;
    }
}