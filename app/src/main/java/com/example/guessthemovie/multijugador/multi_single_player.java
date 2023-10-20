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

/**
 * Submenu donde se le permite al usuario elegir entre Salir del juego, multijugador o SinglePlayer
 */
public class multi_single_player extends AppCompatActivity implements View.OnClickListener {
    //componentes de la interfaz grafica
    private Button btnSingle,btnMulti;
    //variiable global
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_single_player);
        //Declaracion de los componentes
        btnSingle = findViewById(R.id.btnUnJugador);
        btnMulti = findViewById(R.id.btnMultiplayer);
        //Metodo onClick
        btnMulti.setOnClickListener(this);
        btnSingle.setOnClickListener(this);
        //Revisa en caso de que el Intent traiga algo importante
        try{

            Bundle intent = getIntent().getExtras();
            if(intent != null && intent.containsKey("UID")){
                    uid = intent.getString("UID");
            }

        }catch(Exception e){
            Log.d("Error", e.getMessage());
        }

    }

    /**
     * Cierra la aplicacion
     * @param view
     */
    public void nucazo(View view) {
        finishAffinity();
    }

    /**
     * Dependiendo del boton precionado, este lleva al multijugador, singleplayer
     * @param v The view that was clicked.
     */
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