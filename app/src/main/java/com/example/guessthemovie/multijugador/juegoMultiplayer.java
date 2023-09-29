package com.example.guessthemovie.multijugador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessthemovie.ActivityConRecycler.activityJuego;
import com.example.guessthemovie.ActivityConRecycler.peliculasGuardadasRecyclerView;
import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.DB.peliculasDB;
import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;
import com.example.guessthemovie.R;
import com.github.javafaker.Faker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class juegoMultiplayer extends AppCompatActivity implements View.OnClickListener{
    private List<Integer> numerosDisponibles;
    private Random random;

    private List<Integer> numerosDisponiblesS;
    private Random randomS;


    private ProgressBar p;
    private TextView txt,txt2,porcentaje;
    private ImageView img;
    private Button btn1,btn2,btn3;

    private String id="",nombrePeli="",uidtemp="";

    private static String nombreFilm;

    private Handler h = new Handler();

    private peliculasDB pelis;
    private SQLiteDatabase database;
    private pelicula pa;

    private boolean isActive = false,btnPressed = false;
    private int i= 0;
    private int a=0,b=0,c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_multiplayer);

        numerosDisponibles = new ArrayList<>();
        txt = findViewById(R.id.textViewJuegoMulti);
        txt2 = findViewById(R.id.textView2JuegoMulti);
        img = findViewById(R.id.imageViewJuegoMulti);
        p = (ProgressBar) findViewById(R.id.progressBarMulti);
        porcentaje = findViewById(R.id.porcentajeNumMulti);
        btn1 = findViewById(R.id.peli1Multi);
        a = R.id.peli1Multi;
        btn2 = findViewById(R.id.peli2Multi);
        b = R.id.peli2Multi;
        btn3 = findViewById(R.id.peli3Multi);
        c = R.id.peli3Multi;

        try {
            Bundle intent = getIntent().getExtras();
            if (intent != null) {
                id = intent.getString("Film");
                uidtemp = intent.getString("UID");
                llenado(id);

            }
            Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("ErrorAdd",e.getMessage());
        }
//
        try {
            for (int i1 = 0; i1 < 3; i1++) {
                numerosDisponibles.add(i1);
            }
            random = new Random();

            numerosDisponiblesS = new ArrayList<>();
            for (int i2 = 0; i2 < 3; i2++) {
                numerosDisponiblesS.add(i2);
            }
            randomS = new Random();
            asignarNombre();
        }catch(Exception e){
            Log.d("aleatorio",e.getMessage());
        }

        btn1.setOnClickListener(this);

        btn2.setOnClickListener(this);

        btn3.setOnClickListener(this);

        barra(true);
    }
//
    private void llenado(String idTemp){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pelicula2");
        Query query = databaseReference.orderByChild("filmName").equalTo(idTemp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        pelicula2 peli = data.getValue(pelicula2.class);
                        nombreFilm = peli.getFilmName();
                        txt2.setText(peli.getPistas());
                        img.setImageBitmap(convertir_desonvertirBit_a_str.base64ToBitmap(peli.getImg()));

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"No se encontro",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"No se encontron",Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//
//
    private void barra(boolean var) {
        if (var) {
            if (!isActive) {
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (i <= 100) {
                            if(btnPressed){
                                finish();
                                break;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    porcentaje.setText(String.valueOf(100 - i)); // Actualiza el TextView
                                    p.setProgress(i); // Actualiza la ProgressBar
                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            if (i == 100) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Perdiste", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                try {
                                    Thread.sleep(100);
                                    Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
                                    intent.putExtra("UID",uidtemp);
                                    startActivity(intent);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            i++;
                            isActive = true;
                        }
                    }
                });
                hilo.start();
            }
        }
    }

    private void asignarNombre(){

        List<Integer> botones = new ArrayList<>();
        botones.add(0,R.id.peli1Multi);
        botones.add(1,R.id.peli2Multi);
        botones.add(2,R.id.peli3Multi);

        Faker f = new Faker();
        List<String> peli = new ArrayList<>();
        peli.add(0,id);
        peli.add(1,f.harryPotter().character());
        peli.add(2,f.book().title());


        int mm =0;

        for (int i3 = 0; i3 < 3; i3++) {
            mm = obtenerNumeroAleatorioUnico();

            if (mm >= 0) {
                botones.get(mm);
                if(R.id.peli1Multi==botones.get(mm)){
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn1.setText(peli.get(numero));
                }else if(R.id.peli2Multi==botones.get(mm)){
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn2.setText(peli.get(numero));
                }else if(R.id.peli3Multi==botones.get(mm)){
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn3.setText(peli.get(numero));
                }
            }
        }


    }


    private int obtenerNumeroAleatorioUnico() {

        int indiceAleatorio = random.nextInt(numerosDisponibles.size());
        int numeroAleatorio = numerosDisponibles.get(indiceAleatorio);
        numerosDisponibles.remove(indiceAleatorio);

        return numeroAleatorio;
    }

    private int obtenerNumeroAleatorioUnicoS() {
        int indiceAleatorio = randomS.nextInt(numerosDisponiblesS.size());
        int numeroAleatorio = numerosDisponiblesS.get(indiceAleatorio);
        numerosDisponiblesS.remove(indiceAleatorio);

        return numeroAleatorio;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.peli1Multi){
            if(btn1.getText().equals(id)){
                Toast.makeText(getApplicationContext(), "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.peli2Multi){
            if(btn2.getText().equals(id)){
                Toast.makeText(getApplicationContext(), "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.peli3Multi){
            if(btn3.getText().equals(id)){
                Toast.makeText(getApplicationContext(), "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }

}