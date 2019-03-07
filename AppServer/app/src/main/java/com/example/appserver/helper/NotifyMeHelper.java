package com.example.appserver.helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.appserver.R;

public class NotifyMeHelper extends ContextWrapper {
    private static final String OUI_ID = "com.example.appserver.Oui";
    private static final String OUI_NAME = "Oui";

    private NotificationManager notificationManager;

    public NotifyMeHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void channel() {
        NotificationChannel notificationChannel = new NotificationChannel(OUI_ID, OUI_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getNotificationManager() {
        if(notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String body, PendingIntent pendingIntent, Uri uri){

        return new Notification.Builder(getApplicationContext(), OUI_ID).setContentIntent(pendingIntent).setContentTitle(title).setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher).setSound(uri).setAutoCancel(false);
    }
}
