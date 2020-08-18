package com.guillot.go4lunch.Restaurant;

import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.Models.Restaurant;
import com.guillot.go4lunch.authentication.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolder {

    @GET("maps/api/place/nearbysearch/json?location=48.8534,2.3488&radius=1000&types=restaurant&key="+ CONSTANTS.GOOGLE_API_KEY)
    Call<List<Restaurant>> getRestaurants();
}
