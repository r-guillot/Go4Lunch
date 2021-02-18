package com.guillot.go4lunch.mates;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserRepository {
    private static UserRepository newInstance;

    public static UserRepository getInstance(){
        if(newInstance == null){
            newInstance = new UserRepository();
        }
        return newInstance;
    }

    public String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            return user.getUid();
        }
           return null;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

    }

    public void deleteUserFromFirebase() {
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();
     }

}
