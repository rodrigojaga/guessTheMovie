package com.example.guessthemovie.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guessthemovie.R;

public class viewHolder_Carditem1 extends RecyclerView.ViewHolder {

    public ImageView img1;
    public ImageView img2;
    public ImageView img3;
    public CardView cardView;
    public RelativeLayout relativeLayout;


    public viewHolder_Carditem1(@NonNull View itemView) {
        super(itemView);

        img1 = itemView.findViewById(R.id.imgViewImg1);
        img2 = itemView.findViewById(R.id.imgViewImg2);
        img3 = itemView.findViewById(R.id.imgViewImg3);
        cardView = itemView.findViewById(R.id.card_view);
        relativeLayout = itemView.findViewById(R.id.layoutABorrar);


    }
}
