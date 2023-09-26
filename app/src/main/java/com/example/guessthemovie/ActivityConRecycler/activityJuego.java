package com.example.guessthemovie.ActivityConRecycler;

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

import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.DB.peliculasDB;
import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.R;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class activityJuego extends AppCompatActivity implements View.OnClickListener {

    private List<Integer> numerosDisponibles;
    private Random random;

    private List<Integer> numerosDisponiblesS;
    private Random randomS;


    ProgressBar p;
    TextView txt,txt2,porcentaje;
    ImageView img;
    Button btn1,btn2,btn3;

    String id,nombrePeli,uidtemp;

    Handler h = new Handler();

    private peliculasDB pelis;
    private SQLiteDatabase database;
    private pelicula pa;

    boolean isActive = false,btnPressed = false;
    int i= 0;

    int a=0,b=0,c=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        numerosDisponibles = new ArrayList<>();
        txt = findViewById(R.id.textViewJuego);
        txt2 = findViewById(R.id.textView2Juego);
        img = findViewById(R.id.imageViewJuego);
        p = (ProgressBar) findViewById(R.id.progressBar);
        porcentaje = findViewById(R.id.porcentajeNum);
        btn1 = findViewById(R.id.peli1);
        a = R.id.peli1;
        btn2 = findViewById(R.id.peli2);
        b = R.id.peli2;
        btn3 = findViewById(R.id.peli3);
        c = R.id.peli3;
        DBPeticiones db = new DBPeticiones();
        pelis = new peliculasDB(this);
        try {
            Intent intent = getIntent();
            if (intent != null) {
                id = intent.getStringExtra("idPelicula_Key");
                uidtemp = intent.getStringExtra("UID");
                pa = db.getPeliculaPorID(activityJuego.this,id);
                llenado(pa);
            }

        }catch (Exception e){
            Log.d("ErrorAdd",e.getMessage());
        }

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

    private void llenado(pelicula a){
        nombrePeli = (a.getlStrNombrePelicula());
        txt2.setText(a.getlLstPistas());
        img.setImageBitmap(a.getImagen1());
    }



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
                                        Toast.makeText(activityJuego.this, "Perdiste", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                try {
                                    Thread.sleep(100);
                                    Intent intent = new Intent(activityJuego.this, peliculasGuardadasRecyclerView.class);
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
        botones.add(0,R.id.peli1);
        botones.add(1,R.id.peli2);
        botones.add(2,R.id.peli3);

        Faker f = new Faker();
        List<String> peli = new ArrayList<>();
        peli.add(0,nombrePeli);
        peli.add(1,f.harryPotter().character());
        peli.add(2,f.book().title());


        int mm =0;

        for (int i3 = 0; i3 < 3; i3++) {
            mm = obtenerNumeroAleatorioUnico();

            if (mm >= 0) {
                botones.get(mm);
                if(R.id.peli1==botones.get(mm)){
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn1.setText(peli.get(numero));
                }else if(R.id.peli2==botones.get(mm)){
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn2.setText(peli.get(numero));
                }else if(R.id.peli3==botones.get(mm)){
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
        if(v.getId()==R.id.peli1){
            if(btn1.getText().equals(nombrePeli)){
                Toast.makeText(activityJuego.this, "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activityJuego.this, peliculasGuardadasRecyclerView.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(activityJuego.this, "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.peli2){
            if(btn2.getText().equals(nombrePeli)){
                Toast.makeText(activityJuego.this, "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activityJuego.this, peliculasGuardadasRecyclerView.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(activityJuego.this, "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.peli3){
            if(btn3.getText().equals(nombrePeli)){
                Toast.makeText(activityJuego.this, "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activityJuego.this, peliculasGuardadasRecyclerView.class);
                i=0;
                btnPressed = true;
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(activityJuego.this, "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}