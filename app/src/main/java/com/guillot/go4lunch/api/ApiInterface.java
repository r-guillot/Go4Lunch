package com.guillot.go4lunch.api;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.model.ApiRestaurantResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?type=restaurant&rankby=distance&key=" + BuildConfig.ApiPlaceKey)
    Observable<ApiRestaurantResponse> getRestaurants(@Query("location") String location);
}