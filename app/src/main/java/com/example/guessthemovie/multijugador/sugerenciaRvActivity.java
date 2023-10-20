package com.example.guessthemovie.multijugador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.guessthemovie.POO.Movie;
import com.example.guessthemovie.R;
import com.example.guessthemovie.adaptadorRecyclerView.adaptadorRecyclerViewSugerencia;
import com.example.guessthemovie.metodosPublicos.ListaYMetodoDeLlenado;

import java.util.ArrayList;

/**
 * Activity donde estan listadas las peliculas obtenidas de la API
 */
public class sugerenciaRvActivity extends AppCompatActivity {
    //variable global
    private String UIDTEMP;
    ArrayList<Movie> listarPelicula = new ArrayList<>();

    //componentes de la Interfaz grafica
    private RecyclerView rv;
    private LinearLayoutManager layoutManager;
    private adaptadorRecyclerViewSugerencia adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia_rv);
        //Metodos relaxcionados a la implementacion del RecyclerView
        rv = findViewById(R.id.rvSugerencia);

        try{
            rv.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            rv.setLayoutManager(layoutManager);

        }catch (Exception e){

        }
        //Verifica si el intent que llamo a este Activity tiene algo relevante
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            if(intent.containsKey("UID")){
                UIDTEMP = intent.getString("UID");
            }
        }
        for(Movie mo: ListaYMetodoDeLlenado.lista){
            listarPelicula.add(mo);
        }

        adaptador = new adaptadorRecyclerViewSugerencia(listarPelicula);
        rv.setAdapter(adaptador);

    }


    /**
     * regresa al Activity de la clase addFilmMultiplayer
     * @param view
     */
    public void volverCrear(View view) {
        Intent intent = new Intent(getApplicationContext(),addFilmMultiplayer.class);
        intent.putExtra("UID",UIDTEMP);
        startActivity(intent);
    }

}