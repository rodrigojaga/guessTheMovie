package com.example.guessthemovie.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.guessthemovie.POO.pelicula;
import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.guessthemovie.metodosPublicos.convertir_desonvertirBit_a_str;

/**
 * Clase que contiene todos los metodos necesarios para conectarse con la base de datos local
 */
public class DBPeticiones {
//Conexion con la base de datos
    private peliculasDB pelis;
    private SQLiteDatabase database;


    /**
     * Permite obtener una plicula registrada en la base de datos unicamente con su id
      * @param context
     * @param id
     * @return
     */
    public pelicula getPeliculaPorID(Context context, String id) {
        pelis = new peliculasDB(context);
        database = pelis.getReadableDatabase();
        pelicula pelicula = null;

        String[] columnas = {
                peliculasDB.ColumnID,
                peliculasDB.ColumnMovieName,
                peliculasDB.ColumnImage1,
                peliculasDB.ColumnImage2,
                peliculasDB.ColumnImage3,
                peliculasDB.ColumnPistas
        };

        String seleccion = peliculasDB.ColumnID + " = ?";
        String[] seleccionArgs = {String.valueOf(id)};

        Cursor cursor = database.query(
                peliculasDB.TableName,
                columnas,
                seleccion,
                seleccionArgs,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String idPelicula = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnID));
                String nombre = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnMovieName));
                Bitmap image1 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage1)));
                Bitmap image2 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage2)));
                Bitmap image3 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage3)));
                String pistas = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnPistas));

                // Crea un objeto Pelicula con los datos obtenidos de la base de datos

                pelicula = new pelicula(idPelicula,image1,image2,image3,nombre,pistas);
            }

            cursor.close();
        }

        database.close();

        return pelicula;
    }

    /**
     * Crea una nueva pelicula en la base de datos, tomando como requerimiento los datos necesarios
     * para hacerlo
     * @param nombrePelicula
     * @param img1
     * @param img2
     * @param img3
     * @param pista
     * @param context
     */
    public void insertMovie(String nombrePelicula, String img1, String img2, String img3, String pista, Context context){
        try {
            pelis = new peliculasDB(context);
            database = pelis.getWritableDatabase();



            ContentValues values = new ContentValues();
            values.put(peliculasDB.ColumnMovieName, nombrePelicula);
            values.put(peliculasDB.ColumnImage1, img1);
            values.put(peliculasDB.ColumnImage2, img2);
            values.put(peliculasDB.ColumnImage3, img3);
            values.put(peliculasDB.ColumnPistas, pista);
            database.insert(peliculasDB.TableName, null, values);
            Toast.makeText(context, "Ingresado correctamente", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.d("Error",e.getMessage());
        }
    }

    /**
     * Lista todas las peliculas que hay en la base de datos y las mete un un ArrayList
     * @param context
     * @return
     */
    public ArrayList<pelicula> getPeliculas(Context context){
        pelis = new peliculasDB(context);
        database = pelis.getWritableDatabase();
        ArrayList<pelicula> listPeliculas = new ArrayList<>();
        String[] projection = {peliculasDB.ColumnID,peliculasDB.ColumnMovieName,peliculasDB.ColumnImage1,peliculasDB.ColumnImage2,peliculasDB.ColumnImage3,peliculasDB.ColumnPistas};
        Cursor cursor = database.query(peliculasDB.TableName,projection,null,null,null,null,null);
        pelicula peli;
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnID));
                String nombre = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnMovieName));
                Bitmap img1 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage1)));
                Bitmap img2 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage2)));
                Bitmap img3 = comprobacion(cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnImage3)));
                String pista = cursor.getString(cursor.getColumnIndex(peliculasDB.ColumnPistas));

                peli = new pelicula(id,img1,img2,img3,nombre,pista);
                listPeliculas.add(peli);
            }while(cursor.moveToNext());
            return listPeliculas;
        }
        cursor.close();
        return null;
    }

    /**
     * Elimina una pelicula de la base de datos unicamente con su Id
     * @param id
     * @param context
     */
    public void deleteMovie(int id,Context context){
        pelis = new peliculasDB(context);
        database = pelis.getWritableDatabase();

        String selection =  peliculasDB.ColumnID +" = ?";
        String[] selectionArguments = {String.valueOf(id)};
        database.delete(pelis.TableName,selection,selectionArguments);
    }

    /**
     * Actualiza una pelicula tomando de los parametros los datos que necesita
     * @param id
     * @param nombrePelicula
     * @param img1
     * @param img2
     * @param img3
     * @param pista
     * @param context
     */
    public void UpdateMovie(String id,String nombrePelicula, String img1, String img2, String img3, String pista, Context context){
        try {
            pelis = new peliculasDB(context);
            database = pelis.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(peliculasDB.ColumnMovieName, nombrePelicula);
            values.put(peliculasDB.ColumnImage1, img1);
            values.put(peliculasDB.ColumnImage2, img2);
            values.put(peliculasDB.ColumnImage3, img3);
            values.put(peliculasDB.ColumnPistas, pista);
            String selection = peliculasDB.ColumnID+"=?";
            String[] selectionArgs = {id};
            database.update(peliculasDB.TableName, values,selection,selectionArgs);
            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Log.d("Error",e.getMessage());
        }
    }

    /**
     * comprueba si una imagen es null, si no lo es, transforma la imagen a un Bitmap
     * @param imgStrAComprobar
     * @return
     */
    private Bitmap comprobacion(String imgStrAComprobar){

        if(imgStrAComprobar.isEmpty()||imgStrAComprobar.equals("")||imgStrAComprobar == null){
            return null;
        }else{
            return convertir_desonvertirBit_a_str.base64ToBitmap(imgStrAComprobar);
        }

    }

    /**
     * Separa una cadena mediante el caracter |
     * @param cadena
     * @return
     */
    private List<String> separarCadena(String cadena) {
        String[] partes = cadena.split("\\|");
        return Arrays.asList(partes);
    }


}
