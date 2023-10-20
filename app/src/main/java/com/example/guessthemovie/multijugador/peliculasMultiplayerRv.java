package com.example.guessthemovie.multijugador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.guessthemovie.MainActivity;
import com.example.guessthemovie.POO.Movie;
import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.adaptadorRecyclerView.adaptadorRecyclerViewMulti;
import com.example.guessthemovie.RealTimeDatabase.daoPelicula;
import com.example.guessthemovie.R;
import com.example.guessthemovie.apiConsumo.MyApiClient;
import com.example.guessthemovie.metodosPublicos.varPublicas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Activity donde se muestran listadas las peliculas de la base de datos de Firebase
 */
public class peliculasMultiplayerRv extends AppCompatActivity {
//variables globales
    private String uid,name;

    //Componentes
    private ImageView imgPhoto;
    private Toolbar toolbar;

    //Componentes para Listar de DataBase
    private SwipeRefreshLayout swipeRefreshLayout;
    private adaptadorRecyclerViewMulti adapter;
    private RecyclerView recyclerView;
    private daoPelicula peliculaDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas_multiplayer_rv);
        //Inicializar componentes
        AdeptusAstartes();
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new adaptadorRecyclerViewMulti(this);
        recyclerView.setAdapter(adapter);
        peliculaDao = new daoPelicula();
        //FIN
        loadData();
        //Revisa si el intent que activo este Activiry contiene algo importante
        try{
            Bundle intent = getIntent().getExtras();
            if(intent!=null){

                uid = intent.getString("UID");
                player.UID = uid;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userReference = databaseReference.child("Users").child(intent.getString("UID"));
                //Busca al usuario en la tabla Users de la base de datos y trae su foto de perfil  y su nombre
                try {
                    Task<DataSnapshot> user = userReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            player user = dataSnapshot.getValue(player.class);
                            Picasso.get().load(user.getProfile()).into(imgPhoto);
                            name = user.getName();
                            player.NAME = user.getName();

                        }
                    });
                }catch (Exception Ignored){

                }

            }
            //Lleva a la vista del top 3
            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(getApplicationContext(), top3.class);
                    intent3.putExtra("UID",uid);
                    startActivity(intent3);
                }
            });
        }catch(Exception e){
            Log.d("Error",e.getMessage());
        }

    }

    /**
     * cargar las peliculas de la base de datos al ArrayList y enviarlas al Adapter
     */
    private void loadData(){
        peliculaDao.getFilms().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<pelicula2> peliculas = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    pelicula2 peli = data.getValue(pelicula2.class);
                    peliculas.add(peli);
                    varPublicas.sigID=String.valueOf(Integer.parseInt(peli.getId())+1);
                }
                adapter.setItems(peliculas);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Inicializa los componentes de la interfaz grafica
     */
    private void AdeptusAstartes(){
        imgPhoto = findViewById(R.id.ImgProfilePhMulti);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.recyclerPeliculasGuardadasMulti);
        toolbar = findViewById(R.id.toolbarMulti);
    }


    //Inicio Metodos toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.multiplayer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.item2Multi){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId() == R.id.itemAgregarMulti){
            datos();
            Intent volver = new Intent(this, addFilmMultiplayer.class);
            volver.putExtra("UID",uid);
            volver.putExtra("NAME",name);
            startActivity(volver);
        }else if(item.getItemId()==R.id.itemMenuJuegoMulti){
            Intent volver = new Intent(this, multi_single_player.class);
            volver.putExtra("UID",uid);
            volver.putExtra("NAME",name);
            startActivity(volver);
        }

        return super.onOptionsItemSelected(item);
    }
    //Fin Metodos toolbar

    /**
     * Hace las peticiones a la api, y deja los datos esperando en la lIsta de la clase ListaYMetodoDeLlenado
     * para hacer mas eficiente el llamado de todos estos datos
     * Pero aun no se utilizan los datos obtenidos de la API
     */
    private void datos(){

        MyApiClient.makeApiRequest(this, new MyApiClient.MyApiCallback<Movie>() {
            @Override
            public void onSuccess(Movie response) {
                // Procesar la respuesta exitosa aquí
                Toast.makeText(getApplicationContext(), "Llegada 1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }
}