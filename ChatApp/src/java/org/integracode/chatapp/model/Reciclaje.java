package org.integracode.chatapp.model;

public class Reciclaje {

    private String material;
    private double cantidad;
    private double puntos;
    private String mensaje;

    public Reciclaje() {}

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPuntos() { return puntos; }
    public void setPuntos(double puntos) { this.puntos = puntos; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
