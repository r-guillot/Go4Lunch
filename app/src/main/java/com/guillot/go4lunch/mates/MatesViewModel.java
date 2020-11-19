package com.guillot.go4lunch.mates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.main.CoreActivity;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class MatesViewModel extends ViewModel {
    private final String TAG = MatesViewModel.class.getSimpleName();

    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<List<User>> getUsers(){ return users; }

    private  UserRepository userRepository;
    private String userId;

    public void init() {
        userRepository = UserRepository.getInstance();
        userId = userRepository.getCurrentUserId();
    }

    public void getAllUsers() {
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> usersList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        Log.d(TAG, "userFetched: " + userFetched);
                        if (!userFetched.getId().equals(userId)) {
                            Log.d(TAG, "currentUserId: " + userId);
                            usersList.add(userFetched);
                            Log.d(TAG, "usersList: " + usersList);
                        }
                    }
                    users.setValue(usersList);
                });
    }

}
