package com.guillot.go4lunch.Restaurant;

import com.guillot.go4lunch.CONSTANTS;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("maps/api/place/nearbysearch/json?")
    Call<DetailReponse> getRestaurants(@Query("location") String location,
                                       @Query("radius") int radius,
                                       @Query("type") String type,
                                       @Query("fields") String fields,
                                       @Query("key") String key);
}

//48.8534,2.3488