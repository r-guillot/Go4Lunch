package com.guillot.go4lunch.api;

import com.guillot.go4lunch.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.ApiPlaceBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiInterface getInterface() {
        return retrofit.create(ApiInterface.class);
    }

    public static ApiDetails getDetails() {return retrofit.create(ApiDetails.class);}
}
