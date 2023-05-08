package com.example.pfe.news;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.pfe.HomepageActivity;
import com.example.pfe.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "my_channel_id";
    private Context mContext;
    private NotificationManager mNotificationManager;

    public NotificationHelper(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My App Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My App Notifications");
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationCompat.Builder getNotificationBuilder(String title, String message) {
        Intent resultIntent = new Intent(mContext, HomepageActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    public void showNotification(int notificationId, Notification notification) {
        mNotificationManager.notify(notificationId, notification);
    }
}

