package com.tariqaliiman.tariqaliiman.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MuslimsDetails extends AppCompatActivity {

    private TextView hadeeth, reference;
    private FloatingActionButton floatingActionButton;
    private AppSharedPreferences appSharedPreferences;
    private int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muslims_details);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hadeeth = findViewById(R.id.hadeeth);
        reference = findViewById(R.id.reference);
        floatingActionButton = findViewById(R.id.floatingActionButton2);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            final String nameS = intent.getStringExtra(Contains.name);
            final String hadeethS = intent.getStringExtra(Contains.content);
            String referenceS = intent.getStringExtra(Contains.reference);

            LayoutInflater inflator = LayoutInflater.from(this);
            @SuppressLint("InflateParams") View v = inflator.inflate(R.layout.titleview, null);
            ((TextView)v.findViewById(R.id.title1)).setText(nameS);
            getSupportActionBar().setCustomView(v);

            hadeeth.setText(hadeethS);
            reference.setText(referenceS);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.app_name1)+" "+"\n\n"+" "+
                                    getString(R.string.muslim)+" : "+"\n\n"+" "+
                                    nameS+" "+"\n\n"+" "+
                                    hadeethS+" "+"\n\n"+
                                    "https://play.google.com/store/apps/details?id="+getPackageName());
                    sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MuslimsDetails.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                    }
                }
            });

            // View Ads
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(getString(R.string.mainBanner4));
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
