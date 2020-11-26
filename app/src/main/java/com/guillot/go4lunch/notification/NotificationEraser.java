package com.guillot.go4lunch.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

public class NotificationEraser extends BroadcastReceiver {
    private final String TAG = NotificationEraser.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.eraseRestaurantInfo();
    }

    private void eraseRestaurantInfo() {
        UserHelper.getAllUser()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        User user = documentSnapshot.toObject(User.class);
                        if(user != null && user.getRestaurantId() != null) {
                            UserHelper.updateRestaurantInfo(user.getId(), "", "", "");
                            UserHelper.updateNotification(user.getId(), false);
                        }
                    }
                });
    }
}
