package com.example.guessthemovie.multijugador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guessthemovie.ActivityConRecycler.peliculasGuardadasRecyclerView;
import com.example.guessthemovie.R;

public class multi_single_player extends AppCompatActivity implements View.OnClickListener {

    private Button btnSingle,btnMulti;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_single_player);
        btnSingle = findViewById(R.id.btnUnJugador);
        btnMulti = findViewById(R.id.btnMultiplayer);
        btnMulti.setOnClickListener(this);
        btnSingle.setOnClickListener(this);

        try{

            Bundle intent = getIntent().getExtras();
            if(intent != null && intent.containsKey("UID")){
                    uid = intent.getString("UID");
            }

        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }

    }

    public void nucazo(View view) {
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnUnJugador){
            Intent intent = new Intent(getApplicationContext(), peliculasGuardadasRecyclerView.class);
            intent.putExtra("UID",uid);
            startActivity(intent);
        }else if(v.getId() == R.id.btnMultiplayer){
            Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
            intent.putExtra("UID",uid);
            startActivity(intent);
        }
    }
}