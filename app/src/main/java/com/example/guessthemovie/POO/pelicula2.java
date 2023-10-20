package com.example.guessthemovie.POO;

/**
 * Atributos necesarios para la interaccion de los datos almacenados en base de datos de Firebase
 */
public class pelicula2 {
//atributos de la clase pelicula2
    private String id;
    private String filmName;
    private String pistas;
    private String img;

    /**
     * Constructor de la clase
     * @param id
     * @param filmName
     * @param pistas
     * @param img
     */
    public pelicula2(String id, String filmName, String pistas, String img) {
        this.id = id;
        this.filmName = filmName;
        this.pistas = pistas;
        this.img = img;
    }

    public pelicula2() {
    }

    //Inicio metodos get de los atributos de la clase pelicula2
    public String getId() {
        return id;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getPistas() {
        return pistas;
    }

    public String getImg() {
        return img;
    }
    //Fin metodos get

    //Inicio metodos set de los atributos de la clase pelicula2

    public void setId(String id) {
        this.id = id;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public void setPistas(String pistas) {
        this.pistas = pistas;
    }

    public void setImg(String img) {
        this.img = img;
    }
    //Fin metodos set
}
