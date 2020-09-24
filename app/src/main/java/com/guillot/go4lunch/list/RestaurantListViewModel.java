package com.guillot.go4lunch.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.maps.RestaurantMapRepository;
import com.guillot.go4lunch.model.Restaurant;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> mutableLiveData;

    public void init(LatLng location) {
        if (mutableLiveData != null) {
            return;
        }
        RestaurantMapRepository mRestaurantRepository = RestaurantMapRepository.getInstance();
        mutableLiveData = mRestaurantRepository.getRestaurantList(Utils.convertLocationForApi(location));
    }

    public LiveData<List<Restaurant>> getRestaurantsRepository() {
        return mutableLiveData;
    }
}
