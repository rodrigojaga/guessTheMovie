package com.example.guessthemovie.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Crea la tabla local del Login
 * COntiene usuarios
 */
public class loginDB extends SQLiteOpenHelper {

    //Variables locales
    private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "login.db";
    public static final String TableName = "usuario";
    public static final String ColumnUsu = "usuarioNombre";
    public static final String ColumnUsuIV = "usuarioIV";
    public static final String ColumnPass = "usuarioContrasena";
    public static final String ColumnPassIV = "usuarioContrasenaIV";


    public loginDB(Context context){
        super(context,DatabaseName,null,DatabaseVersion);
    }

    /**
     * Crea la base de datos
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create table "+TableName+"("
                +ColumnUsu+" BLOB,"
                +ColumnUsuIV+" BLOB,"
                +ColumnPass+" BLOB,"
                +ColumnPassIV+" BLOB) ";

        db.execSQL(query);
    }

    /**
     * Elimina la base de datos en caso de que exista
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TableName);
        onCreate(db);
    }
}
