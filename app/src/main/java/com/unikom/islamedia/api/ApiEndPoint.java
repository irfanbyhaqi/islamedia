package com.unikom.islamedia.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiEndPoint {
    private static final String BASE_URL = "http://kaffah.info/android/";
    private static final String BASE_URL_CUACA = "https://api.openweathermap.org/data/";

    private static Retrofit retrofit;
    private static Retrofit retrofitCuaca;

    public static Retrofit getClient(){
        if(retrofit == null){
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static Retrofit getCuaca(){
        if (retrofitCuaca == null){
            Gson gson = new GsonBuilder().setLenient().create();
            retrofitCuaca = new Retrofit.Builder()
                        .baseUrl(BASE_URL_CUACA)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
        }

        return  retrofitCuaca;
    }
}
