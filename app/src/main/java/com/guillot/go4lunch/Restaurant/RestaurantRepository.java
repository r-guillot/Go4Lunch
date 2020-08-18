package com.guillot.go4lunch.Restaurant;


import com.guillot.go4lunch.Models.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class RestaurantRepository {

    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    JsonPlaceHolder mJsonPlaceHolder = mRetrofit.create(JsonPlaceHolder.class);
    Call<List<Restaurant>> call = mJsonPlaceHolder.getRestaurants();
}
