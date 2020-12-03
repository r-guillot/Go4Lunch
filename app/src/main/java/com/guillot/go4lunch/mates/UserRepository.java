package com.guillot.go4lunch.mates;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.model.User;

import java.util.List;

public class UserRepository {
    private static UserRepository newInstance;
    private FirebaseUser user;

    public static UserRepository getInstance(){
        if(newInstance == null){
            newInstance = new UserRepository();
        }
        return newInstance;
    }

    public String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            return user.getUid();
        }
        else {
           return null;
        }
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

    }

    public void deleteUserFromFirebase() {
        FirebaseAuth.getInstance().getCurrentUser().delete();
     }

}
