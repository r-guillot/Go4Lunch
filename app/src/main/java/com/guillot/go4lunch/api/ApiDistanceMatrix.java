package com.guillot.go4lunch.api;

import com.guillot.go4lunch.BuildConfig;
import com.guillot.go4lunch.model.ApiDetailsRestaurantResponse;
import com.guillot.go4lunch.model.ApiDistanceResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiDistanceMatrix {

    @GET("distancematrix/json?&key=" + BuildConfig.ApiPlaceKey)
    Observable<ApiDistanceResponse> getDistanceMatrix(@Query("origins") String restaurantLocation,
                                                      @Query("destinations") String userLocation);
}
