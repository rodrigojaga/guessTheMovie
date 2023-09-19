package com.example.guessthemovie.adaptadorRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guessthemovie.ActivityConRecycler.activityJuego;
import com.example.guessthemovie.ActivityConRecycler.addPelicula;
import com.example.guessthemovie.ActivityConRecycler.peliculasGuardadasRecyclerView;
import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.R;
import com.example.guessthemovie.viewHolder.viewHolder_Carditem1;

import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class adaptadorRecycler1 extends RecyclerView.Adapter<viewHolder_Carditem1> {

    List<pelicula> ListaObjeto;

    public adaptadorRecycler1(List<pelicula> listaObjeto) {
        ListaObjeto = listaObjeto;
    }

    @NonNull
    @Override
    public viewHolder_Carditem1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        return new viewHolder_Carditem1(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder_Carditem1 holder, int position) {


        if(ListaObjeto.get(position).getImagen1()==null ){

            holder.img1.setImageResource(R.drawable.noellelogo);

        }else{
            holder.img1.setImageBitmap(ListaObjeto.get(position).getImagen1());
        }

        if(ListaObjeto.get(position).getImagen2()==null){
            holder.img2.setImageResource(R.drawable.noellelogo);

        }else{
            holder.img2.setImageBitmap(ListaObjeto.get(position).getImagen2());
        }

        if(ListaObjeto.get(position).getImagen3()==null){
            holder.img3.setImageResource(R.drawable.baseline_update_24);

        }else{
            holder.img3.setImageBitmap(ListaObjeto.get(position).getImagen3());
        }

        String id = ListaObjeto.get(position).getId();
        String pistas = ListaObjeto.get(position).getlLstPistas();
        String nombre = ListaObjeto.get(position).getlStrNombrePelicula();


        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(v,position,id);
            }
        });

        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(v,position,id);
            }
        });
        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ListaObjeto.get(position).getImagen1();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, addPelicula.class);
                    intent.putExtra("idPelicula_Key", id);
                    context.startActivity(intent);

                }catch (Exception e){
                    Log.d("errorbtn",e.getMessage());
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return ListaObjeto.size();
    }

    public void removeItem(int adapterPosition, Context context) {
        String a = ListaObjeto.get(adapterPosition).getId();
        ListaObjeto.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

        DBPeticiones peticiones = new DBPeticiones();
        peticiones.deleteMovie(Integer.parseInt(a),context);

    }

    private void handleClick(View v, int position,String id) {
        Context context = v.getContext();
        Intent intent = new Intent(context, activityJuego.class);
        intent.putExtra("idPelicula_Key", id);
        context.startActivity(intent);
    }


}
