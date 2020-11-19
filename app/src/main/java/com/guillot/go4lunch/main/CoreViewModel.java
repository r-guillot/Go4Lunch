package com.guillot.go4lunch.main;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.authentication.SignInRepository;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

import java.util.List;

public class CoreViewModel extends ViewModel {
    private final String TAG = CoreViewModel.class.getSimpleName();

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<User> getCurrentUserLiveData(){ return currentUser; }

    private UserRepository userRepository;
    private User fetchedUser;

    public void init() {
        userRepository = UserRepository.getInstance();
    }

    public void getCurrentUser() {
        assert userRepository.getCurrentUserId() != null;
        UserHelper.getUser(userRepository.getCurrentUserId()).addOnSuccessListener(documentSnapshot -> {
            fetchedUser = documentSnapshot.toObject(User.class);
            currentUser.setValue(fetchedUser);
            Log.d(TAG, "getCurrentUser: " + fetchedUser+ currentUser);
        });
    }

    public String getChosenRestaurant() {
        return fetchedUser.getRestaurantId();
    }

}
