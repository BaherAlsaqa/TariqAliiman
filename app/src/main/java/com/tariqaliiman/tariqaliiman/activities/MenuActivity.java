package com.tariqaliiman.tariqaliiman.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;
import com.tariqaliiman.tariqaliiman.Application.QuranApplication;
import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.Database.AppPreference;
import com.tariqaliiman.tariqaliiman.Database.DatabaseAccess;
import com.tariqaliiman.tariqaliiman.Downloader.DownloadService;
import com.tariqaliiman.tariqaliiman.Models.Quarter;
import com.tariqaliiman.tariqaliiman.Models.Sora;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.SalaatTimesActivity;
import com.tariqaliiman.tariqaliiman.TermsAndConditionsActivity;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.Utilities.QuranConfig;
import com.tariqaliiman.tariqaliiman.Utilities.QuranValidateSources;
import com.tariqaliiman.tariqaliiman.Utilities.Settingsss;
import com.tariqaliiman.tariqaliiman.notification.NotificationHelper;
import com.tariqaliiman.tariqaliiman.utils.AppSettings;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tariqaliiman.tariqaliiman.Constants.REQUEST_TNC;

public class MenuActivity extends AppCompatActivity {

    private Button muslim, sereen, quran, settings, prayerTimings, book, contactUs;
    private String hours, minutes;
    private AppSharedPreferences appSharedPreferences;
    private int x = 0;
    private boolean n = true;
    private InterstitialAd mInterstitialAd1;
    private InterstitialAd mInterstitialAd2;
    private InterstitialAd mInterstitialAd3;

    public static List<Sora> soraListModified ;
    public static List<Quarter> quarterListModified ;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_WRITE_Settings = 113;
    private QuranApplication quranApplication;
    public static File myExternalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        muslim = findViewById(R.id.muslim);
        sereen = findViewById(R.id.sereen);
        quran = findViewById(R.id.quran);
        settings = findViewById(R.id.settings);
        prayerTimings = findViewById(R.id.prayertimings);
        book = findViewById(R.id.books);
        contactUs = findViewById(R.id.contactus);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);
        ((TextView) v.findViewById(R.id.title1)).setText(this.getTitle());
        getSupportActionBar().setCustomView(v);

        /*AppSettings appsettings = AppSettings.getInstance(this);
        if (!appsettings.getBoolean(AppSettings.Key.IS_TNC_ACCEPTED, false)) {
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MenuActivity.this, TermsAndConditionsActivity.class);
                    overridePendingTransition(R.anim.enter_from_bottom, R.anim.no_animation);
                    startActivityForResult(intent, REQUEST_TNC);
                }
            }, 2000);
        }*/

        hours = "8";
        minutes = "0";

        quranApplication = QuranApplication.newInstanse();

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // Interstitial Ads
        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId(getString(R.string.inter1));
        mInterstitialAd1.loadAd(new AdRequest.Builder()
                .build());

        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("log" + "132", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("log" + "132", "onAdFailedToLoad = " + i);
//                Toast.makeText(MenuActivity.this, getString(R.string.problem), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("log" + "132", "onAdClosed");
            }
        });
        ///////////////////////////////////////////////////////
        // Interstitial Ads
        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId(getString(R.string.inter2));
        mInterstitialAd2.loadAd(new AdRequest.Builder()
                .build());

        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("log" + "132", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("log" + "132", "onAdFailedToLoad = " + i);
//                Toast.makeText(MenuActivity.this, getString(R.string.problem), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("log" + "132", "onAdClosed");
            }
        });
        ////////////////////////////////////////////////////////
        // Interstitial Ads
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd3.setAdUnitId(getString(R.string.inter3));
        mInterstitialAd3.loadAd(new AdRequest.Builder()
                .build());

        mInterstitialAd3.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("log" + "132", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("log" + "132", "onAdFailedToLoad = " + i);
//                Toast.makeText(MenuActivity.this, getString(R.string.problem), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("log" + "132", "onAdClosed");
            }
        });

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        appSharedPreferences.writeBoolean("download_on_destroy", true);

        n = appSharedPreferences.readBoolean(Contains.notifi_helper);
        if (n) {
            Log.d(Contains.log+"notifi", "Notification true");
            //notification run
            NotificationHelper.scheduleRepeatingRTCNotification(getApplicationContext(), hours, minutes);
            NotificationHelper.enableBootReceiver(getApplicationContext());
        }else{
            Log.d(Contains.log+"notifi", "Notification false");
        }

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SendEmailContactUs.class));
            }
        });

        prayerTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SalaatTimesActivity.class));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd1.isLoaded()) {
                        mInterstitialAd1.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BooksActivity.class));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd1.isLoaded()) {
                        mInterstitialAd1.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }
            }
        });

        muslim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd1.isLoaded()) {
                        mInterstitialAd1.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }
            }
        });
        sereen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SereenActivity.class));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd2.isLoaded()) {
                        mInterstitialAd2.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //marshmallow check permission permissions
        final boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        quran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermission) {
                    Log.d("baherdb", "(!hasPermission)");
                    ActivityCompat.requestPermissions(MenuActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                } else {
                    Log.d("baherdb", "(hasPermission)");
                    validateFilesAndDownload();
                }
//                startActivity(new Intent(MenuActivity.this, QuranKareem.class));
                /*x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd3.isLoaded()) {
                        mInterstitialAd3.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }*/


            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                Log.d(Contains.log + "7", "x = " + x);
                appSharedPreferences.writeInteger(Contains.cont_ads, x + 1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd3.isLoaded()) {
                        mInterstitialAd3.show();
                        Log.d("log" + "131", "mInterstitialAd1.show. x = "+x);
                    } else {
                        Log.d("log" + "131", "The interstitial1 wasn't loaded yet. x = "+x);
                    }
                }else if (x>=9){
                    Log.d("log" + "131", "x = "+x);
                    appSharedPreferences.writeInteger(Contains.cont_ads, 0);
                }
            }
        });

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mainBanner));
        adView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("logads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("logfailed", "onAdFailedToLoad = " + i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("logonadclosed", "onAdClosed");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check if permission had taken or not
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //valid to download or not
                validateFilesAndDownload();
            } else {
                Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    MenuActivity.this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareItem1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.app_name1) + " " + "\n\n" + " " +
                                getString(R.string.sharemessage) + " " + "\n\n: " +
                                "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MenuActivity.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ratingItem2:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void validateFilesAndDownload() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //set screen resolution in prefrence
                QuranConfig.getResolutionURLLink(MenuActivity.this);

                //check if files is valid
                if (!QuranValidateSources.validatAppMainFoldersAndFiles(MenuActivity.this) ||
                        (Settingsss.isMyServiceRunning(MenuActivity.this, DownloadService.class)
                                && AppPreference.getDownloadType() == AppConstants.Preferences.IMAGES)) {
                    Log.d(Constants.log+"download", "Validat App Main Folders And Files");
                    //set default preference font int web_view to the meduim font
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("size" , "large");
                    editor.apply();
                    Intent downloadActivity = new Intent(MenuActivity.this, QuranDataActivity.class);
                    startActivity(downloadActivity);
//                    MenuActivity.this.finish();

                } else {
                    Log.d(Constants.log+"download", "Not Validat App Main Folders And Files");
                    loadMainApplicationData();

                    Intent Home = new Intent(MenuActivity.this, HomeActivity.class);
                    startActivity(Home);
//                    MenuActivity.this.finish();
                }

            }
        }).start();
    }

    public static void loadMainApplicationData(){
        List<Quarter> quarters = new DatabaseAccess().getAllQuarters();
        int lastPart = 0;
        quarterListModified = new ArrayList<>();
        for (Quarter quarter : quarters) {
            if (lastPart != quarter.joza) {
                lastPart = quarter.joza;
                quarterListModified.add(new Quarter("", "", -1, new DatabaseAccess()
                        .getPartStartPage(lastPart), -1, -1, "", -1, -1, lastPart));
            }
            quarterListModified.add(quarter);
        }

        List<Sora> parts = new DatabaseAccess().getAllSora() ;

        int counter = 0;
        lastPart = 0;
        soraListModified = new ArrayList<>();
        for (Sora sora_item : parts) {
            counter++;
            if (lastPart != sora_item.jozaNumber) {
                if ((sora_item.jozaNumber - lastPart) == 2) {
                    soraListModified.add(new Sora("", "", -1, new DatabaseAccess()
                            .getPartStartPage(lastPart + 1), lastPart + 1, -1));
                }
                lastPart = sora_item.jozaNumber;
                soraListModified.add(new Sora("", "", -1, new DatabaseAccess()
                        .getPartStartPage(lastPart), lastPart, -1));
            }
            sora_item.setSoraTag(counter + "");
            soraListModified.add(sora_item);
        }

    }
}
