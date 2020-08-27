package com.guillot.go4lunch.Restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.CONSTANTS;

public class RestaurantViewModel extends AndroidViewModel {
    private RestaurantRepository mRestaurantRepository;
    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        mRestaurantRepository = new RestaurantRepository();
    }

    public void setRetrofit(LatLng location, int radius, String type, String key) {
        mRestaurantRepository.setRetrofit(location, radius, type, key);
    }

    public void getRestaurants() {
        mRestaurantRepository.getRestaurants();
    }
}
