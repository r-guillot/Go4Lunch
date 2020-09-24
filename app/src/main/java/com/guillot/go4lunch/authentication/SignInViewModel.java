package com.guillot.go4lunch.authentication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.guillot.go4lunch.model.User;

public class SignInViewModel extends AndroidViewModel {
    private SignInRepository mSignInRepository;
    LiveData<User> authenticatedUserLiveData;

    public SignInViewModel(Application application) {
        super(application);
        mSignInRepository = new SignInRepository();
    }

    void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithGoogle(googleAuthCredential);
    }

    void signInWithFacebook(AuthCredential facebookAuthCredential) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithFacebook(facebookAuthCredential);
    }

//    public void saveData() {
//        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.
//    }

}
