package com.guillot.go4lunch.main;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.guillot.go4lunch.authentication.SignInRepository;

public class CoreViewModel extends ViewModel {

    private CoreRepository coreRepository;
    private SignInRepository signInRepository;

    public void init() {
        coreRepository = CoreRepository.getInstance();
        signInRepository = new SignInRepository();
    }


    public String getChosenRestaurant() {
        coreRepository.getUser();
        return coreRepository.getChosenRestaurant();
    }

    public Uri getUserProfilePic() {
        coreRepository.getUser();
        return coreRepository.GetProfilePic();
    }

    public String getUserName() {
        coreRepository.getUser();
        return coreRepository.getUserName();
    }

    public String getUserMail() {
        coreRepository.getUser();
        return coreRepository.getUserMail();
    }


    public void logOutUser() {
        signInRepository.logOut();
    }

}
