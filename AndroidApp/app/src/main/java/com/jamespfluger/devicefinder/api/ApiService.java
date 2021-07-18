package com.jamespfluger.devicefinder.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";

    private static final ManagementInterface managementInterface = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManagementInterface.class);


    public static ManagementInterface getInstance() {
        return managementInterface;
    }
}
