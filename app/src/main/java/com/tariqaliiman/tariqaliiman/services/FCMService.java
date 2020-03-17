package com.tariqaliiman.tariqaliiman.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.activities.MenuActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.d(Contains.LOG+"fcm", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT");
            showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getBody(),
                    1);
        } else {
            Log.d(Contains.LOG+"fcm", "else  Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT");
            showNotification(remoteMessage.getNotification().getBody(),
                    1);
        }*/

    }


    private void showNotification(String message, int notifi_id) {
        Log.d("fcm", "showNotification");
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
        r.play();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel(manager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "FileDownload")
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#008577"))
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(notifi_id, builder.build());
        startForeground(1, builder.build());

    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        Log.d(Contains.LOG+"fcm", "createChannel");

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        String name = "FileDownload";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel mChannel = new NotificationChannel(name, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setSound(alarmSound,attributes);
        mChannel.enableVibration(true);
        notificationManager.createNotificationChannel(mChannel);
    }

    /*@Override
    public void onNewToken(String token) {
        Log.d(Contains.LOG+"refresh_token", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        APIInterface apiInterface = null;
        Call<ErrorBody> call = apiInterface.deviceToken(token);
        call.enqueue(new Callback<ErrorBody>() {
            @Override
            public void onResponse(Call<ErrorBody> call, Response<ErrorBody> response) {

                ErrorBody resource = response.body();

                String status = response.body().getStatus().getMessage();
                String code = response.code()+"";

                Log.d(Contains.LOG+"ErrorBody", "Status = "+status+" | Code = "+code);

                if (status.equals(Contains.message)) {

                    Log.d(Contains.LOG+"device_token", resource.getErrorData().getDeviceToken());

                }


            }

            @Override
            public void onFailure(Call<ErrorBody> call, Throwable t) {
                call.cancel();
            }
        });
    }*/
}
