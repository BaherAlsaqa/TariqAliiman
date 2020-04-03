package com.tariqaliiman.tariqaliiman.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Database.AppPreference;
import com.tariqaliiman.tariqaliiman.Downloader.DownloadService;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.Utilities.FileManager;
import com.tariqaliiman.tariqaliiman.Utilities.QuranConfig;
import com.tariqaliiman.tariqaliiman.Utilities.Settingsss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Activity class to download application resources
 */
public class QuranDataActivity extends Activity {
    public ProgressBar downloadProgress;
    public TextView downloadInformation;
    public Button StartQuran;
    int k = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_data);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(AppConstants.Download.INTENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    /**
     * Init Quran data activity
     */
    private void init() {

        downloadProgress = (ProgressBar) findViewById(R.id.progressBar);
        downloadInformation = (TextView) findViewById(R.id.textView);
        StartQuran = (Button) findViewById(R.id.button);

        StartQuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start home activity
                Intent Home = new Intent(QuranDataActivity.this, HomeActivity.class);
                startActivity(Home);
                finish();
            }
        });

        String AlertMsg;

        File mainDatabase = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                getString(R.string.app_folder_path) + "/quran.sqlite");

        if (!mainDatabase.exists()) {
            Log.d(Constants.log+"download", "main database is exist");
            //copy database
            new CopyDatabase().execute();
        } else {
            Log.d(Constants.log+"download", "main database is not exist, now must download database");
            downloadDialog();
        }
    }

    /**
     * Download Dialog check internet and annotation
     */
    private void downloadDialog() {
        int internetStatus = Settingsss.checkInternetStatus(this);
        if (!Settingsss.isMyServiceRunning(QuranDataActivity.this, DownloadService.class)) {
            if (internetStatus > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.Alert));
                builder.setMessage(internetStatus == 1 ?
                        getResources().getString(R.string.normal_download_alert) :
                        getResources().getString(R.string.mobile_data_alert));
                builder.setPositiveButton(getResources().getString(R.string.Ok),
                        new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) {
                            Log.d(Constants.log+"download", "dialog ok");
                            downloadInformation.setText(getString(R.string.connecting));
                            new Thread(downloadService).start();
                        }
                }
                );
                builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) {dialog.cancel();
                    System.exit(0);}});
                builder.show();

            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setCancelable(false);
                builder.setTitle(getResources().getString(R.string.Alert));
                builder.setMessage(getResources().getString(R.string.no_internet_alert));
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
    }

    /**
     * Thread to check file length and start download service
     */
    private Runnable downloadService = new Runnable() {
        @Override
        public void run() {

            String DownloadLink = QuranConfig.getResolutionURLLink(QuranDataActivity.this);
            Log.e(QuranDataActivity.class.getSimpleName(), "downloadLink : +" + DownloadLink);

            //check file download length
            if (Integer.getInteger(FileManager.getDownloadFileLength(DownloadLink)) ==
                    Integer.getInteger(FileManager.getAvailableInternalMemorySize())) {
                Log.d(Constants.log+"download", "available internal memory size");
                //check if download service running
                if (!Settingsss.isMyServiceRunning(QuranDataActivity.this, DownloadService.class)) {
                    Log.d(Constants.log+"download", "service is not running");
                    AppPreference.setDownloadType(AppConstants.Preferences.IMAGES);
                    Intent serviceIntent = new Intent(QuranDataActivity.this, DownloadService.class);
                    serviceIntent.putExtra(AppConstants.Download.DOWNLOAD_URL, DownloadLink);
                    serviceIntent.putExtra(AppConstants.Download.DOWNLOAD_LOCATION, Environment
                            .getExternalStorageDirectory().getAbsolutePath() +
                            getResources().getString(R.string.app_folder_path));
                    try {
                        getBaseContext().startService(serviceIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getBaseContext().startForegroundService(serviceIntent);
                        }else{
                            getBaseContext().startService(serviceIntent);
                        }
                    }
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadInformation.setText(getString(R.string.no_enough_memory));
                    }
                });
            }
        }
    };

    /**
     * Broadcast receiver to take information of download
     */
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {

            int value = (int) intent.getLongExtra(AppConstants.Download.NUMBER, 0);
            int max = (int) intent.getLongExtra(AppConstants.Download.MAX, 0);
            String status = intent.getStringExtra(AppConstants.Download.DOWNLOAD);

            if (status != null) {
                if (status.equals(AppConstants.Download.IN_DOWNLOAD)) {
                    downloadProgress.setMax(max);
                    downloadProgress.setProgress(value);
                    Log.e("tag", "onReceive: max"+max/10000 );
                    Log.e("tag", "onReceive: value"+value/10000 );
                    downloadInformation.setText(getString(R.string.downloading)+" "+max / 10000 + " / " + value / 10000 +" ( "+(int)((value/10000)*100)/(max / 10000)+"%)");
                } else if (status.equals(AppConstants.Download.FAILED)) {
                    downloadProgress.setMax(1);
                    downloadProgress.setProgress(1);
                    downloadInformation.setText(getString(R.string.failed_download));
                } else if (status.equals(AppConstants.Download.SUCCESS)) {
                    downloadProgress.setMax(1);
                    downloadProgress.setProgress(1);
                    downloadInformation.setText(AppConstants.Download.SUCCESS);
                    downloadProgress.setVisibility(View.GONE);
                    MenuActivity.loadMainApplicationData();
                } else if (status.equals(AppConstants.Download.IN_EXTRACT)) {
                    downloadProgress.setVisibility(View.GONE);
                    downloadInformation.setText(intent.getStringExtra(AppConstants.Download.FILES));
                } else if (status.equals(AppConstants.Download.UNZIP)) {
                    downloadInformation.setVisibility(View.GONE);
                    StartQuran.setVisibility(View.VISIBLE);
                }

            }
        }
    };


    /**
     * Class for copying main database from assets to internal memory
     */
    private class CopyDatabase extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                downloadInformation.setText(getString(R.string.Done));
                downloadDialog();
            } else
                downloadInformation.setText(R.string.failed);

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                boolean canWrie = canWriteInSDCard();
                File databaseDirectory = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()+"/"+getApplicationContext().getResources().getString(R.string.app_folder_path));
                if(!databaseDirectory.exists()){
                    databaseDirectory.mkdirs();
                }
                   File databaseFile = new File(databaseDirectory,"quran.sqlite");
                databaseFile.getParentFile().mkdirs();
                if(!databaseFile.exists()){
                    boolean createFile = databaseFile.createNewFile();
                }
                    InputStream inputStream = getApplicationContext().getAssets().open("quran.sqlite");
                    FileOutputStream outputStream = new FileOutputStream(databaseFile);
                    int fileSize = inputStream.available();
                    publishProgress(0, fileSize);
                    int copylength = 0;
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int read = inputStream.read(buffer);
                        if (read == -1) break;
                        copylength += read;
                        publishProgress(copylength, fileSize);
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return false;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
            return true;
        }

        private boolean canWriteInSDCard(){
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            downloadProgress.setMax(values[1]);
            downloadProgress.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadInformation.setText(getString(R.string.copydata));
        }
    }


}
