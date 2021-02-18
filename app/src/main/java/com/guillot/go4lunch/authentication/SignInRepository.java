package com.guillot.go4lunch.authentication;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignInRepository {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user;

    MutableLiveData<User> firebaseAuthWithEmailAndPassword(String mail, String password, Context context) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String id = firebaseUser.getUid();
                                String username = (firebaseUser.getDisplayName() != null) ? firebaseUser.getDisplayName() : String.valueOf(R.string.no_name);
                                String urlProfilePicture = "";
                                String userLocation = Constants.LOCATION_MONT_BLANC;
                                String userMail = firebaseUser.getEmail();
                                List<String> restaurantLiked = new ArrayList<>();
                                user = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, "", "", "", true);
                                checkIfUserExist(firebaseUser);
                                authenticatedUserMutableLIveData.setValue(user);
                            }
                        }else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseGetUserWithEmailAndPassword(String mail, String password, Context context) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    String username = (firebaseUser.getDisplayName() != null) ? firebaseUser.getDisplayName() : String.valueOf(R.string.no_name);
                    String urlProfilePicture = "";
                    String userLocation = Constants.LOCATION_MONT_BLANC;
                    String userMail = firebaseUser.getEmail();
                    List<String> restaurantLiked = new ArrayList<>();
                    user = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, "", "", "", true);
                    checkIfUserExist(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            } else {
                Toast.makeText(context, Objects.requireNonNull(authTask.getException()).getMessage(), Toast.LENGTH_LONG).show();
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
                    createNewUser(firebaseUser);
                    checkIfUserExist(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
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
                    createNewUser(firebaseUser);
                    checkIfUserExist(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }
        });
        return authenticatedUserMutableLIveData;
    }

    MutableLiveData<User> firebaseAuthWithTwitter(AuthCredential twitterCredential) {
        MutableLiveData<User> authenticatedUserMutableLIveData = new MutableLiveData<>();
        mAuth.signInWithCredential(twitterCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser != null) {
                    createNewUser(firebaseUser);
                    checkIfUserExist(firebaseUser);
                    authenticatedUserMutableLIveData.setValue(user);
                }
            }
        });
        return authenticatedUserMutableLIveData;
    }

    private void createNewUser(FirebaseUser firebaseUser){
        String id = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String urlProfilePicture = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
        String userLocation = Constants.LOCATION_MONT_BLANC;
        String userMail = firebaseUser.getEmail();
        List<String> restaurantLiked = new ArrayList<>();
        user = new User(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, "", "", "", true);
    }

    public void createUserInFirestore(FirebaseUser firebaseUser){
        String id = firebaseUser.getUid();
        String username = firebaseUser.getDisplayName();
        String urlProfilePicture = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;
        String userLocation = Constants.LOCATION_MONT_BLANC;
        String userMail = firebaseUser.getEmail();
        List<String> restaurantLiked = new ArrayList<>();
        UserHelper.createUser(id, username, urlProfilePicture, userLocation, userMail, restaurantLiked, "", "", "", true);
    }

    public void checkIfUserExist(FirebaseUser firebaseUser) {
        UserHelper.getUser(firebaseUser.getUid()).addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()){
                createUserInFirestore(firebaseUser);
            }
        });
    }
}
