package com.guillot.go4lunch.Restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

class RestaurantViewModel extends AndroidViewModel {
    private RestaurantRepository mRestaurantRepository;
    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        mRestaurantRepository = new RestaurantRepository();
    }

    public void setRetrofit() {
        mRestaurantRepository.setRetrofit();
    }
}
