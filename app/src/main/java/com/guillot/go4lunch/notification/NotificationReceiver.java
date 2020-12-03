package com.guillot.go4lunch.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.api.UserHelper;
import com.guillot.go4lunch.common.Constants;
import com.guillot.go4lunch.common.Utils;
import com.guillot.go4lunch.details.RestaurantDetailActivity;
import com.guillot.go4lunch.main.SplashActivity;
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
    private String restaurantId;
    private List<String> usersNamesList;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);
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

                restaurantId = fetchedUser.getRestaurantId();
                restaurantName = fetchedUser.getRestaurantName();
                restaurantAddress = fetchedUser.getRestaurantAddress();
                intentForTouchAction(context);
                getUsersEatingHere(fetchedUser.getRestaurantId());
        });
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
            bigText = String.format(message, restaurantName, Utils.convertListToStringForNotification(usersNamesList), restaurantName);
            showNotification(bigText);
        }
    }

    public void showNotification(String bigText) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, context.getString(R.string.notificationChannel))
                .setSmallIcon(R.drawable.marker_restaurant_green_48px)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.YELLOW, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification.build());
    }
}
