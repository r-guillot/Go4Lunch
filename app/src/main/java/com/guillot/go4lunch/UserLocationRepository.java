package com.guillot.go4lunch;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.authentication.User;

public class UserLocationRepository {
    private User user;

    public MutableLiveData<User> SetLocationUser(LatLng location) {
        MutableLiveData<User> locationUserUpdate = new MutableLiveData<>();

        Log.e("user", "location value " + location);Log.e("user", "user value " + user);
//        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        user.setUserLocation(location);



        locationUserUpdate.setValue(user);

        return locationUserUpdate;
    }

}
