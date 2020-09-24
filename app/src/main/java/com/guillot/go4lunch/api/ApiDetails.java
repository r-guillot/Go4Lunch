package com.guillot.go4lunch.api;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiDetails {

    @GET("place/details/json?&fields=opening_hours&key=" + BuildConfig.ApiPlaceKey)
    Call<ApiResponse> getRestaurantDetails(@Query("place_id") String placeId);
}
