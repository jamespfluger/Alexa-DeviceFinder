package com.jamespfluger.alexadevicefinder.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";

    public static AuthInterface createAuthInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AuthInterface.class);
    }

    public static ManagementInterface createManagementInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "management/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ManagementInterface.class);
    }
}
