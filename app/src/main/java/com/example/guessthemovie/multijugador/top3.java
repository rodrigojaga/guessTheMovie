package com.example.guessthemovie.multijugador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.POO.puntaje;
import com.example.guessthemovie.R;
import com.example.guessthemovie.RealTimeDatabase.daoPuntaje;
import com.example.guessthemovie.metodosPublicos.varPublicas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class top3 extends AppCompatActivity {

    TextView txtNombre, txtPuntaje;
    TextView txtNombre2, txtPuntaje2;
    TextView txtNombre3, txtPuntaje3;
    ImageView imgCamp;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top3);

        txtNombre = findViewById(R.id.txtNombreRK);
        txtPuntaje = findViewById(R.id.txtPuntajeRK);
        imgCamp = findViewById(R.id.imgCamp);
        txtNombre2 = findViewById(R.id.txtNombreRK2);
        txtPuntaje2 = findViewById(R.id.txtPuntajeRK2);
        txtNombre3 = findViewById(R.id.txtNombreRK3);
        txtPuntaje3 = findViewById(R.id.txtPuntajeRK3);

        daoPuntaje dao = new daoPuntaje();



        dao.getScore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<puntaje> puntajes = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    puntaje pj = data.getValue(puntaje.class);
                    puntajes.add(pj);

                }

                txtNombre.setText(puntajes.get(2).getNombre());
                txtPuntaje.setText(puntajes.get(2).getPuntaje());

                txtNombre2.setText(puntajes.get(1).getNombre());
                txtPuntaje2.setText(puntajes.get(1).getPuntaje());

                txtNombre3.setText(puntajes.get(0).getNombre());
                txtPuntaje3.setText(puntajes.get(0).getPuntaje());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            if (intent.containsKey("UID")) {
                uid = intent.getString("UID");
            }
        }
    }


    public void volverAntes(View view) {
        Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
        intent.putExtra("UID",uid);
        startActivity(intent);
    }
}