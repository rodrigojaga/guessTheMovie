package com.example.guessthemovie.RealTimeDatabase;

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

public class daoPuntaje {

    private DatabaseReference databaseReference;

    public daoPuntaje() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(puntaje.class.getSimpleName());

    }

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


//    public Task<Void> addScore(puntaje score) {
//        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(puntaje.class.getSimpleName());
//        DatabaseReference scoreReference = databaseReference1.child(score.getNombre());
//
//        // Verificar si el nodo con el nombre ya existe en la base de datos
//        scoreReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    // El puntaje con el mismo nombre ya existe en la base de datos, puedes manejar esto si es necesario
//                } else {
//                    // El puntaje no existe, entonces lo agregamos
//                    DatabaseReference newScoreReference = databaseReference1.push();
//                    newScoreReference.setValue(score);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Maneja errores si es necesario
//            }
//        });
//        return null;
//    }
//
//    public Query getFilms(){
//        return databaseReference.orderByKey();
//    }

}
