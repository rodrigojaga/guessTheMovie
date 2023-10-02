package com.example.guessthemovie.metodosPublicos;

import com.example.guessthemovie.POO.Movie;

import java.util.ArrayList;

public class ListaYMetodoDeLlenado {

    public static ArrayList<Movie> lista = new ArrayList<>();
    public static boolean cambioNuevo = false;
    public static void pelicula(Movie mo){
        lista.add(mo);
    }

}
