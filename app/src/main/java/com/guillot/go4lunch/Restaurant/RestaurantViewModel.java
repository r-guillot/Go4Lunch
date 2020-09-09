package com.guillot.go4lunch.Restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    private RestaurantRepository mRestaurantRepository;
    public LiveData<List<Restaurant>> RestaurantListLiveData;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        mRestaurantRepository = new RestaurantRepository();
    }

    public void setRetrofit(LatLng location, int radius, String type, String key) {
        mRestaurantRepository.setRetrofit(location, radius, type, key);
    }

    public void getRestaurants() {
        RestaurantListLiveData = mRestaurantRepository.getRestaurants();
    }
}
