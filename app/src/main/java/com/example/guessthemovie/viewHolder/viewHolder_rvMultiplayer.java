package com.example.guessthemovie.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guessthemovie.R;

public class viewHolder_rvMultiplayer extends RecyclerView.ViewHolder {

    public ImageView imgMulti;
    public ImageView img2Multi;
    public CardView cardViewMulti;
    public RelativeLayout relativeLayoutMulti;
    public TextView txtPuntos;


    public viewHolder_rvMultiplayer(@NonNull View itemView) {
        super(itemView);

        imgMulti = itemView.findViewById(R.id.imgViewImg1Multi);
        img2Multi = itemView.findViewById(R.id.imgViewImg3Multi);
        cardViewMulti = itemView.findViewById(R.id.card_viewMulti);
        relativeLayoutMulti = itemView.findViewById(R.id.layoutABorrarMulti);
        txtPuntos = itemView.findViewById(R.id.txtPuntosDisponibles);

    }
}
