package com.recykred.app.retrofit;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static Retrofit newInstance;
    private static String dirc = "";

    public static Retrofit getInstance(String dir){
        if(!dir.equalsIgnoreCase("")){
            dirc = dir;
        }else{
            dirc = "http://conectemonos.com/";
        }
        if(newInstance == null){


            newInstance = new Retrofit.Builder()
                    .baseUrl(dirc)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
     return  newInstance;
    }

}
