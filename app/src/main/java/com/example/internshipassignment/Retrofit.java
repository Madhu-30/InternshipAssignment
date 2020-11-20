package com.example.internshipassignment;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static com.example.internshipassignment.Retrofit instance = null;
    private final com.example.internshipassignment.APIInterface apiInterface;

    private Retrofit() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder().baseUrl(com.example.internshipassignment.APIInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(com.example.internshipassignment.APIInterface.class);
    }

    public static synchronized com.example.internshipassignment.Retrofit getInstance() {
        if (instance == null) {
            instance = new com.example.internshipassignment.Retrofit();
        }
        return instance;
    }

    public com.example.internshipassignment.APIInterface getApiInterface() {
        return apiInterface;
    }
}