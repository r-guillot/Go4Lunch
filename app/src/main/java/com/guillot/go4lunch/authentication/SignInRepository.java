package com.guillot.go4lunch.authentication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.CONSTANTS;

class SignInRepository {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    MutableLiveData<User> firebaseAuthWithGoogle(AuthCredential googleCredential) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithCredential(googleCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    String username = firebaseUser.getDisplayName();
                    Uri urlProfilePicture = firebaseUser.getPhotoUrl();
                    LatLng userLocation = new LatLng(45.833641, 6.864594);
                    User user = new User(id, username, urlProfilePicture, userLocation);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }else {
                    Log.e("error", "errorFirebaseAuthWithGoogle:" + authTask.getException().getMessage());
                }
        });
        return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseAuthWithFacebook(AuthCredential facebookCredential) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithCredential(facebookCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    String username = firebaseUser.getDisplayName();
                    Uri urlProfilePicture = firebaseUser.getPhotoUrl();
                    User user = new User(id, username, urlProfilePicture, null);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }else {
                Log.e("error", "errorFirebaseAuthWithFacebook:" + authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLIveData;
    }

//    public static SharedPreferences sharedPreferences (Context context) {
//        return context.getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_USER, 0);
//
//        SharedPreferences.Editor editor = sharedPreferences(context).edit();
//    }
//
//    public void saveData() {
//        SharedPreferences sharedPreferences = this.getSharedPreferences(CONSTANTS.SHARED_PREFERENCES_USER, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString()
//    }

}
