package com.example.guessthemovie.apiConsumo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiService {

    private static ApiService instance;
    private RequestQueue requestQueue;
    private static Context context;

    private ApiService(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


}
