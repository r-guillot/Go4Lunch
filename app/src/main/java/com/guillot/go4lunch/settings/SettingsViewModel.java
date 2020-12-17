package com.guillot.go4lunch.settings;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

public class  SettingsViewModel extends ViewModel {
    private final String TAG = SettingsViewModel.class.getSimpleName();

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<User> getCurrentUserLiveData() {
        return currentUser;
    }

    private UserRepository userRepository;
    private User fetchedUser;
    private StorageReference storageReference;

    public void init() {
        userRepository = UserRepository.getInstance();
    }

    public void getCurrentUser() {
        Log.d(TAG, "getCurrentUser: " + userRepository.getCurrentUserId());
        assert userRepository.getCurrentUserId() != null;
        UserHelper.getUser(userRepository.getCurrentUserId()).addOnSuccessListener(documentSnapshot -> {
            fetchedUser = documentSnapshot.toObject(User.class);
            currentUser.setValue(fetchedUser);
            Log.d(TAG, "getCurrentUser: " + fetchedUser + currentUser);
        });
    }

    public void updateUserInfo(String id, String username, String userMail) {
        UserHelper.updateUserInfo(id, username, userMail);
    }

    public void deleteUser(String id) {
        UserHelper.deleteUser(id);
        userRepository.deleteUserFromFirebase();
    }

    public void uploadPhotoStorage(Uri urlPictureUpload) {
        storageReference = FirebaseStorage.getInstance().getReference(userRepository.getCurrentUserId());
        Log.d(TAG, "uploadPhotoStorage: userId" + userRepository.getCurrentUserId());
        StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + urlPictureUpload.toString());
        Log.d(TAG, "fileReference: " + fileReference);
        UploadTask uploadTask = (UploadTask) fileReference.putFile(urlPictureUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getPhotoFromStorageAndUpdate(taskSnapshot);
                        Log.d(TAG, "fileReference: onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "fileReference: onFailure" + e.getMessage());
                    }
                });
    }

    public void getPhotoFromStorageAndUpdate(UploadTask.TaskSnapshot taskSnapshot) {
        Log.d(TAG, "getPhotoFromStorageAndUpdate: " + taskSnapshot.getMetadata().getReference().getDownloadUrl());
        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String newPhotoUrl = uri.toString();
                    UserHelper.updateUserProfilePicture(userRepository.getCurrentUserId(),
                            newPhotoUrl);
                });
    }

    public void updateNotification(String userId, boolean isChecked){
        UserHelper.updateNotification(userId, isChecked);
    }




}
