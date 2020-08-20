package com.guillot.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.UserLocationRepository;
import com.guillot.go4lunch.authentication.User;

public class UserLocationViewModel extends AndroidViewModel {
    private UserLocationRepository mUserLocationRepository;
    public LiveData<User> locationUserUpdateLiveData;

    public UserLocationViewModel(@NonNull Application application) {
        super(application);

        mUserLocationRepository = new UserLocationRepository();
    }

    public void setLocationUserLiveData(User user, LatLng location) {
        locationUserUpdateLiveData = mUserLocationRepository.SetLocationUser(user, location);
    }
}
