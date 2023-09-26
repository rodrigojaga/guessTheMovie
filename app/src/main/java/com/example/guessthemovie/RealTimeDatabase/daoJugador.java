package com.example.guessthemovie.RealTimeDatabase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.guessthemovie.POO.player;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class daoJugador {

    private DatabaseReference databaseReference;

    public daoJugador(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(player.class.getSimpleName());
    }

    public Task<Void> add(player con){
        return databaseReference.push().setValue(con);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete (String key){
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key){
        if(key == null){
            return databaseReference.orderByKey().limitToFirst(8);
        }else{
            return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
        }

    }

}
