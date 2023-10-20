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
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.POO.puntaje;
import com.example.guessthemovie.RealTimeDatabase.daoPuntaje;
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

/**
 * Activity de juego
 */
public class juegoMultiplayer extends AppCompatActivity implements View.OnClickListener{
    //Variables utiles para asignacion de titulos a botones de forma aleatoria
    private List<Integer> numerosDisponibles;
    private Random random;

    private List<Integer> numerosDisponiblesS;
    private Random randomS;
//FIN

//Variables de componentes de la aplicacion
    private ProgressBar p;
    private TextView txt,txt2,porcentaje;
    private ImageView img;
    private Button btn1,btn2,btn3;
//Fin

//variables Globales
    private String id="",nombrePeli="",uidtemp="";

    private static String nombreFilm;

    private Handler h = new Handler();

    private boolean isActive = false,btnPressed = false;
    private int i= 0;
    private int a=0,b=0,c=0;

    private daoPuntaje dao;
//FIN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_multiplayer);

        numerosDisponibles = new ArrayList<>();
        //Asignacion de valores a los componentes de la interfaz
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
        //FIN

        //instancia de la clase que se conecta con la base de datos de Firebase
        dao = new daoPuntaje();

        //revisa si el Intent que llamo a este Activity contiene algo
        try {
            Bundle intent = getIntent().getExtras();
            if (intent != null) {
                id = intent.getString("Film");
                uidtemp = intent.getString("UID");
                llenado(id);

            }

        }catch (Exception e){
            Log.d("ErrorAdd",e.getMessage());
        }
//Llena los ArrayList con numeros del 0 al 3 para luego asignar nombres randoms a los Botones
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
//onCLickListeners de los botones que contienen las opciones de respuestas en el juego
        btn1.setOnClickListener(this);

        btn2.setOnClickListener(this);

        btn3.setOnClickListener(this);
//Llamada del metodo relacionado al progressBar
        barra(true);
    }

    /**
     * Si el intent contenia algo, tomaba los datos y los en la base de datos de Firebase para luego
     * @param idTemp
     */
    private void llenado(String idTemp){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pelicula2");
        Query query = databaseReference.orderByChild("filmName").equalTo(idTemp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //Si los datos existe, los trae de la base de datos de Firebase y los pone donde corresponde
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

    /**
     * Metodo que Activa el contador y el progressBar que mide el timpo que le queda al usuario
     * para completar el juego.
     * En caso de perder, saldra el mensaje de que el usuario Perdio
     * @param var sirve para activar este metodo
     */
    private void barra(boolean var) {
        if (var) {
            if (!isActive) {
                //Inicia un nuevo proceso paralelo en el hilo principal de la aplicacion
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
                                Thread.sleep(100);//hace esperar al hilo
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
                hilo.start();//comienza el hilo
            }
        }
    }
    /**
     * Este metodo toma los 3 botones de la interfaz y los mete en un ArrayList, para luego asignarle un
     * nombre de pelicula aleatoria
     */
    private void asignarNombre(){
//Mete los botones en el ArrayList
        List<Integer> botones = new ArrayList<>();
        botones.add(0,R.id.peli1Multi);
        botones.add(1,R.id.peli2Multi);
        botones.add(2,R.id.peli3Multi);

        /*
        Utiliza la dependencia de DataFaker para obtener nombre de Harry Potter y de libros aleatorios
        En uno de estos botones estara el nombre correcto de la pelicula
        */
        Faker f = new Faker();
        List<String> peli = new ArrayList<>();
        peli.add(0,id);
        peli.add(1,f.harryPotter().character());
        peli.add(2,f.book().title());


        int mm =0;
//Obtiene un numero aleatorio entre 0 y 2 de las listas que llenamos al principio del Activity
        for (int i3 = 0; i3 < 3; i3++) {
            mm = obtenerNumeroAleatorioUnico();

            if (mm >= 0) {//Busca el id del boton que corresponde a la posicion del numero obtenido anteriormente
                botones.get(mm);
                if(R.id.peli1Multi==botones.get(mm)){//si el id corresponde al boton 1, toma ese boton
                    int numero=0;
                    //Toma otro numero aleatorio entre el 0 y 2 para tomar un nombre aleatorio de la lista peli
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn1.setText(peli.get(numero));//Asigna al boton 1 el nombre de la pelicula, Personaje de Harry Potter o nombre del libro
                }else if(R.id.peli2Multi==botones.get(mm)){//si el id obtenido es del boton 2 asigna a este el nombre aleatorio
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn2.setText(peli.get(numero));//Asigna al boton 1 el nombre de la pelicula, Personaje de Harry Potter o nombre del libro
                }else if(R.id.peli3Multi==botones.get(mm)){//si el id obtenido es del boton 2 asigna a este el nombre aleatorio
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn3.setText(peli.get(numero));//Asigna al boton 1 el nombre de la pelicula, Personaje de Harry Potter o nombre del libro
                }
            }
        }


    }


    /**
     * Obtiene un numero aleatorio del ArrayList numerosDisponibles
     * Este metodo se usa en el for para tomar el id de un boton al azar
     * @return int
     *
     */
    private int obtenerNumeroAleatorioUnico() {

        int indiceAleatorio = random.nextInt(numerosDisponibles.size());
        int numeroAleatorio = numerosDisponibles.get(indiceAleatorio);
        numerosDisponibles.remove(indiceAleatorio);

        return numeroAleatorio;
    }

    /**
     * Obtiene un numero aleatorio del ArrayList numerosDisponiblesS
     * Este metodo se usa en el for para tomar un texto al azar
     * @return int
     *
     */
    private int obtenerNumeroAleatorioUnicoS() {
        int indiceAleatorio = randomS.nextInt(numerosDisponiblesS.size());
        int numeroAleatorio = numerosDisponiblesS.get(indiceAleatorio);
        numerosDisponiblesS.remove(indiceAleatorio);

        return numeroAleatorio;
    }

    /**
     * Identifica que boton fue presionado, asi como tambien hace los procesos en caso de que
     * se haya presionado el boton correcto para ganar
     * Devuelve al activity anterior y suma puntos al usuario que respondio bien
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.peli1Multi){
            if(btn1.getText().equals(id)){
                Toast.makeText(getApplicationContext(), "HAS GANADO!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
                i=0;
                btnPressed = true;
                dao.addOrUpdateScore(new puntaje(player.NAME,"100"));
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
                dao.addOrUpdateScore(new puntaje(player.NAME,"100"));
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
                dao.addOrUpdateScore(new puntaje(player.NAME,"100"));
                intent.putExtra("UID",uidtemp);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
    }





}