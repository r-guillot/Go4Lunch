package com.guillot.go4lunch.authentication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.main.SplashActivity;
import com.guillot.go4lunch.model.User;

import java.util.concurrent.Executor;

public class SignInRepository {
    private final String TAG = SignInRepository.class.getSimpleName();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user;

    MutableLiveData<User> firebaseAuthWithEmailAndPassword(String mail, String password, Context context) {
        Log.d(TAG, "firebaseAuthWithEmailAndPassword: " + mail + password);
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Log.d(TAG, "firebaseAuthWithEmailAndPassword: " + firebaseUser);
                            if (firebaseUser != null) {
                                String id = firebaseUser.getUid();
                                String username = firebaseUser.getDisplayName();
                                String urlProfilePicture = "";
                                String userLocation = "45.833641, 6.864594";
                                String userName = firebaseUser.getEmail();
                                user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", true);
                                createUserInFirestore(firebaseUser);
                                authenticatedUserMutableLIveData.setValue(user);
                            }
                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String error = task.getException().getMessage();
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseGetUserWithEmailAndPassword(String mail, String password, Context context) {
        Log.d(TAG, "firebaseGetUserWithEmailAndPassword: " + mail + password);
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                Log.d(TAG, "firebaseAuthWithEmailAndPassword: " + firebaseUser);
                if (firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    String username = firebaseUser.getDisplayName();
                    String urlProfilePicture = "";
                    String userLocation = "45.833641, 6.864594";
                    String userName = firebaseUser.getEmail();
                    user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", true);
                    createUserInFirestore(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            } else {
                Log.e("error", "firebaseAuthWithEmailAndPassword:" + authTask.getException().getMessage());
                Toast.makeText(context, authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseAuthWithGoogle(AuthCredential googleCredential) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithCredential(googleCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
//                    String id = firebaseUser.getUid();
//                    String username = firebaseUser.getDisplayName();
//                    String urlProfilePicture = firebaseUser.getPhotoUrl().toString();
//                    String userLocation = "45.833641, 6.864594";
//                    String userName = firebaseUser.getEmail();
//                    user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", 1, true);
                    createNewUser(firebaseUser);
                    createUserInFirestore(firebaseUser);
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
//                    String id = firebaseUser.getUid();
//                    String username = firebaseUser.getDisplayName();
//                    String urlProfilePicture = firebaseUser.getPhotoUrl().toString();
//                    String userLocation = "45.833641, 6.864594";
//                    String userName = firebaseUser.getEmail();
//                    user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", 1, true);
                    createNewUser(firebaseUser);
                    createUserInFirestore(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }else {
                Log.e("error", "errorFirebaseAuthWithFacebook:" + authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseAuthWithTwitter(AuthCredential twitterCredential) {
        Log.d(TAG, "firebaseAuthWithTwitter: " + twitterCredential);
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithCredential(twitterCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                Log.d(TAG, "firebaseAuthWithTwitter: "+ firebaseUser);
                if(firebaseUser != null) {
//                    String id = firebaseUser.getUid();
//                    String username = firebaseUser.getDisplayName();
//                    String urlProfilePicture = firebaseUser.getPhotoUrl().toString();
//                    String userLocation = "45.833641, 6.864594";
//                    String userName = firebaseUser.getEmail();
//                    user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", 1, true);
                    createNewUser(firebaseUser);
                    createUserInFirestore(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }else {
                Log.e("error", "errorFirebaseAuthWithTwitter:" + authTask.getException().getMessage());
            }
        });
        return authenticatedUserMutableLIveData;
    }

    private void createNewUser(FirebaseUser firebaseUser){
        String id = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String urlProfilePicture = firebaseUser.getPhotoUrl().toString();
        String userLocation = "45.833641, 6.864594";
        String userName = firebaseUser.getEmail();
        user = new User(id, username, urlProfilePicture, userLocation, userName, "", "", "", true);
    }

    private void createUserInFirestore(FirebaseUser firebaseUser){
        Log.d(TAG, "createUserInFirestore: " + firebaseUser);
        String id = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String urlProfilePicture = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;
        String userLocation = "45.833641, 6.864594";
        String userMail = firebaseUser.getEmail();
        UserHelper.createUser(id, username, urlProfilePicture, userLocation, userMail,  "", "", "", true);
    }


//    public static SharedPreferences sharedPreferences (Context context) {
//        return context.getSharedPreferences(Constants.SHARED_PREFERENCES_USER, 0);
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
