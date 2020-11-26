package com.guillot.go4lunch.notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = NotificationReceiver.class.getSimpleName();
    private final int NOTIFICATION_ID = 33;
    private NotificationManagerCompat notificationManager;
    private Context context;
    private UserRepository userRepository;
    private String userId;
    private User fetchedUser;
    private String restaurantName;
    private String restaurantAddress;

    private List<String> usersNamesList;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
        userRepository = UserRepository.getInstance();
        getCurrentUserInfo();
    }

    public void getCurrentUserInfo() {
        if (userRepository.getCurrentUserId() != null) {
            userId = userRepository.getCurrentUserId();
            UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
                fetchedUser = documentSnapshot.toObject(User.class);
            });
            restaurantName = fetchedUser.getRestaurantName();
            restaurantAddress = fetchedUser.getRestaurantAddress();
            getUsersEatingHere(fetchedUser.getRestaurantId());
        }
    }

    public void getUsersEatingHere(String restaurantId) {
        UserHelper.getUserByRestaurantId(restaurantId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    usersNamesList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!userFetched.getId().equals(userId)) {
                            usersNamesList.add(userFetched.getUsername());
                        }
                    }
                    checkIfNotificationShouldBeDisplay();
                });
    }

    private void checkIfNotificationShouldBeDisplay() {
        String message;
        String bigText;
        if (fetchedUser != null && fetchedUser.getRestaurantName() != null && usersNamesList == null) {
            message = context.getString(R.string.notification_message_alone);
            bigText = String.format(message, restaurantName, restaurantAddress);
            showNotification(bigText);
        } else if (fetchedUser != null && fetchedUser.getRestaurantName() != null && usersNamesList != null){
            message = context.getString(R.string.notification_message);
            bigText = String.format(message, restaurantName, usersNamesList, restaurantName);
            showNotification(bigText);
        }
    }

    public void showNotification(String bigText) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, context.getString(R.string.notificationChannel))
                .setSmallIcon(R.drawable.marker_restaurant_green_48px)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification.build());
    }
}
