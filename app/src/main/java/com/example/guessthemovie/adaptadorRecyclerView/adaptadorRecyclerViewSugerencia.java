package com.example.guessthemovie.adaptadorRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guessthemovie.POO.Movie;
import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.R;
import com.example.guessthemovie.metodosPublicos.ListaYMetodoDeLlenado;
import com.example.guessthemovie.metodosPublicos.varPublicas;
import com.example.guessthemovie.multijugador.addFilmMultiplayer;
import com.example.guessthemovie.multijugador.sugerenciaRvActivity;
import com.example.guessthemovie.viewHolder.viewHolder_CardItemSG;
import com.example.guessthemovie.viewHolder.viewHolder_rvMultiplayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Adaptador para el recycler para la vista de la clase sugerenciasRvActivity
 * utiliza el viewHolder_CardItemSG para listar los componentes
 */
public class adaptadorRecyclerViewSugerencia extends RecyclerView.Adapter<viewHolder_CardItemSG> {

    //Variables globales
    private ArrayList<Movie> listaObject = new ArrayList<>();
    private String idPelicula;
    private Context context;

    /**
     * constructor que toma los datos para llenar la lista
     * @param lista
     */
    public adaptadorRecyclerViewSugerencia(ArrayList<Movie> lista){
        this.listaObject = lista;
    }
//Inicio metodos recyclerView
    @NonNull
    @Override
    public viewHolder_CardItemSG onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_sugerencia,parent,false);
        return new viewHolder_CardItemSG(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder_CardItemSG holder, int position) {
        holder.scrollView.setScrollbarFadingEnabled(false);
        Movie mo = listaObject.get(position);

        holder.txtTitle.setText(mo.getTitle());
        holder.txtOverView.setText(mo.getOverview());
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+mo.getPosterPath()).into(holder.ivImgSG);


        holder.ivImgSG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), addFilmMultiplayer.class);
                intent.putExtra("UID", player.UID);
                intent.putExtra("title",mo.getTitle());
                intent.putExtra("path",mo.getPosterPath());
                ListaYMetodoDeLlenado.lista.clear();
                v.getContext().startActivity(intent);
            }
        });

        holder.txtOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), addFilmMultiplayer.class);
                intent.putExtra("UID", player.UID);
                intent.putExtra("title",mo.getTitle());
                intent.putExtra("path",mo.getPosterPath());
                ListaYMetodoDeLlenado.lista.clear();
                v.getContext().startActivity(intent);
            }
        });

        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), addFilmMultiplayer.class);
                intent.putExtra("UID", player.UID);
                intent.putExtra("title",mo.getTitle());
                intent.putExtra("path",mo.getPosterPath());
                ListaYMetodoDeLlenado.lista.clear();
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaObject.size();
    }
//FIN metodos recyclerView
}
