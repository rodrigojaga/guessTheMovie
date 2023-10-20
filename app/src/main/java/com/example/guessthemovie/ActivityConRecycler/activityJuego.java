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

/**
 * Clase de Activity Juego en modo local
 */
public class activityJuego extends AppCompatActivity implements View.OnClickListener {

//Variables utiles para asignacion de titulos a botones de forma aleatoria
    private List<Integer> numerosDisponibles;
    private Random random;

    private List<Integer> numerosDisponiblesS;
    private Random randomS;
//Fin

//Variables de diseño de la aplicacion
    ProgressBar p;
    TextView txt,txt2,porcentaje;
    ImageView img;
    Button btn1,btn2,btn3;
    String id,nombrePeli,uidtemp;
    Handler h = new Handler();
//Fin

//Bases de datos locales
    //====SQLite====
    private peliculasDB pelis;
    private SQLiteDatabase database;

//POO pelicula
    private pelicula pa;

//Variables utiles para el funcionamiento correcto de este activity
    boolean isActive = false,btnPressed = false;
    int i= 0;
    int a=0,b=0,c=0;
//FIN


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        numerosDisponibles = new ArrayList<>();
    //Asignacion de valores a los componentes de la interfaz
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
    //FIN

        //Base de datos local
        DBPeticiones db = new DBPeticiones();
        pelis = new peliculasDB(this);

        /*
        Revisa si el intent con el que se llamo al Activity contiene algo
        En caso de contener algo, obtiene el id de la pelicula con su key
        Busca el objeto en la base de datos y crea un Objeto de tipo pelicula para luego mostrarlo
        */
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
        /*FIN*/

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
        //FIN

        //onCLickListeners de los botones que contienen las opciones de respuestas en el juego
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        //Fin

        //Llamada del metodo relacionado al progressBar
        barra(true);
    }

    /**
     * Si el intent que llamo a este Activity contenia algo, toma los datos que coinciden con el asignado
     * y los pone en nombrePeli -> TextView, txt2(Pistas) -> TextView, img -> ImageView
     * @param a es un objeto de tipo pelicula
     */
    private void llenado(pelicula a){
        nombrePeli = (a.getlStrNombrePelicula());
        txt2.setText(a.getlLstPistas());
        img.setImageBitmap(a.getImagen1());
    }


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
        botones.add(0,R.id.peli1);
        botones.add(1,R.id.peli2);
        botones.add(2,R.id.peli3);

        /*
        Utiliza la dependencia de DataFaker para obtener nombre de Harry Potter y de libros aleatorios
        En uno de estos botones estara el nombre correcto de la pelicula
        */
        Faker f = new Faker();
        List<String> peli = new ArrayList<>();
        peli.add(0,nombrePeli);
        peli.add(1,f.harryPotter().character());
        peli.add(2,f.book().title());


        int mm =0;
        //Obtiene un numero aleatorio entre 0 y 2 de las listas que llenamos al principio del Activity
        for (int i3 = 0; i3 < 3; i3++) {
            mm = obtenerNumeroAleatorioUnico();

            if (mm >= 0) {//Busca el id del boton que corresponde a la posicion del numero obtenido anteriormente
                botones.get(mm);
                if(R.id.peli1==botones.get(mm)){//si el id corresponde al boton 1, toma ese boton
                    int numero=0;
                    //Toma otro numero aleatorio entre el 0 y 2 para tomar un nombre aleatorio de la lista peli
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn1.setText(peli.get(numero));//Asigna al boton 1 el nombre de la pelicula, Personaje de Harry Potter o nombre del libro
                }else if(R.id.peli2==botones.get(mm)){//si el id obtenido es del boton 2 asigna a este el nombre aleatorio
                    int numero=0;
                    for (int j = 0; j < 3; j++) {
                        numero = obtenerNumeroAleatorioUnicoS();
                        if (numero >= 0) {
                            break;
                        }
                    }
                    btn2.setText(peli.get(numero));//Asigna al boton 1 el nombre de la pelicula, Personaje de Harry Potter o nombre del libro
                }else if(R.id.peli3==botones.get(mm)){//si el id obtenido es del boton 2 asigna a este el nombre aleatorio
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
        //Este ciclo se repite hasta que ya no hay datos en los Arrays, lo que quiere decir que todos los botones tiene texto

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
     * Devuelve al activity anterior
     * @param v The view that was clicked.
     */
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