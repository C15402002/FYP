package com.example.myapplication.interaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.OrderPlacedActivity;
import com.example.myapplication.R;
import com.example.myapplication.config.RemoteAPIService;
import com.example.myapplication.control.Control;
import com.example.myapplication.helper.NotifyMeHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseMessage extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendNotice26AboveApi(remoteMessage);
        }
        sendNotice(remoteMessage);
    }

    private void sendNotice26AboveApi(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = notification.getTitle();
        String body = notification.getBody();

        Intent intent = new Intent(this, OrderPlacedActivity.class);
        intent.putExtra(Control.PHONE_ID, Control.currentUser.getPhone());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotifyMeHelper notifyMeHelper = new NotifyMeHelper(this);
        Notification.Builder builder = notifyMeHelper.getChannelNotification(title, body, pendingIntent, uri);

        notifyMeHelper.getNotificationManager().notify(new Random().nextInt(), builder.build());
    }

    private void sendNotice(RemoteMessage remoteMessage) {
        RemoteMessage.Notification remoteNotification = remoteMessage.getNotification();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(remoteNotification.getTitle())
                .setContentText(remoteNotification.getBody()).setAutoCancel(true).setSound(uri).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}
