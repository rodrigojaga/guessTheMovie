package com.example.guessthemovie.cosasRelacionadasALogin;

public class EncriptacionResult {

    private byte[] iv;
    private byte[] datosEncriptados;

    public EncriptacionResult(byte[] iv, byte[] datosEncriptados) {
        this.iv = iv;
        this.datosEncriptados = datosEncriptados;
    }

    public byte[] getIv() {
        return iv;
    }

    public byte[] getDatosEncriptados() {
        return datosEncriptados;
    }

}
