package com.example.guessthemovie.apiConsumo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.guessthemovie.POO.Movie;
import com.example.guessthemovie.metodosPublicos.ListaYMetodoDeLlenado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyApiClient {

    //Variables globales
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1N2E1NTBjZjQ1YzdjNDYxNGVlZmQ2NGRiNDVkNzczOCIsInN1YiI6IjY1MGE1ZDIxYWVkZTU5MDEzODg1YTViYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.5aA20cOhzDO2VunpPsq2P1HPZNj6ThWyMQU6yTy5tCI";
    public static ArrayList<Movie> movies = new ArrayList<>();

    /**
     * Interfaz util para saber si nos conectamos correctamente
     * @param <T>
     */
    public interface MyApiCallback<T> {
        void onSuccess(T response);

        void onError(String errorMessage);
    }

    /**
     * Obtiene los datos de la api en Modo JSON y los deserializa
     * @param context
     * @param callback
     */
    public static void makeApiRequest(Context context, final MyApiCallback<Movie> callback) {
        String url = BASE_URL + "movie/popular?language=en-US&page=2";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray resultsArray = response.getJSONArray("results");

                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject movieObject = resultsArray.getJSONObject(i);

                                Movie movie = new Movie(movieObject.getInt("id"),movieObject.getString("overview"),
                                        movieObject.getString("poster_path"),movieObject.getString("release_date"),
                                        movieObject.getString("title"));
                                try {
                                    ListaYMetodoDeLlenado.pelicula(movie);
                                    movies.add(movie);
                                }catch (Exception e){
                                    Log.d("Error",e.getMessage());
                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(context.getApplicationContext(), "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }) {
            //Pone los Headers a la peticion, lo que hace que esta nos devuelva una respuesta
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + API_KEY);
                return headers;
            }
        };

        ApiService.getInstance(context).addToRequestQueue(request);
        ListaYMetodoDeLlenado.cambioNuevo = true;
    }

}
