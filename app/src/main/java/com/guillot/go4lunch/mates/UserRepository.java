package com.guillot.go4lunch.mates;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.model.User;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }
}
