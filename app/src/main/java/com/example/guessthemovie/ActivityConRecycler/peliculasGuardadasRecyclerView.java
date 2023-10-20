package com.example.guessthemovie.ActivityConRecycler;

import androidx.annotation.NonNull;
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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.DB.peliculasDB;
import com.example.guessthemovie.MainActivity;
import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.R;
import com.example.guessthemovie.adaptadorRecyclerView.adaptadorRecycler1;
import com.example.guessthemovie.multijugador.multi_single_player;
import com.example.guessthemovie.touchHelper.RecyclerItemTouchHelper;
import com.example.guessthemovie.viewHolder.viewHolder_Carditem1;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Activity de listado de las peliculas creadas de forma local
 */
public class peliculasGuardadasRecyclerView extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    //Variables globales
    static ArrayList<pelicula> listarPelicula = new ArrayList<>();
    private RecyclerView rvListarPeliculas;
    LinearLayoutManager layout;
    adaptadorRecycler1 adaptador;
    private String uid;

    //Conexion con base de datos Local
    private peliculasDB pelis;
    private SQLiteDatabase database;

    //Componentes de la vista
    private Toolbar toolbar;
    private ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peliculas_guardadas_recycler_view);
        //Metodos del recycler
        Log.d("errorInciio","Empezo la app");
        pelis = new peliculasDB(this);
        database = pelis.getWritableDatabase();
        rvListarPeliculas = findViewById(R.id.recyclerPeliculasGuardadas);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        imgPhoto = findViewById(R.id.ImgProfilePh);
        try{
            rvListarPeliculas.setHasFixedSize(true);
            layout = new LinearLayoutManager(getApplicationContext());
            rvListarPeliculas.setLayoutManager(layout);
            DBPeticiones dbp = new DBPeticiones();
            listarPelicula.clear();
            //Llena el recycler en caso de que existan datos
            for(pelicula det: dbp.getPeliculas(this)){
                lista(det);
            }
            adaptador = new adaptadorRecycler1(listarPelicula);
            rvListarPeliculas.setAdapter(adaptador);
            //Le pone un itemtoch al recycler para poder eliminar datos deslizando
            ItemTouchHelper.SimpleCallback simpleCallback =
                    new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,peliculasGuardadasRecyclerView.this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListarPeliculas);
        }catch (Exception Ignored){
            Log.d("errorInciio",Ignored.getMessage());}
        //FIN recycler

        //Revisa si el intent que llamo a este Activity contiene algo
        try{
            Bundle intent = getIntent().getExtras();
            if(intent!=null){

                uid = intent.getString("UID");
                player.UID = uid;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userReference = databaseReference.child("Users").child(intent.getString("UID"));
                Task<DataSnapshot> user = userReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    //Asigna lo foto de perfil del usuario a la aplicacion
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        player user = dataSnapshot.getValue(player.class);
                        Picasso.get().load(user.getProfile()).into(imgPhoto);
                    }
                });

            }
        }catch(Exception e){
            Log.d("Error",e.getMessage());
        }


    }

    //Metodos para mostrar el ToolBar
    //Esto siempre debe ser asi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.item2){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId() == R.id.itemAgregar){
            Intent volver = new Intent(this, addPelicula.class);
            volver.putExtra("UID",uid);
            startActivity(volver);
        }else if(item.getItemId()==R.id.itemMenuJuego){
            Intent volver = new Intent(this, multi_single_player.class);
            volver.putExtra("UID",uid);
            startActivity(volver);
        }

        return super.onOptionsItemSelected(item);
    }
    //Fin metodos ToolBar

    /**
     * Metodo para llnear la lista con las peliculas existentes
     * @param peli
     */
    private void lista(pelicula peli){
        listarPelicula.add(peli);
    }

    /**
     * Asigna la caracteristica de deslizar al Recycler
     * utilizado para eliminar registros
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof viewHolder_Carditem1){
            adaptador.removeItem(viewHolder.getAdapterPosition(),this);
        }
    }



}