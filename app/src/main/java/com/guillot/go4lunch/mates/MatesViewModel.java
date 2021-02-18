package com.guillot.go4lunch.mates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MatesViewModel extends ViewModel {

    private MutableLiveData<List<User>> users = new MutableLiveData<>();

    public LiveData<List<User>> getUsers(){ return users; }

    private String userId;

    public void init() {
        UserRepository userRepository = UserRepository.getInstance();
        userId = userRepository.getCurrentUserId();
    }

    public void getAllUsers() {
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> usersList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!Objects.requireNonNull(userFetched).getId().equals(userId)) {
                            usersList.add(userFetched);
                        }
                    }
                    users.setValue(usersList);
                });
    }

}
