package com.tariqaliiman.tariqaliiman.Utilities;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tariqaliiman.tariqaliiman.Database.AppPreference;
import com.tariqaliiman.tariqaliiman.activities.custom.HighlightImageView;

import java.io.File;

/**
 * Created by nosier on 11/15/2016.
 */

public class CloseApplication extends Service {
    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        HighlightImageView.selectionFromTouch = false;


        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

//        deleteCache(getApplicationContext());
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
//        quit();
        deleteCache(getApplicationContext());
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        AppPreference.setSelectionVerse(null);
//        //delete all selection in the image
//        Intent resetImage = new Intent(AppConstants.Highlight.RESET_IMAGE);
//        resetImage.putExtra(AppConstants.Highlight.RESET , true);
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(resetImage);
////        Toast.makeText(this, "Service Destroyeddddddd", Toast.LENGTH_LONG).show();
//        QuranPageFragment.SELECTION = false;
//        HighlightImageView.selectionFromTouch = false;
////        super.onTaskRemoved(rootIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
//        quit();
        trimCache();
        deleteCache(getApplicationContext());


    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
    }


    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    public void trimCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
