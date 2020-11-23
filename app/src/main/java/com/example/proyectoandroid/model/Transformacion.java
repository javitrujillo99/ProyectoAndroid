package com.example.proyectoandroid.model;

import java.io.Serializable;

//Implementamos el Serializable para que se pueda enviar una instancia de este objeto a otro sitio
public class Transformacion implements Serializable{

    private int id;
    private String nombre;
    private Object foto;
    private int id_usuario;


    /**
     * Constructor sin atributos
     */
    public Transformacion() {
    }

    /**
     * Constructor con atributos
     */
    public Transformacion(String nombre, Object foto) {
        this.nombre = nombre;
        this.foto = foto;
    }


    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Object getFoto() {
        return foto;
    }

    public void setFoto(Object foto) {
        this.foto = foto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
