package com.example.guessthemovie.apiConsumo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Hace la conexion con la api
 */
public class ApiService {

    //Variable globales
    private static ApiService instance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * Permite obtener un contexto para conectar con la Api
     * @param context
     */
    private ApiService(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Obtiene una instancia para conectar con la api
     * @param context
     * @return
     */
    public static ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    /**
     * Recibe una cola de respuesta
     * @return
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Obtiene la peticion que le estamos haciendo a la api
     * @param request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


}
