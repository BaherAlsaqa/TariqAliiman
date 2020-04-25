package com.tariqaliiman.tariqaliiman.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.tariqaliiman.tariqaliiman.Downloader.DownloadService;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        try {
            context.startService(new Intent(context, DownloadService.class));
        } catch (Exception e) {
            e.printStackTrace();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, DownloadService.class));
            } else {
                context.startService(new Intent(context, DownloadService.class));
            }
        }
    }
}