package com.guillot.go4lunch.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

public class MainViewModel extends ViewModel {
    private final String TAG = MainViewModel.class.getSimpleName();

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNotificationEnable = new MutableLiveData<Boolean>();

    public LiveData<User> getCurrentUserLiveData(){ return currentUser; }
    public LiveData<Boolean> getIsNotificationEnable(){ return isNotificationEnable; }

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

    public void signOut() {
        userRepository.logOut();
    }

    public void checkIfNotificationIsEnabled() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            UserHelper.getUser(userRepository.getCurrentUserId()).addOnSuccessListener(documentSnapshot -> {
                fetchedUser = documentSnapshot.toObject(User.class);
                assert fetchedUser != null;
                fetchedUser.isNotification();
                isNotificationEnable.setValue(fetchedUser.isNotification());
            });
        }
    }
}
