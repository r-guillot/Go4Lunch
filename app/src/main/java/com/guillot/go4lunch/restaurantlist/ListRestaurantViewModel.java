package com.guillot.go4lunch.restaurantlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.Restaurant.model.Restaurant;
import com.guillot.go4lunch.Restaurant.RestaurantRepository;

import java.util.List;

public class ListRestaurantViewModel extends AndroidViewModel {
    private RestaurantRepository mRestaurantRepository;
    public LiveData<List<Restaurant>> RestaurantListLiveData;

    public ListRestaurantViewModel(@NonNull Application application) {
        super(application);
        mRestaurantRepository = new RestaurantRepository();
    }

    public void setRetrofit(LatLng location, int radius, String type, String fields, String key) {
        mRestaurantRepository.setRetrofit(location, radius, type, fields, key);
    }

    public void getRestaurants() {
        RestaurantListLiveData = mRestaurantRepository.getRestaurants();
    }
}
