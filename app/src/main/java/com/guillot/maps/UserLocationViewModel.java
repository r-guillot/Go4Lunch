package com.guillot.maps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.google.android.gms.maps.model.LatLng;
import com.guillot.go4lunch.CONSTANTS;
import com.guillot.maps.UserLocationRepository;
import com.guillot.go4lunch.authentication.User;

public class UserLocationViewModel extends AndroidViewModel {
    private UserLocationRepository mUserLocationRepository;
    public LiveData<User> locationUserUpdateLiveData;

    public UserLocationViewModel(@NonNull Application application) {
        super(application);

        mUserLocationRepository = new UserLocationRepository();
    }

}
