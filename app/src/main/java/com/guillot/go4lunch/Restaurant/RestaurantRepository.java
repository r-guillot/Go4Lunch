package com.guillot.go4lunch.Restaurant;


import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestaurantRepository {
    private final String TAG = RestaurantRepository.class.getSimpleName();

    Call<DetailReponse> call;
    GoogleMap mMap;
    String baseUrl = "https://maps.googleapis.com/";
    List<Restaurant> restaurantList;

    public void setRetrofit(LatLng location, int radius, String type,String key) {
        String userLocation = String.valueOf(location.latitude).concat(",").concat(String.valueOf(location.longitude));

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi mJsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
        call = mJsonPlaceHolderApi.getRestaurants(userLocation, radius, type, key);
        Log.d(TAG, "parametre repo :" + userLocation + " " + radius + " " + type + " " + key);
    }

    public MutableLiveData<List<Restaurant>> getRestaurants() {
        MutableLiveData<List<Restaurant>> RestaurantListLiveData = new MutableLiveData<>();
        Log.d(TAG, "getRestaurants: ");
        call.enqueue(new Callback<DetailReponse>() {
            @Override
            public void onResponse(Call<DetailReponse> call, Response<DetailReponse> response) {
                Log.d(TAG, "onResponse: ");
                List<Restaurant> restaurants = response.body().getResults();
                Log.d(TAG, "list size: " + restaurants.size());
                restaurantList = new ArrayList<>();
                for (Restaurant restaurant: restaurants) {
                    String id = restaurant.getPlaceId();
                    Uri logo = restaurant.getUriIcon();
                   String name = restaurant.getName();
                   Geometry geometry = restaurant.getGeometry();

                   Restaurant markerRestaurant = new Restaurant(id, logo, name, geometry);
                    Log.d(TAG, "markerRestaurant " + markerRestaurant.getName());
                   restaurantList.add(markerRestaurant);
                    Log.d(TAG, "list size B: " + restaurantList.size());
                }
                RestaurantListLiveData.setValue(restaurantList);
                Log.d(TAG, "LiveDataSize " + RestaurantListLiveData);
            }

            @Override
            public void onFailure(Call<DetailReponse> call, Throwable t) {
                Log.d(TAG, "error :" + t.getLocalizedMessage());
            }
        });
        return RestaurantListLiveData;
    }
}
