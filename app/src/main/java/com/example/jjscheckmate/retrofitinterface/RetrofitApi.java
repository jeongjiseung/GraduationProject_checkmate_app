package com.example.jjscheckmate.retrofitinterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitApi {
    private static String baseUrl="http://172.30.1.55:8887/";
//    http://172.30.1.13:8887/
//    http://172.30.1.55:8887/
    private static Retrofit retrofit=null;

    public static RetrofitService getService(){

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
//                .baseUrl("http://172.30.1.13:8887/")
                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        return service;

    }

}
