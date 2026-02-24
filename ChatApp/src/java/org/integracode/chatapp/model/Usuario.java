/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.integracode.chatapp.model;

/**
 *
 * @author Raniel025
 */
public class Usuario
{
    int id;
    String nombre;
    String contrasenia;
   
    
    public Usuario(){}

    public Usuario(int id, String nombre, String contrasenia)
    {
        this.id = id;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
       
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getContrasenia()
    {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia)
    {
        this.contrasenia = contrasenia;
    }

   
}