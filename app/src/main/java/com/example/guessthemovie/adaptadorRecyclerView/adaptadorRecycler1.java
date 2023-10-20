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
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.R;
import com.example.guessthemovie.viewHolder.viewHolder_Carditem1;

import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Adaptador para el recycler para la vista de la clase peliculasGuardadasRecyclerView
 * utiliza el viewHolder_CardItem1 para listar los componentes
 */
public class adaptadorRecycler1 extends RecyclerView.Adapter<viewHolder_Carditem1> {
    //Variables globales
    List<pelicula> ListaObjeto;
    String idDef;

    /**
     * Constructor que asigna los datos a la lista local
     * @param listaObjeto
     */
    public adaptadorRecycler1(List<pelicula> listaObjeto) {
        ListaObjeto = listaObjeto;
    }

    //Metodos del RecyclerView
    @NonNull
    @Override
    public viewHolder_Carditem1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista;
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        return new viewHolder_Carditem1(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder_Carditem1 holder, int position) {

        //Si en la lista no existe una imagen para el imageView, este tomara una imagen por defecto
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
        //asigna valores
        idDef = ListaObjeto.get(position).getId().trim().toString();
        String pistas = ListaObjeto.get(position).getlLstPistas();
        String nombre = ListaObjeto.get(position).getlStrNombrePelicula();

        //Listener en caso de que una de las imagenes sea presionada
        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(v,position,idDef);
            }
        });

        holder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(v,position,idDef);
            }
        });
        //Este listner activa la vista para la edicion de la pelicula
        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ListaObjeto.get(position).getImagen1();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, addPelicula.class);
                    intent.putExtra("cambio",true);
                    intent.putExtra("idPelicula_Key", idDef);
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
//Fin metodos del RecyclerView

    /**
     * mETODO para eliminar un registro de la base de datos
     * @param adapterPosition
     * @param context
     */
    public void removeItem(int adapterPosition, Context context) {
        String a = ListaObjeto.get(adapterPosition).getId();
        ListaObjeto.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

        DBPeticiones peticiones = new DBPeticiones();
        peticiones.deleteMovie(Integer.parseInt(a),context);

    }

    /**
     * Lleva al la vista de juego con un Intent
     * @param v
     * @param position
     * @param id
     */
    private void handleClick(View v, int position,String id) {
        Context context = v.getContext();
        Intent intent = new Intent(context, activityJuego.class);
        intent.putExtra("idPelicula_Key", id);
        intent.putExtra("UID", player.UID);
        context.startActivity(intent);
    }


}
