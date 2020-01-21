package com.tariqaliiman.tariqaliiman.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.activities.MenuActivity;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by baher on 4/17/17.
 */

/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {
    private Uri alarmSound;
    private AppSharedPreferences appSharedPreferences;
    private int x;
    private String content;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications

        /*alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
        r.play();*/

        //Intent to invoke app when click on notification.
        //In this sample, we want to start/launch this sample app when user clicks on notification
        appSharedPreferences = new AppSharedPreferences(context);
        Intent intentToRepeat = new Intent(context, MenuActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("xxx", appSharedPreferences.readInteger("bbb")+" out");
        x = appSharedPreferences.readInteger("bbb");
        if (x % 2 == 0){
            Log.d("xxx", appSharedPreferences.readInteger("bbb")+"");
            content = "إملأ يومك بالأذكار";
            x++;
            appSharedPreferences.writeInteger("bbb", x);
        }else{
            Log.d("xxx", appSharedPreferences.readInteger("bbb")+"");
            content = "فسر حلمك لهذا اليوم";
            x++;
            appSharedPreferences.writeInteger("bbb", x);
        }
        //Build notification
        Notification repeatedNotification = buildLocalNotification(context, pendingIntent, content).build();

        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel(manager);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent, String content) {
        Log.d("fcm", "buildLocalNotification");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FileDownload")
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#20a600"))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder;
    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        Log.d("fcm", "createChannel");

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
}
