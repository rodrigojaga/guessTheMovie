package com.example.guessthemovie.POO;

public class pelicula2 {

    private String id;
    private String filmName;
    private String pistas;
    private String img;

    public pelicula2(String id, String filmName, String pistas, String img) {
        this.id = id;
        this.filmName = filmName;
        this.pistas = pistas;
        this.img = img;
    }

    public pelicula2() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getPistas() {
        return pistas;
    }

    public void setPistas(String pistas) {
        this.pistas = pistas;
    }
}
