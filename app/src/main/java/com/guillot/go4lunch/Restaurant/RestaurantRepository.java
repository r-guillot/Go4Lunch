package com.guillot.go4lunch.Restaurant;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guillot.go4lunch.CONSTANTS;
import com.squareup.moshi.Moshi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.guillot.go4lunch.CONSTANTS.*;


public class RestaurantRepository {
    Call<List<Restaurant>> call;
    GoogleMap mMap;
    String baseUrl = "https://maps.googleapis.com/";

    public void setRetrofit(LatLng location, int radius, String type,String key) {
        String userLocation = location.toString();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        JsonPlaceHolderApi mJsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
        call = mJsonPlaceHolderApi.getRestaurants(userLocation, radius, type, key);
    }

    public void getRestaurants() {
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurants = response.body();
                for (Restaurant restaurant: restaurants) {
                   double lat = restaurant.getLat();
                   double lng = restaurant.getLng();
                   String name = restaurant.getName();

                   LatLng positionRestaurant = new LatLng(lat, lng);
                   mMap.addMarker(new MarkerOptions()
                   .position(positionRestaurant)
                   .title(name));
                }
            }


            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {

            }
        });
    }

//    private void restaurantMarker() {
//        mMap.addMarker(new MarkerOptions()
//                .position(call.)
//        )
//    }
}
