package com.guillot.go4lunch.api;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.guillot.go4lunch.R;
import com.guillot.go4lunch.notification.NotificationEraser;
import com.guillot.go4lunch.notification.NotificationReceiver;

import java.util.Calendar;

public class NotificationHelper extends ContextWrapper {
    private final String TAG = NotificationHelper.class.getSimpleName();

    private PendingIntent pendingIntentOn;
    private static int[] TIME_NOTIFICATION = {12, 0};
    private static int[] TIME_RESET = {23, 59};

    public NotificationHelper(Context base) {
        super(base);
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getString(R.string.notificationChannel);
            CharSequence name = getString(R.string.name_channel);
            String description = getString(R.string.description_channel);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void configureNotification(boolean isEnable){
        configureNotificationIntent();
        if (isEnable) enableNotification();
        if (!isEnable) disableNotification();
    }

    private void configureNotificationIntent(){
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        pendingIntentOn = PendingIntent.getBroadcast(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public void enableNotification() {
        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY,TIME_NOTIFICATION[0]);
        notificationTime.set(Calendar.MINUTE, TIME_NOTIFICATION[1]);
        notificationTime.set(Calendar.SECOND, 0);

        Calendar todayMidDay = Calendar.getInstance();
        if (notificationTime.before(todayMidDay)) {
            notificationTime.add(Calendar.DATE,1);
        }
        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationReceiver.class);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        assert manager != null;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentOn);
    }

    public void disableNotification() {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        assert manager != null;
        manager.cancel(pendingIntentOn);

        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationReceiver.class);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void resetRestaurantData(){
        Intent notificationIntent = new Intent(this, NotificationEraser.class);
        PendingIntent pendingIntentOff = PendingIntent.getBroadcast(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar resetTime = Calendar.getInstance();
        resetTime.setTimeInMillis(System.currentTimeMillis());
        resetTime.set(Calendar.HOUR_OF_DAY, TIME_RESET[0]);
        resetTime.set(Calendar.MINUTE, TIME_RESET[1]);
        resetTime.set(Calendar.SECOND, 0);

        ComponentName receiver = new ComponentName(getApplicationContext(), NotificationEraser.class);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        assert manager != null;
        manager.setInexactRepeating(AlarmManager.RTC, resetTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentOff);

    }
}
