package com.example.guessthemovie.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class peliculasDB extends SQLiteOpenHelper {
    //Variables globales
    private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "guessTheMovie1.db";
    public static final String TableName = "movies";
    public static final String ColumnID = "id";
    public static final String ColumnMovieName = "movieName";
    public static final String ColumnImage1 = "Image1";
    public static final String ColumnImage2 = "Image2";
    public static final String ColumnImage3 = "Image3";
    public static final String ColumnPistas = "pistas";

    public peliculasDB(Context context){
        super(context,DatabaseName,null,DatabaseVersion);
    }

    /**
     * Crea la base de datos
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create table "+TableName+"("
                +ColumnID+" INTEGER Primary Key Autoincrement,"
                +ColumnMovieName+" TEXT,"
                +ColumnImage1+" TEXT,"
                +ColumnImage2+" TEXT,"
                +ColumnImage3+" TEXT,"
                +ColumnPistas+" TEXT) ";

        db.execSQL(query);
    }

    /**
     * Elimina la tabla en caso de que exista
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
