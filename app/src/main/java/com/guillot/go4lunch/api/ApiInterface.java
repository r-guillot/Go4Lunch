package com.guillot.go4lunch.api;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?type=restaurant&radius=1500&key=" + BuildConfig.ApiPlaceKey)
    Call<ApiResponse> getRestaurants(@Query("location") String location);
}