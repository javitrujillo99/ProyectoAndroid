package com.example.proyectoandroid.model;

import java.io.Serializable;

//Implementamos el Serializable para que se pueda enviar una instancia de este objeto a otro sitio
public class Transformacion implements Serializable{

    private String nombre;
    private int foto;

    /**
     * Constructor sin atributos
     */
    public Transformacion() {
    }

    /**
     * Constructor con atributos
     * @param nombre
     * @param foto
     */
    public Transformacion(String nombre, int foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    /**
     * Getters y setters
     */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
