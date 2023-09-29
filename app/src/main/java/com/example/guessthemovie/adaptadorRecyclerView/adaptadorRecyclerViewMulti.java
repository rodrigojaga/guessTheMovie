package com.example.guessthemovie.adaptadorRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.R;
import com.example.guessthemovie.multijugador.juegoMultiplayer;
import com.example.guessthemovie.viewHolder.viewHolder_rvMultiplayer;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

import java.util.ArrayList;


public class adaptadorRecyclerViewMulti extends RecyclerView.Adapter<viewHolder_rvMultiplayer> {

    private ArrayList<pelicula2> listaObject = new ArrayList<>();
    private String idPelicula;
    private Context context;

    public adaptadorRecyclerViewMulti(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<pelicula2> lista){
        listaObject.addAll(lista);
    }

    @NonNull
    @Override
    public viewHolder_rvMultiplayer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_multiplayer,parent,false);
        return new viewHolder_rvMultiplayer(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder_rvMultiplayer holder, int position) {
        pelicula2 peli = listaObject.get(position);
        holder.imgMulti.setImageBitmap(convertir_desonvertirBit_a_str.base64ToBitmap(peli.getImg()));

        holder.img2Multi.setImageResource(R.drawable.noellelogo);

        holder.imgMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Prueba",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), juegoMultiplayer.class);
                intent.putExtra("UID",player.UID);
                intent.putExtra("Film",peli.getFilmName());
                context.startActivity(intent);

            }
        });

        holder.img2Multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Prueba",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), juegoMultiplayer.class);
                intent.putExtra("UID",player.UID);
                intent.putExtra("Film",peli.getFilmName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaObject.size();
    }
}