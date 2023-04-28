package com.example.pfe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyReminderService extends Service {
    private int notificationId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get the desired notification time from the intent extras
        long notificationTime = intent.getLongExtra("notification_time", 0);

        // create intent for BroadcastReceiver to handle the notification
        Intent notificationIntent = new Intent(this, MyBroadcastReceiver.class);
        notificationIntent.putExtra("notification_id", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create an alarm to trigger the BroadcastReceiver at the desired time
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);

        // increment the notification ID to ensure each notification is unique
        notificationId++;

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

