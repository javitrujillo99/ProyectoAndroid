package com.example.proyectoandroid.model;


import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Implementa Serializable para poder pasar el objeto a otra activity, que es una clase que sirve solamente
// para especificar que el estado de un objeto instanciado podrá ser escrito o enviado en la red como una trama
// de bytes.
public class Personaje implements Serializable {

    //Atributos del personaje
    private int id;
    private String nombre;
    private String descripcion;
    private Object foto; //Lo declaro con Object porque a veces será integer y otras veces Uri
    private Object fotoCompleta;
    private String raza;
    private String ataqueEspecial;

    //Inicializamos la lista para que no de problemas de NullPointerException
    private List<Transformacion> transformaciones = new ArrayList<Transformacion>();


    /**
     * Constructor sin atributos
     */
    public Personaje() {

    }

    /**
     * Contructor con atributos y foto int
     */

    public Personaje(String nombre, String descripcion, String raza, String ataqueEspecial, Object foto, Object fotoCompleta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.raza = raza;
        this.ataqueEspecial = ataqueEspecial;
        this.foto = foto;
        this.fotoCompleta = fotoCompleta;
    }



    /**
     * Getters y setters
     * @return
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getAtaqueEspecial() {
        return ataqueEspecial;
    }

    public void setAtaqueEspecial(String ataqueEspecial) {
        this.ataqueEspecial = ataqueEspecial;
    }

    public Object getFoto() {
        return foto;
    }

    public void setFoto(Object foto) {
        this.foto = foto;
    }


    public Object getFotoCompleta() {
        return fotoCompleta;
    }

    public void setFotoCompleta(Object fotoCompleta) {
        this.fotoCompleta = fotoCompleta;
    }

    public List<Transformacion> getTransformaciones() {
        return transformaciones;
    }

    public void setTransformaciones(List<Transformacion> transformaciones) {
        this.transformaciones = transformaciones;
    }
}
