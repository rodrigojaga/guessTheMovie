package com.example.guessthemovie.POO;

/**
 * Atriutos necesarios para interactuar con la tabla puntajes de la base de datos de Firebase
 */
public class puntaje {
//Atributos
    private String nombre;
    private String puntaje;

    /**
     * Contructor
     * @param nombre
     * @param puntaje
     */
    public puntaje(String nombre, String puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public puntaje() {
    }

    //Inicio metodos get de los atributos de la clase puntaje
    public String getNombre() {
        return nombre;
    }

    public String getPuntaje() {
        return puntaje;
    }
//Fin metodos get


    //Inicio metodos set de los atributos de la clase puntaje
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }
    //FIN metodos set
}
