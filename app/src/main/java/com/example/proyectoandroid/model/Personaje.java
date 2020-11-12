package com.example.proyectoandroid.model;


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
    private int foto;
    private int fotoCompleta;
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
     * Constructor con atributos con id
     */

    public Personaje(int id, String nombre, String descripcion, String raza, String ataqueEspecial, int foto, int fotoCompleta) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.raza = raza;
        this.ataqueEspecial = ataqueEspecial;
        this.foto = foto;
        this.fotoCompleta = fotoCompleta;
    }

    /**
     * Contructor con atributos sin id
     * @param nombre
     * @param descripcion
     * @param foto
     */

    public Personaje(String nombre, String descripcion, String raza, String ataqueEspecial, int foto, int fotoCompleta) {
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

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getFotoCompleta() {
        return fotoCompleta;
    }

    public void setFotoCompleta(int fotoCompleta) {
        this.fotoCompleta = fotoCompleta;
    }

    public List<Transformacion> getTransformaciones() {
        return transformaciones;
    }

    public void setTransformaciones(List<Transformacion> transformaciones) {
        this.transformaciones = transformaciones;
    }
}
