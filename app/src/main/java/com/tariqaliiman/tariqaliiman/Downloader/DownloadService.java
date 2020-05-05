package com.tariqaliiman.tariqaliiman.Downloader;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Database.AppPreference;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.scheduler.Restarter;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import java.util.List;

/**
 * Service class to download file
 */
public class DownloadService extends Service {
    private DownloadManager downloadManager;
    AppSharedPreferences appSharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Function to start service
     *
     * @param intent  Activity intent
     * @param flags   to start sticky or not
     * @param startId Service id
     * @return usually start or start once
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            appSharedPreferences = new AppSharedPreferences(getBaseContext());
            AppPreference.Downloading(true);
            Bundle extras = intent.getExtras();
            assert extras != null;
            String downloadURL = extras.getString(AppConstants.Download.DOWNLOAD_URL);

            String downloadLocation = extras.getString(AppConstants.Download.DOWNLOAD_LOCATION);
            int type = extras.getInt(AppConstants.Download.TYPE, -1);
            List<String> downloadLinks = extras.getStringArrayList(AppConstants.Download.DOWNLOAD_LINKS);
            Log.d(Constants.log + "download", "service is running not sticky");
            if (downloadLinks == null) {
                Log.d(Constants.log + "download", "downloadLinks == null");
                downloadManager = new DownloadManager(this, true, type);
                downloadManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadURL, downloadLocation);
            } else {
                Log.d(Constants.log + "download", "downloadLinks != null");
                downloadManager = new DownloadManager(this, true, downloadLinks, type);
                downloadManager.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", downloadLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;//change from START_NOT_STICKY to START_STICKY to continue downlaod
    }

    /**
     * Function on destroy service set downloading statues
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Broadcast Listened", "onDestroy >> Service tried to stop");
        /*if (downloadManager != null) {
            downloadManager.stopDownload = true;
        }
        AppPreference.Downloading(false);*/
        appSharedPreferences.writeBoolean("download_on_destroy", false);
        boolean download_complete = appSharedPreferences.readBoolean("download_complete");
        if (!download_complete) {
            Log.d(Constants.log+"ddd", "if (!download_complete)");
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(DownloadService.this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }
    }


}
