package com.example.guessthemovie.RealTimeDatabase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.guessthemovie.POO.pelicula2;
import com.google.firebase.database.Query;

import java.util.HashMap;

/**
 * clase que interactua con la base de datos de Firebase
 * concretamenre con la tabla pelicula2
 */
public class daoPelicula {

    private DatabaseReference databaseReference;

    /**
     * Constructor que establece la conexion especifica con la tabla pelicula2
     */
    public daoPelicula() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(pelicula2.class.getSimpleName());

    }

    /**
     * Agrega una nueva pelicula a la base de datos en Firebase
     * @param peli2
     * @return
     */
    public Task<Void> addFilm(pelicula2 peli2){
        return databaseReference.push().setValue(peli2);
    }

    /**
     * Obtiene todos las peliculas creadas y guardadas en la base de datos
     * @return
     */
    public Query getFilms(){
        return databaseReference.orderByKey();
    }
}
