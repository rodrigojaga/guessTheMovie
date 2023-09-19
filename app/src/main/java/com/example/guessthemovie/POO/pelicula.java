package com.example.guessthemovie.POO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class pelicula {
    private String id;
    private Bitmap imagen1,imagen2,imagen3;
    private String lStrNombrePelicula;
    private String lLstPistas;

    public pelicula(String id, Bitmap imagen1, Bitmap imagen2, Bitmap imagen3, String lStrNombrePelicula, String lLstPistas) {
        this.id = id;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
        this.imagen3 = imagen3;
        this.lStrNombrePelicula = lStrNombrePelicula;
        this.lLstPistas = lLstPistas;
    }

    public String getlStrNombrePelicula() {
        return lStrNombrePelicula;
    }

    public void setlStrNombrePelicula(String lStrNombrePelicula) {
        this.lStrNombrePelicula = lStrNombrePelicula;
    }

    public String getlLstPistas() {
        return lLstPistas;
    }

    public void setlLstPistas(String lLstPistas) {
        this.lLstPistas = lLstPistas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getImagen1() {
        return imagen1;
    }

    public void setImagen1(Bitmap imagen1) {
        this.imagen1 = imagen1;
    }

    public Bitmap getImagen2() {
        return imagen2;
    }

    public void setImagen2(Bitmap imagen2) {
        this.imagen2 = imagen2;
    }

    public Bitmap getImagen3() {
        return imagen3;
    }

    public void setImagen3(Bitmap imagen3) {
        this.imagen3 = imagen3;
    }
}
