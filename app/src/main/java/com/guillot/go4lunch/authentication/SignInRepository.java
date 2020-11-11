package com.guillot.go4lunch.authentication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.model.User;

public class SignInRepository {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user;

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
                    String userName = firebaseUser.getEmail();
                    user = new User(id, username, urlProfilePicture, userLocation, userName, null, null);
                    authenticatedUserMutableLIveData.setValue(user);
                    if (UserHelper.getUser(id) == null){
                        UserHelper.createUser(id, username, urlProfilePicture, userLocation, userName, null, null);
                    }
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
                    LatLng userLocation = new LatLng(45.833641, 6.864594);
                    String userName = firebaseUser.getEmail();
                    user = new User(id, username, urlProfilePicture, userLocation, userName, null, null);
                    authenticatedUserMutableLIveData.setValue(user);
                    if (UserHelper.getUser(id) == null){
                        UserHelper.createUser(id, username, urlProfilePicture, userLocation, userName,  null, null);
                    }
                }
            }else {
                Log.e("error", "errorFirebaseAuthWithFacebook:" + authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLIveData;
    }

    public void logOut() {
        mAuth.getCurrentUser();
        mAuth.signOut();}


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

    public void userIntent() {
        Intent intent = new Intent();
        intent.putExtra(Constants.USER_INTENT, user);
    }

}
