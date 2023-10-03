package com.example.guessthemovie.RealTimeDatabase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.guessthemovie.POO.pelicula2;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class daoPelicula {

    private DatabaseReference databaseReference;

    public daoPelicula() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(pelicula2.class.getSimpleName());

    }

    public Task<Void> addFilm(pelicula2 peli2){
        return databaseReference.push().setValue(peli2);
    }

    public Query getFilms(){
        return databaseReference.orderByKey();
    }
}
