package com.example.guessthemovie.ActivityConRecycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessthemovie.DB.DBPeticiones;
import com.example.guessthemovie.DB.peliculasDB;
import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.R;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

/**
 * Vista compartida para las opciones de Crear peliculas nuevas y modificar peliculas
 * existentes
 */
public class addPelicula extends AppCompatActivity {

    //componentes
    private Button agregar;
    private ImageView img1,img2,img3;
    private TextView txtPistas, txtPeliculaNombre, txtEncabezado;


    //variables globales
    private boolean img1HasChanged = false,img2HasChanged = false,img3HasChanged = false;

    //StringBuilder que arma las pistas
    private StringBuilder cadenaPista;

    //Uri de las imagenes
    private Uri pathimg1 = null, pathimg2 = null,pathimg3 = null;

    //modo de edicion
    private boolean cambioAModificacion;
    private String strIdPelicula,UIDTEMP;

    //Acceso a Base de datos local
    private pelicula pa;
    private peliculasDB pelis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pelicula);
        cadenaPista = new StringBuilder();
        //declaracion de componentes
        img1 = findViewById(R.id.imagen1);
        img2 = findViewById(R.id.imagen2);
        img3 = findViewById(R.id.imagen3);
        txtPistas = findViewById(R.id.etPistas);
        txtEncabezado = findViewById(R.id.tvEncabezado);
        txtPeliculaNombre = findViewById(R.id.txtNombrePelicula);
        agregar = findViewById(R.id.agregarPelicula);
        DBPeticiones db = new DBPeticiones();
        pelis = new peliculasDB(this);
        //En caso de que el intent que activo este Activity contenga algo
        //revisa si debe entrar en el modo de edicion o de creacion
        try {
            cambioAModificacion = false;
            Bundle intent = getIntent().getExtras();
            if (intent != null) {
                if(intent.containsKey("UID")){
                    UIDTEMP = intent.getString("UID");
                }else if(intent.containsKey("cambio")){
                cambioAModificacion = intent.getBoolean("cambio");
                strIdPelicula = intent.getString("idPelicula_Key");
                pa = db.getPeliculaPorID(addPelicula.this,strIdPelicula);
                agregar.setText("Actualizar");
                txtEncabezado.setText("Actualizar");
                flashPoint(pa);
                }

            }
        }catch (Exception e){
            Log.d("ErrorAdd",e.getMessage());
        }


        //Listeners del boton agregar
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();
            }
        });
    }

    /**
     * En caso de que se active el modo de edicion, este metodo lista los datos para cambiarlos
     * @param pa Objeto recibido del Intent
     */
    private void flashPoint(pelicula pa) {

        txtPeliculaNombre.setText(pa.getlStrNombrePelicula());
        txtPistas.setText(pa.getlLstPistas());
        img1.setImageBitmap(pa.getImagen1());

    }

    /**
     * Elige una imagen para el ImageView
     * @param view
     */
    public void selectImageForImageView(View view) {
        cargarImagen(1);
    }

    public void selectImageForImageView2(View view) {
        cargarImagen(2);
    }

    public void selectImageForImageView3(View view) {
        cargarImagen(3);
    }

    /**
     * Permite al usuario seleccionar una foto de su galeria para subirla a la aplicacion
     * @param imageViewID
     */
    private void cargarImagen(int imageViewID) {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent,"Seleccione una aplicacion"),imageViewID);
    }

    /**
     * Toma la imagen obtenida de la seleccion del usuario y la pone en el ImageView respectivo
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
                img1.setImageURI(path);
                pathimg1 = path;
                img1HasChanged = true;
            } else if (requestCode == 2) {
                img2.setImageURI(path);
                pathimg2 = path;
                img2HasChanged = true;
            } else if (requestCode == 3) {
                img3.setImageURI(path);
                pathimg3 = path;
                img3HasChanged = true;
            }
        }
    }

    /**
     * Vuelve a la vista peliculasGuardadasRecyclerView
     * @param view
     */
    public void volver(View view) {

        Intent volver = new Intent(this, peliculasGuardadasRecyclerView.class);
        volver.putExtra("UID",UIDTEMP);
        startActivity(volver);

    }

    /**
     * permite agregar una nueva pista al usuario, separandolas por el carqacter |
     * @param view
     */
    public void armarPista(View view) {

        try{
            String pista = txtPistas.getText().toString().trim();
            if (!pista.isEmpty()) {
                if (cadenaPista.length() > 0) {
                    cadenaPista.append(" | ");
                }
                cadenaPista.append(pista);
                txtPistas.setText("");
                Log.d("pistas",cadenaPista.toString());
            }
        }catch(Exception e){
            Log.d("pistasError",e.getMessage());
        }


    }

    /**
     * En caso de estar en modo de creacion, toma los datos y crea una nueva pelicula
     * En caso de estar en modeo de edicion, toma los datos y actualiza la pelicula seleccionada
     */
    private void obtenerDatos(){

        String nombrePelicula = txtPeliculaNombre.getText().toString().trim();
        String pistaCadena = "";
        String img1String = comprobacion(img1HasChanged,(Drawable) img1.getDrawable());

        Intent intent = new Intent(this, peliculasGuardadasRecyclerView.class);

        if(cadenaPista.toString()==null || cadenaPista.toString().equals("")){
            cadenaPista.append("");
            pistaCadena = cadenaPista.toString();
        }else{
            pistaCadena = cadenaPista.toString();
        }



        if(nombrePelicula.isEmpty()){
            Toast.makeText(addPelicula.this,"Debe darle nombre a la pelicula",Toast.LENGTH_SHORT).show();
        }else {
            DBPeticiones peticiones = new DBPeticiones();
            if(cambioAModificacion){
                cambioAModificacion = false;
                peticiones.UpdateMovie(strIdPelicula,nombrePelicula,img1String,null,null,pistaCadena,addPelicula.this);
            }else{
                peticiones.insertMovie(nombrePelicula,img1String,"","",pistaCadena,addPelicula.this);
            }
            intent.putExtra("UID",UIDTEMP);
            startActivity(intent);

        }


    }

    /**
     * Revisa si un ImageView esta ocupado, en caso de estarlo convierte la imagen a una cadena de
     * texto para guardarla en la base de datos
     * @param comprobar booleano que dice si esta libre (false) o no (true)
     * @param drawable
     * @return
     */
    private String comprobacion(boolean comprobar, Drawable drawable){
        if(comprobar){
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            return convertir_desonvertirBit_a_str.bitmapToBase64(bm);
        }
        return null;
    }


}