package com.example.guessthemovie.multijugador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessthemovie.POO.pelicula2;
import com.example.guessthemovie.R;
import com.example.guessthemovie.RealTimeDatabase.daoPelicula;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;
import com.example.guessthemovie.metodosPublicos.varPublicas;
import com.squareup.picasso.Picasso;

/**
 * Clase de la Activity para agregar peliculas en el modo multijugador
 * Esta clase guarda las peliculas en Firebase RealTime Database
 */
public class addFilmMultiplayer extends AppCompatActivity {
//Componentes de la interfaz visual
    private Button btnAgregar;
    private ImageView img;
    private TextView txtTitulo,txtPista;

//variables globales
    private boolean img1HasChanged = false;
    private Uri pathimg1;
    private StringBuilder cadenaPista;
    private daoPelicula dao;
    private String UIDTEMP,NAMETEMP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film_multiplayer);
        //declaracion de componentes
        cadenaPista = new StringBuilder();
        btnAgregar = findViewById(R.id.agregarPelicula1);
        img = findViewById(R.id.imagen11);
        txtTitulo = findViewById(R.id.txtNombrePelicula1);
        txtPista = findViewById(R.id.etPistas1);
        dao = new daoPelicula();

        //Revisa si el Intent que llama a esta Activity contiene algo
        Bundle intent = getIntent().getExtras();
        try {
            if (intent != null) {
                if (intent.containsKey("UID")) {
                    UIDTEMP = intent.getString("UID");
                    NAMETEMP = intent.getString("NAME");
                    if (intent.containsKey("path")) {
                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + intent.getString("path")).into(img);
                        txtTitulo.setText(intent.getString("title"));
                        img1HasChanged = true;
                    }
                }
            }
        }catch (Exception e){

        }
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    obtenerDatos();
                }catch (Exception e){

                }
            }


        });
    }

    /**
     * Comienza el proceso de seleccion de imagenes
     * @param view
     */
    public void selectImageForImageView4(View view) {
        cargarImagen(1);
    }

    /**
     * Permite selccionar una imagen de la galeria
     * @param imageViewID
     */
    private void cargarImagen(int imageViewID) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent,"Seleccione una aplicacion"),imageViewID);
    }

    /**
     * Permite establecer en un ImageView la imagen seleccionada por el usuario desde su galeria
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path = data.getData();
            if (requestCode == 1) {
                img.setImageURI(path);
                pathimg1 = path;
                img1HasChanged = true;
            }
        }
    }

    /**
     * Arma las pistas ingresadas por el usuario, separandolas mediante el caracter |
     * @param view
     */
    public void armarPistaMulti(View view) {

        try{
            String pista = txtPista.getText().toString().trim();
            if (!pista.isEmpty()) {
                if (cadenaPista.length() > 0) {
                    cadenaPista.append(" | ");
                }
                cadenaPista.append(pista);
                txtPista.setText("");
                Log.d("pistas",cadenaPista.toString());
            }
        }catch(Exception e){
            Log.d("pistasError",e.getMessage());
        }

    }

    /**
     * Obtiene los datos de los campos respectivos para crear una nueva pelicula en Firebase
     */
    private void obtenerDatos(){
        try {
            String nombrePelicula = txtTitulo.getText().toString().trim();
            String pistaCadena = "";
            String img1String = comprobacion(img1HasChanged, (Drawable) img.getDrawable());

            Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);

            if (cadenaPista.toString() == null || cadenaPista.toString().equals("")) {
                cadenaPista.append("");
                pistaCadena = cadenaPista.toString();
            } else {
                pistaCadena = cadenaPista.toString();
            }


            if (nombrePelicula.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Debe darle nombre a la pelicula", Toast.LENGTH_SHORT).show();
            } else {
                pelicula2 peli = new pelicula2(varPublicas.sigID, nombrePelicula, pistaCadena, img1String);
                dao.addFilm(peli).addOnSuccessListener(suc -> {
                    Toast.makeText(getApplicationContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                    intent.putExtra("UID", UIDTEMP);
                    startActivity(intent);
                }).addOnFailureListener(er -> {
                    Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                });

            }

        }catch (Exception e){

        }
    }

    /**
     * comprueba si se ingreso una imagen
     * si se ingreso, se transforma a String
     * en caso de que no retorna null
     * @param comprobar
     * @param drawable
     * @return String
     */
    private String comprobacion(boolean comprobar, Drawable drawable){
        if(comprobar){
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            return convertir_desonvertirBit_a_str.bitmapToBase64(bm);
        }
        return null;
    }

    /**
     * Vuelve a la vista peliculasMultiplayerRv
     * @param view
     */
    public void volverMP(View view) {
        Intent intent = new Intent(getApplicationContext(), peliculasMultiplayerRv.class);
        intent.putExtra("UID",UIDTEMP);
        startActivity(intent);
    }

    /**
     * Lleva a la vista de sugerencias de peliculas
     * @param view
     */
    public void verSugerencias(View view) {
        //datos();
        Intent intent = new Intent(getApplicationContext(), sugerenciaRvActivity.class);
        intent.putExtra("UID",UIDTEMP);
        startActivity(intent);
    }


}