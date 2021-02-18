package com.guillot.go4lunch.notification;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.details.RestaurantDetailActivity;
import com.guillot.go4lunch.mates.UserRepository;
import com.guillot.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {
    private Context context;
    private UserRepository userRepository;
    private String userId;
    private User fetchedUser;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantId;
    private List<String> usersNamesList;
    private PendingIntent pendingIntent;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        userRepository = UserRepository.getInstance();
        getCurrentUserInfo();
    }

    private void intentForTouchAction(Context context) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(Constants.RESTAURANT,restaurantId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }

    public void getCurrentUserInfo() {
        if (userRepository.getCurrentUserId() != null) {
            userId = userRepository.getCurrentUserId();
            UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
                fetchedUser = documentSnapshot.toObject(User.class);
                assert fetchedUser != null;
                restaurantId = fetchedUser.getRestaurantId();
                restaurantName = fetchedUser.getRestaurantName();
                restaurantAddress = fetchedUser.getRestaurantAddress();
                intentForTouchAction(context);
                assert fetchedUser.getRestaurantId() != null;
                if (!fetchedUser.getRestaurantId().equals("")) {
                    getUsersEatingHere(fetchedUser.getRestaurantId());
                }
        });
        }
    }

    public void getUsersEatingHere(String restaurantId) {
        UserHelper.getUserByRestaurantId(restaurantId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    usersNamesList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        User userFetched = documentSnapshot.toObject(User.class);
                        if (!Objects.requireNonNull(userFetched).getId().equals(userId)) {
                            usersNamesList.add(userFetched.getUsername());
                        }
                    }
                    checkIfNotificationShouldBeDisplay();
                });
    }

    private void checkIfNotificationShouldBeDisplay() {
        String message;
        String bigText;
        if (fetchedUser != null && fetchedUser.getRestaurantName() != null && usersNamesList.isEmpty() && fetchedUser.isNotification()) {
            message = context.getString(R.string.notification_message_alone);
            bigText = String.format(message, restaurantName, restaurantAddress);
            showNotification(bigText);
        } else if (fetchedUser != null && fetchedUser.getRestaurantName() != null && !usersNamesList.isEmpty() && fetchedUser.isNotification()){
            message = context.getString(R.string.notification_message);
            bigText = String.format(message, restaurantName, Utils.convertListToStringForNotification(usersNamesList), restaurantAddress);
            showNotification(bigText);
        }
    }

    public void showNotification(String bigText) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, context.getString(R.string.notificationChannel))
                .setSmallIcon(R.drawable.marker_restaurant_green_48px)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        int NOTIFICATION_ID = 33;
        notificationManagerCompat.notify(NOTIFICATION_ID, notification.build());
    }
}
