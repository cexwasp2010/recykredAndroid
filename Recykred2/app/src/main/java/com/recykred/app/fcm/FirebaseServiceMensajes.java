package com.recykred.app.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.recykred.app.LoginActivity;
import com.recykred.app.R;


import java.util.Map;
import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;

public class FirebaseServiceMensajes extends FirebaseMessagingService {




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("aca","remoteMessage = " + remoteMessage.getData());
        super.onMessageReceived(remoteMessage);
        crearNotificacion(remoteMessage.getData());
    }

    private void crearNotificacion(Map<String, String> data) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("id_pedido",data.get("id_pedido"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, getRandomCode(), intent, PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotifChannel(this);

        }
        NotificationCompat.Builder  notificationBuilder = new NotificationCompat.Builder(this,"1234")
                .setContentTitle("Recykred")
                .setContentText("Nuevo pedido de " + data.get("usuario"))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setVibrate(new long[0])
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getRandomCode(), notificationBuilder.build());




    }

    private int getRandomCode(){
        //Obtiene un valor entre 1000.
        Random rand = new Random();
        return rand.nextInt(1000 - 0 + 1);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("aca","token = " + s);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotifChannel(Context context) {
        NotificationChannel channel = new NotificationChannel("1234",
                "MyApp events", NotificationManager.IMPORTANCE_HIGH);
        // Configure the notification channel
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        channel.setDescription("MyApp event controls");
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.setVibrationPattern(new long[]{300, 300, 300});
        if (defaultSoundUri != null) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            channel.setSound(defaultSoundUri, att);
        }
        NotificationManager manager = context.getSystemService(NotificationManager.class);

        manager.createNotificationChannel(channel);

        Log.d("aca", "createNotifChannel: created=" + "1234");
    }
}
