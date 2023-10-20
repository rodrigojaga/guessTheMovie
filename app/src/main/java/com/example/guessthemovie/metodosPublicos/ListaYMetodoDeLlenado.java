package com.example.guessthemovie.metodosPublicos;

import com.example.guessthemovie.POO.Movie;

import java.util.ArrayList;

/**
 * Clase utilizada para listar los datos recibidos de la Api
 */
public class ListaYMetodoDeLlenado {
    //Variables globales
    public static ArrayList<Movie> lista = new ArrayList<>();
    public static boolean cambioNuevo = false;

    /**
     * metodo que guarda las peliculas en el ArrayList para luego llamarlo
     * @param mo
     */
    public static void pelicula(Movie mo){
        lista.add(mo);
    }

}
