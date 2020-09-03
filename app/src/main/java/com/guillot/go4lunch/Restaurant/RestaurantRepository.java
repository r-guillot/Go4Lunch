package com.guillot.go4lunch.Restaurant;


import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.go4lunch.authentication.User;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.guillot.go4lunch.CONSTANTS.*;


public class RestaurantRepository {
    Call<DetailReponse> call;
    GoogleMap mMap;
    String baseUrl = "https://maps.googleapis.com/";

    public void setRetrofit(LatLng location, int radius, String type,String key) {
        String userLocation = String.valueOf(location.latitude).concat(",").concat(String.valueOf(location.longitude));

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi mJsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
        call = mJsonPlaceHolderApi.getRestaurants(userLocation, radius, type, key);
        Log.d("list", "parametre repo :" + userLocation + " " + radius + " " + type + " " + key);
    }

    MutableLiveData<Restaurant> getRestaurants() {
        MutableLiveData<Restaurant> RestaurantLiveData = new MutableLiveData<>();
        Log.d("list", "getRestaurants: ");
        call.enqueue(new Callback<DetailReponse>() {
            @Override
            public void onResponse(Call<DetailReponse> call, Response<DetailReponse> response) {
                List<Restaurant> restaurants = response.body().getResults();
                Log.d("list", "list size: " + restaurants.size());
                for (Restaurant restaurant: restaurants) {
                    String id = restaurant.getPlaceId();
                    Uri logo = restaurant.getUriIcon();
                   String name = restaurant.getName();
                   Geometry geometry = restaurant.getGeometry();

                   Restaurant markerRestaurant = new Restaurant(id, logo, name, geometry);
                   RestaurantLiveData.setValue(markerRestaurant);
                }
            }

            @Override
            public void onFailure(Call<DetailReponse> call, Throwable t) {
                Log.d("list", "error :" + t.getLocalizedMessage());
            }
        });
        return RestaurantLiveData;
    }
}
