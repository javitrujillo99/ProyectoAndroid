package com.example.proyectoandroid.model;


public class Personaje {

    //Atributos del personaje
    private int id;
    private String nombre;
    private String descripcion;
    private Object foto; //Lo declaro con Object porque a veces será integer y otras veces Uri
    private Object fotoCompleta;
    private String raza;
    private String ataqueEspecial;

    //El personaje tiene dentro una lista de transformaciones, pero lo hago con bases de datos


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

}
