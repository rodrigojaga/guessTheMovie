package com.example.guessthemovie.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guessthemovie.R;

public class viewHolder_CardItemSG extends RecyclerView.ViewHolder {

    public TextView txtTitle,txtOverView;
    public ImageView ivImgSG;

    public ScrollView scrollView;


    public viewHolder_CardItemSG(@NonNull View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtTitleSG);
        txtOverView = itemView.findViewById(R.id.txtOverviewSG);
        ivImgSG = itemView.findViewById(R.id.ivImgSG);
        scrollView = itemView.findViewById(R.id.scrollView);

    }
}
