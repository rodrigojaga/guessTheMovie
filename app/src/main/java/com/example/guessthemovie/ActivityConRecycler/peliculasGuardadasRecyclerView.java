package com.example.guessthemovie.ActivityConRecycler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.DB.peliculasDB;
import com.example.guessthemovie.MainActivity;
import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.R;
import com.example.guessthemovie.adaptadorRecyclerView.adaptadorRecycler1;
import com.example.guessthemovie.touchHelper.RecyclerItemTouchHelper;
import com.example.guessthemovie.viewHolder.viewHolder_Carditem1;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

import java.util.ArrayList;

public class peliculasGuardadasRecyclerView extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    static ArrayList<pelicula> listarPelicula = new ArrayList<>();
    private RecyclerView rvListarPeliculas;
    LinearLayoutManager layout;
    adaptadorRecycler1 adaptador;

    private peliculasDB pelis;
    private SQLiteDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peliculas_guardadas_recycler_view);

        Log.d("errorInciio","Empezo la app");
        pelis = new peliculasDB(this);
        database = pelis.getWritableDatabase();
        rvListarPeliculas = findViewById(R.id.recyclerPeliculasGuardadas);

        try{
            rvListarPeliculas.setHasFixedSize(true);
            layout = new LinearLayoutManager(getApplicationContext());
            rvListarPeliculas.setLayoutManager(layout);
            DBPeticiones dbp = new DBPeticiones();
            listarPelicula.clear();

            for(pelicula det: dbp.getPeliculas(this)){
                lista(det);
            }


            adaptador = new adaptadorRecycler1(listarPelicula);
            rvListarPeliculas.setAdapter(adaptador);

            ItemTouchHelper.SimpleCallback simpleCallback =
                    new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,peliculasGuardadasRecyclerView.this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListarPeliculas);
        }catch (Exception Ignored){
            Log.d("errorInciio",Ignored.getMessage());}


    }

    private void lista(pelicula peli){
        listarPelicula.add(peli);
    }




    //adicionales
    public void siguienteAgregarPeli(View view){
        Intent volver = new Intent(this, addPelicula.class);
        startActivity(volver);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof viewHolder_Carditem1){
            adaptador.removeItem(viewHolder.getAdapterPosition(),this);
        }
    }

    public void inicio(View view) {
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
    }
}