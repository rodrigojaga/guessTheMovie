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

public class peliculasGuardadasRecyclerView extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    static ArrayList<pelicula> listarPelicula = new ArrayList<>();
    private RecyclerView rvListarPeliculas;
    LinearLayoutManager layout;
    adaptadorRecycler1 adaptador;

    private peliculasDB pelis;
    private SQLiteDatabase database;

    private Toolbar toolbar;
    private ImageView imgPhoto;


    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peliculas_guardadas_recycler_view);

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

        try{
            Bundle intent = getIntent().getExtras();
            if(intent!=null){

                uid = intent.getString("UID");
                player.UID = uid;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userReference = databaseReference.child("Users").child(intent.getString("UID"));
                Task<DataSnapshot> user = userReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        player user = dataSnapshot.getValue(player.class);
                        Picasso.get().load(user.getProfile()).into(imgPhoto);
                        //textView.setText(user.getName());
                        //Picasso.get().load(user.getProfile()).into(imageView);
                    }
                });

            }
        }catch(Exception e){
            Log.d("Error",e.getMessage());
        }


    }

    //ToolBar
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

    private void lista(pelicula peli){
        listarPelicula.add(peli);
    }




    //adicionales


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