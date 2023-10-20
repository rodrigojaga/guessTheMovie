package com.example.guessthemovie.RealTimeDatabase;

import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.POO.puntaje;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Clase que interactua directamente con la tabla puntaje de la base de datos de firebase
 */
public class daoPuntaje {

    private DatabaseReference databaseReference;

    /**
     * Realiza la conexion con la tabla puntaje
     */
    public daoPuntaje() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(puntaje.class.getSimpleName());

    }

    /**
     * Crea un nuevo record o actualiza el existente con el nuevo puntaje obtenido
     * @param score
     * @return
     */
    public Task<Void> addOrUpdateScore(puntaje score) {
        DatabaseReference userReference = databaseReference.child(score.getNombre());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    puntaje existingScore = dataSnapshot.getValue(puntaje.class);
                    String updatedScore = String.valueOf(Integer.parseInt(existingScore.getPuntaje()) + 100);
                    existingScore.setPuntaje(updatedScore);
                    userReference.setValue(existingScore);
                } else {

                    databaseReference.child(score.getNombre()).setValue(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }

    /**
     * Obtiene los 3 punteos mas altos
     * @return
     */
    public Query getScore(){
        return databaseReference.orderByChild("puntaje").limitToLast(3);
    }

}
