package com.performans.commonutilshelper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();
    private static final int NOTIFICATION_ID = 2738;
    private static final String NOTIFICATION_CHANNEL_ID = "73";
    private static final String NOTIFICATION_CHANNEL_NAME = "channel73";

    public static Notification getNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setLightColor(Color.BLUE);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(context.getApplicationContext(),
                    NOTIFICATION_CHANNEL_ID);
            return builder.build();
        } else {
            return null;
        }
    }


    public static void createNotification(Context context, Intent targetIntent, String contentText, int icon) {
        createNotification(context, targetIntent, contentText, icon, "");
    }

    @SuppressLint("NewApi")
    public static void createNotification(Context context, Intent targetIntent, String contentText, int icon, String action) {
        Intent intent = new Intent(context, targetIntent.getClass());
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentText(contentText)
                .setContentTitle(context.getResources().getString(R.string.app_name_cu))
                .setSmallIcon(icon)
                .setContentIntent(pIntent)
                .addAction(icon, action, pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Objects.requireNonNull(notificationManager).notify(NOTIFICATION_ID, notification);
    }

    public static void dismissNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).cancel(NOTIFICATION_ID);
    }
}