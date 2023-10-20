package com.example.guessthemovie.cosasRelacionadasALogin;

/**
 * Clase util para guardar usuarios registrados en el Login de manera local
 */
public class EncriptacionResult {
//Atributos
    private byte[] iv;
    private byte[] datosEncriptados;

    //Constructor
    public EncriptacionResult(byte[] iv, byte[] datosEncriptados) {
        this.iv = iv;
        this.datosEncriptados = datosEncriptados;
    }
//Metodos get
    public byte[] getIv() {
        return iv;
    }

    public byte[] getDatosEncriptados() {
        return datosEncriptados;
    }

}
