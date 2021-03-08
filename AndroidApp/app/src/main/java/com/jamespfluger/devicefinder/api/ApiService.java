package com.jamespfluger.devicefinder.api;

import com.jamespfluger.devicefinder.models.EndpointType;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";

    public static <T> T createInstance(EndpointType type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (type == EndpointType.MANAGEMENT) {
            return (T)retrofit.create(ManagementInterface.class);
        }
        else {
            return (T)retrofit.create(AuthInterface.class);
        }
    }
}
