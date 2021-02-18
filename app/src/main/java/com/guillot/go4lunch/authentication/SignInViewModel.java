package com.guillot.go4lunch.authentication;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.guillot.go4lunch.model.User;

public class SignInViewModel extends AndroidViewModel {
    private SignInRepository mSignInRepository;
    public LiveData<User> authenticatedUserLiveData;

    public SignInViewModel(Application application) {
        super(application);
        mSignInRepository = new SignInRepository();
    }

    public void checkLogOrSignWithMail(String mail, String password, int check, Context context){
        if (check == 0){
            signInWithEmail(mail, password, context);
        } else if (check == 1){
            logInWithMail(mail, password, context);
        }
    }
    public void logInWithMail(String mail, String password, Context context){
        authenticatedUserLiveData = mSignInRepository.firebaseGetUserWithEmailAndPassword(mail, password, context);
    }

    public void signInWithEmail(String mail, String password, Context context) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithEmailAndPassword(mail,password, context);
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithGoogle(googleAuthCredential);
    }

    public void signInWithFacebook(AuthCredential facebookAuthCredential) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithFacebook(facebookAuthCredential);
    }

    public void signInWithTwitter(AuthCredential twitterAuthCredential) {
        authenticatedUserLiveData = mSignInRepository.firebaseAuthWithTwitter(twitterAuthCredential);
    }

}
