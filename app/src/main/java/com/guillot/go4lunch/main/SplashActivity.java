package com.guillot.go4lunch.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guillot.go4lunch.R;
import com.guillot.go4lunch.authentication.SignInActivity;
import com.guillot.go4lunch.notification.NotificationEraser;
import com.guillot.go4lunch.notification.NotificationReceiver;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private FirebaseUser user;
    private SharedPreferences mSharedPreferences;
    private String userId;
    private MainViewModel viewModel;
//    private PendingIntent pendingIntentOn;
//    private PendingIntent pendingIntentOff;
//    private static int[] TIME_NOTIFICATION = {12, 0};
//    private static int[] TIME_RESET = {23, 59};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser();
        initViewModel();
//        resetRestaurantData();
//        createNotificationChannel();

        mSharedPreferences = getPreferences(0);
        Log.d("userId" ,"user splash"+ userId);

        checkIfUserIsConnected();
    }

    /**
     * If user is not authenticated, the sign in activity is launch and the user can be authenticate
     * himself,else the maps activity opens
     */
    private void checkIfUserIsConnected() {
        Log.d(TAG, "checkIfUserIsConnected: ");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "user: " +user);
                Intent intent = (user != null )? new Intent(getBaseContext(), CoreActivity.class):
                        new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.init();
//        viewModel.checkIfNotificationIsEnabled();
//        initNotification();
    }

//    private void createNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            String channelId = getString(R.string.notificationChannel);
//            CharSequence name = getString(R.string.name_channel);
//            String description = getString(R.string.description_channel);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    private void initNotification(){
//        viewModel.getIsNotificationEnable().observe(this, this::configureNotification);
//    }
//
//    private void configureNotification(boolean isEnable){
//        configureNotificationIntent();
//        if (isEnable) enableNotification();
//        if (!isEnable) disableNotification();
//    }
//
//    private void configureNotificationIntent(){
//        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
//        pendingIntentOn = PendingIntent.getBroadcast(this, 0,
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//    }
//
//    private void enableNotification() {
//        Calendar notificationTime = Calendar.getInstance();
//        notificationTime.set(Calendar.HOUR_OF_DAY,TIME_NOTIFICATION[0]);
//        notificationTime.set(Calendar.MINUTE, TIME_NOTIFICATION[1]);
//        notificationTime.set(Calendar.SECOND, 0);
//
//        Calendar todayMidDay = Calendar.getInstance();
//        if (notificationTime.before(todayMidDay)) {
//            notificationTime.add(Calendar.DATE,1);
//        }
//        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationReceiver.class);
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        packageManager.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        assert manager != null;
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentOn);
//    }
//
//    private void disableNotification() {
//        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        assert manager != null;
//        manager.cancel(pendingIntentOn);
//
//        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationReceiver.class);
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        packageManager.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//
//    }
//
//    private void resetRestaurantData(){
//        Intent notificationIntent = new Intent(this, NotificationEraser.class);
//        pendingIntentOff = PendingIntent.getBroadcast(this, 0,
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar resetTime = Calendar.getInstance();
//        resetTime.setTimeInMillis(System.currentTimeMillis());
//        resetTime.set(Calendar.HOUR_OF_DAY, TIME_RESET[0]);
//        resetTime.set(Calendar.MINUTE, TIME_RESET[1]);
//        resetTime.set(Calendar.SECOND, 0);
//
//        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationEraser.class);
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        packageManager.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//
//        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        assert manager != null;
//        manager.setInexactRepeating(AlarmManager.RTC, resetTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentOff);
//
//    }
}