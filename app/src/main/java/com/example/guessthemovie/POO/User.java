package com.example.guessthemovie.POO;

import java.util.regex.Pattern;

/**
 * Atributos necesarios para la correcta interaccion con la tabla usuarios de la base de datos local
 */
public class User {
    //Atributos
    private String userName;
    private String password;
    private boolean isAuthenticated;

    /**
     * Constructor de clase
     * @param userName
     * @param password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isAuthenticated = false;
    }

    /**
     * Constructor vacio
     */
    public User() {
    }

    //Metodos get y set
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }



    /**
     * Expresion regular que valida la validez de una contraseña
     * @param contrasena
     * @return
     */
    public boolean validarContrsena(String contrasena){
        //String regex = "^[a-zA-Z.-9@#$%^&+=!¡¿?_]+$";
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!¡¿?-_])(?!.*\\s).{8,}$";
        Pattern patter = Pattern.compile(regex);
        return patter.matcher(contrasena).matches();
    }
}
