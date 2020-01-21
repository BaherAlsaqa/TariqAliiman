package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

public class SereenDetails extends AppCompatActivity {

    private TextView hadeeth, reference;
    private FloatingActionButton floatingActionButton;
    private AppSharedPreferences appSharedPreferences;
    private int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sereen_details);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hadeeth = findViewById(R.id.hadeeth);
        floatingActionButton = findViewById(R.id.floatingActionButton2);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            final String nameS = intent.getStringExtra(Contains.name);
            final String details = intent.getStringExtra(Contains.content);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.titleview, null);
            ((TextView)v.findViewById(R.id.title1)).setText(nameS);
            getSupportActionBar().setCustomView(v);

            hadeeth.setText(details);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.app_name1)+" "+"\n\n"+" "+
                                    getString(R.string.sereen)+" : "+"\n\n"+" "+
                                    nameS+" "+"\n\n"+" "+
                                    details+" "+"\n\n"+" "+
                                    "https://play.google.com/store/apps/details?id="+getPackageName());
                    sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(SereenDetails.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                    }
                }
            });

            // View Ads
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getString(R.string.Banner5));
            adView = findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.d( "logads", "onAdLoaded");
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}