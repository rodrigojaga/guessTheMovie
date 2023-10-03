package com.example.guessthemovie.POO;

public class player {

    private String idPlayer, profile, name;

    public static String UID;
    public static String NAME;

    public player(String idPlayer, String profile, String name) {
        this.idPlayer = idPlayer;
        this.profile = profile;
        this.name = name;

    }

    public player() {
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
