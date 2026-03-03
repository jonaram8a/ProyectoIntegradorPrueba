package org.integracode.chatapp.controller;
import org.integracode.chatapp.model.Reciclaje;

public class CalcularReciclaje {
    public void calcular(Reciclaje r) {
        double precio = 0;
        // Lógica de 5to cuatrimestre: Datos basados en los 20 materiales del SQL
        if (r.getMaterial().contains("Cobre")) precio = 135.0;
        else if (r.getMaterial().contains("PET")) precio = 6.5;
        else if (r.getMaterial().contains("Aluminio")) precio = 18.5;
        
        r.setPago(r.getCantidad() * precio);
    }
}