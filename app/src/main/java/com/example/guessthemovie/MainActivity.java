package com.example.guessthemovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessthemovie.ActivityConRecycler.peliculasGuardadasRecyclerView;
import com.example.guessthemovie.DB.loginDB;
import com.example.guessthemovie.POO.User;
import com.example.guessthemovie.POO.player;
import com.example.guessthemovie.cosasRelacionadasALogin.EncriptacionResult;
import com.example.guessthemovie.multijugador.multi_single_player;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    EditText usuario, contrasena;
    Button entrar,creacion;

    private GoogleSignInClient client;
    FirebaseAuth auth;
    FirebaseDatabase databaseRTDB;

    private static final String KEY_ALIAS ="my_key_alias12";


    static byte[] contrasenaEnByteCom, usuarioEnByteCom;
    static byte[] usuarioDB, contrasenaDB;
    String datosEncrip,usuarioEncrip;

    static Key aesKey;

    String combinado;
    EncriptacionResult erCon,erUsu,erUsuario,erContrasena;

    static int contador;

    private loginDB loginDB;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.welcome);
        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        entrar = findViewById(R.id.entrar);
        creacion = findViewById(R.id.creacion);
        Animation animacionBtn = AnimationUtils.loadAnimation(this,R.anim.fade_in_button);


        txt.startAnimation(animacion);
        usuario.startAnimation(animacion);
        contrasena.startAnimation(animacion);
        entrar.startAnimation(animacionBtn);
        creacion.startAnimation(animacionBtn);

        loginDB = new loginDB(this);
        database = loginDB.getWritableDatabase();
        try {
            aesKey = generarLave();
            Log.d("llave",aesKey.toString());
        }catch (Exception e){

        }

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,options);
        auth = FirebaseAuth.getInstance();
        databaseRTDB = FirebaseDatabase.getInstance();

        creacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String val = contrasena.getText().toString().trim();
//                String val1 = usuario.getText().toString().trim();
//                User user = new User();
//                boolean validada = user.validarContrsena(val);
//                if(validada){
//                    contrasena.setText("");
//                    usuario.setText("");
//
//                    try {
//
//                        datosEncrip = sha256(val);
//                        usuarioEncrip = sha256(val1);
//
//                        byte[] datosEncriptarBytes = datosEncrip.getBytes(StandardCharsets.UTF_8);
//                        byte[] usuEncriptarBytes = usuarioEncrip.getBytes(StandardCharsets.UTF_8);
//
//                        erCon = encriptarDatos(aesKey,datosEncriptarBytes);
//                        erUsu = encriptarDatos(aesKey,usuEncriptarBytes);
//
//                        newUser(erUsu.getDatosEncriptados(), erUsu.getIv(), erCon.getDatosEncriptados(), erCon.getIv());
//
//                        Toast.makeText(MainActivity.this,"Info encriptada",Toast.LENGTH_SHORT).show();
//                    } catch (Exception ignored) {
//                        Log.d("cosa",ignored.toString());
//                    }
//
//                }else{
//                    Toast.makeText(MainActivity.this,"La contraseña debe contener Mayusculas, minusculas, Numeros, Caracteres especiales",Toast.LENGTH_SHORT).show();
//
//                }

                Intent intent = client.getSignInIntent();
                startActivityForResult(intent,1234);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //buscar los datos mediante emparejado
                    combinado = usuario.getText().toString().trim() + contrasena.getText().toString().trim();
                    readUser();

                    String aux = sha256(String.valueOf(usuario.getText()));
                    String aux1 = sha256(String.valueOf(contrasena.getText()));
                    String a = "";
                    String b = "";
                    String d = "";
                    String c = "";
                    try {
                        usuarioDB = desencriptarDatos(aesKey,erUsuario);
                        contrasenaDB = desencriptarDatos(aesKey,erContrasena);
                        usuarioEnByteCom = aux.getBytes(StandardCharsets.UTF_8);
                        contrasenaEnByteCom = aux1.getBytes(StandardCharsets.UTF_8);
                        a = new String(contrasenaDB, java.nio.charset.StandardCharsets.UTF_8);
                        b = new String(contrasenaEnByteCom, java.nio.charset.StandardCharsets.UTF_8);
                        c = new String(usuarioDB, java.nio.charset.StandardCharsets.UTF_8);
                        d = new String(usuarioEnByteCom, java.nio.charset.StandardCharsets.UTF_8);



                    } catch (Exception e) {
                        Log.d("descrip", e.toString());
                    }

                    if (b.equals(a) && d.equals(c)) {

                        Toast.makeText(MainActivity.this, "Bienvenido ", Toast.LENGTH_SHORT).show();
                        siguiente(v);
                    } else {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        contador++;
                        if (contador == 4) {
                            Toast.makeText(MainActivity.this, "Fin de la sesion", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }catch (Exception e){
                    Log.d("defini",e.toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = auth.getCurrentUser();
                                    player users = new player();
                                    users.setIdPlayer(user.getUid());
                                    users.setName(user.getDisplayName());
                                    users.setProfile(user.getPhotoUrl().toString());
                                    String a = user.getUid();
                                    databaseRTDB.getReference().child("Users").child(user.getUid()).setValue(users);
                                    Intent intent = new Intent(MainActivity.this, multi_single_player.class);
                                    intent.putExtra("UID",user.getUid());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }catch(ApiException e){
                Log.d("Errror",e.getMessage());
            }
        }
    }

    private Key generarLave(){
        try{
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if(!keyStore.containsAlias(KEY_ALIAS)){
                KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setRandomizedEncryptionRequired(true)
                        .build();

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                keyGenerator.init(keyGenParameterSpec);
                Toast.makeText(MainActivity.this,"La llave se genero",Toast.LENGTH_SHORT).show();
                return keyGenerator.generateKey();
            }else{



                return keyStore.getKey(KEY_ALIAS,null);


            }
        }catch(Exception e){
            Log.d("mandarLLave",e.toString());
        }
        return null;
    }

    private String sha256(String contrasena){
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha.digest(contrasena.getBytes());
            StringBuilder hexString = new StringBuilder();
            for(byte b : digest){
                hexString.append(String.format("%02x",b));
            }
            return hexString.toString();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException();
        }
    }

    private EncriptacionResult encriptarDatos(Key clave, byte[] datos) throws Exception{

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);


        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        SecretKey secretKey = secretKeyEntry.getSecretKey();


        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] datosEncriptados = cipher.doFinal(datos);

        byte[] iv = cipher.getIV();

        return new EncriptacionResult(iv, datosEncriptados);

    }

    private void newUser(byte[] usuarioE,byte[] usuarioIV, byte[] contrasena,byte[] contrasenaIV){
        ContentValues values = new ContentValues();
        values.put(loginDB.ColumnUsu,usuarioE);
        values.put(loginDB.ColumnUsuIV,usuarioIV);
        values.put(loginDB.ColumnPass,contrasena);
        values.put(loginDB.ColumnPassIV,contrasenaIV);
        database.insert(loginDB.TableName,null,values);
    }

    private void readUser(){
        try {
            String[] projection = {loginDB.ColumnUsu, loginDB.ColumnUsuIV, loginDB.ColumnPass, loginDB.ColumnPassIV};

            Cursor cursor = database.query(loginDB.TableName, projection, null, null, null, null, null);

            String name = "";
            if (cursor.moveToFirst()) {
                do {
                    byte[] usuarioDB = cursor.getBlob(cursor.getColumnIndex(loginDB.ColumnUsu));
                    byte[] usuarioIVDB = cursor.getBlob(cursor.getColumnIndex(loginDB.ColumnUsuIV));
                    byte[] contrasenaDB = cursor.getBlob(cursor.getColumnIndex(loginDB.ColumnPass));
                    byte[] contrasenaIVDB = cursor.getBlob(cursor.getColumnIndex(loginDB.ColumnPassIV));

                    erUsuario = new EncriptacionResult(usuarioIVDB, usuarioDB);
                    erContrasena = new EncriptacionResult(contrasenaIVDB, contrasenaDB);


                } while (cursor.moveToNext());

            }

            cursor.close();
        }catch(Exception e){
            Log.d("DB", e.toString());
        }
    }

    public byte[] desencriptarDatos(Key clave, EncriptacionResult encriptacionResult) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(encriptacionResult.getIv());
            cipher.init(Cipher.DECRYPT_MODE, clave, ivParameterSpec);
            return cipher.doFinal(encriptacionResult.getDatosEncriptados());
        }catch (Exception e){
            Log.d("desen",e.toString());
        }
        return null;
    }

    public void siguiente(View view){
        Intent volver = new Intent(this, peliculasGuardadasRecyclerView.class);
        startActivity(volver);
    }
}