package com.example.guessthemovie.POO;

/**
 * Atributos necesarios para interactuar con los datos recibidos de la tabla Users de la base
 * de datos de Firebase
 */
public class player {
//Atributos
    private String idPlayer, profile, name;

    public static String UID;
    public static String NAME;

    /**
     * Constructor de la clase
     * @param idPlayer
     * @param profile
     * @param name
     */
    public player(String idPlayer, String profile, String name) {
        this.idPlayer = idPlayer;
        this.profile = profile;
        this.name = name;

    }

    /**
     * Constructor vacio
     */
    public player() {
    }
    //Inicio metodos get de los atributos de la clase player

    public String getIdPlayer() {
        return idPlayer;
    }

    public String getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }
    //Fin metodos get

    //Inicio metodos set de los atributos de la clase player

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Fin metodos set
}
