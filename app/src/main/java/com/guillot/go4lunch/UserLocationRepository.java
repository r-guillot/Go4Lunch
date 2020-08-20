package com.guillot.go4lunch;

import android.content.Intent;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.authentication.SignInViewModel;
import com.guillot.go4lunch.authentication.User;

public class UserLocationRepository {

    public MutableLiveData<User> SetLocationUser(User user, LatLng location) {
        MutableLiveData<User> locationUserUpdateLiveData = new MutableLiveData<>();

        Log.e("user", "location value " + location);Log.e("user", "user value " + user);
        user.setUserLocation(location);

        locationUserUpdateLiveData.setValue(user);

        return locationUserUpdateLiveData;
    }

}
