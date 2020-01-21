package com.tariqaliiman.tariqaliiman.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.adapters.SereensAdapter;
import com.tariqaliiman.tariqaliiman.classes.Sereen;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener1;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener2;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SereenActivity extends AppCompatActivity {

    private static final String TAG = "baherb1";
    private RecyclerView recyclerView;
    private SereensAdapter sereensAdapter;
    private ArrayList<Sereen> sereenArrayList;
    private ArrayList<Sereen> sereenArrayListResult;
    private LinearLayoutManager linearLayoutManager;
    private TextView alf, baa, taa, thaa, geem, h7aa, khaa, dal, thal, raa, zain, seen,
                        sheen, saad, daad, ttaa, thhaa, aain, gain, faa, gaaf, kaaf,
                        laam, meem, noon, haa, oaao, iaa;
    private TextView largChar;
    private Handler myHandler;
    private Runnable myRunnable;
    String[] str={"","","",""};
    private ConstraintLayout constraintLayout5;
    private int x=0;
    private AppSharedPreferences appSharedPreferences;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sereen);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Find view
        recyclerView = findViewById(R.id.recyclerview);
        largChar = findViewById(R.id.largchar);
        alf   = findViewById(R.id.alf);
        baa   = findViewById(R.id.baa);
        taa   = findViewById(R.id.taa);
        thaa  = findViewById(R.id.thaa);
        geem  = findViewById(R.id.geem);
        h7aa  = findViewById(R.id.h7aa);
        khaa  = findViewById(R.id.khaa);
        dal   = findViewById(R.id.dal);
        thal  = findViewById(R.id.thal);
        raa   = findViewById(R.id.raa);
        zain  = findViewById(R.id.zain);
        seen  = findViewById(R.id.seen);
        sheen = findViewById(R.id.sheen);
        saad  = findViewById(R.id.saad);
        daad  = findViewById(R.id.daad);
        ttaa  = findViewById(R.id.ttaa);
        thhaa = findViewById(R.id.thhaa);
        aain  = findViewById(R.id.aain);
        gain  = findViewById(R.id.gain);
        faa   = findViewById(R.id.faa);
        gaaf  = findViewById(R.id.gaaf);
        kaaf  = findViewById(R.id.kaaf);
        laam  = findViewById(R.id.laam);
        meem  = findViewById(R.id.meem);
        noon  = findViewById(R.id.noon);
        haa   = findViewById(R.id.haa);
        oaao  = findViewById(R.id.oaao);
        iaa   = findViewById(R.id.iaa);
        constraintLayout5 = findViewById(R.id.constraintLayout5);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);
        ((TextView)v.findViewById(R.id.title1)).setText(getString(R.string.sereen));
        getSupportActionBar().setCustomView(v);

        //Initialize
        sereenArrayList = new ArrayList<>();
        sereenArrayListResult = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // Interstitial Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter5));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("log" + "132", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("log" + "132", "onAdFailedToLoad = " + i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("log" + "132", "onAdClosed");
            }
        });

        Resources resources = getResources();
        String[] titles = resources.getStringArray(R.array.sereen_titles);
        String[] contents = resources.getStringArray(R.array.sereen_content);

        Log.d(TAG, "titles.length = "+titles.length);
        for (int i = 0; i < titles.length; i++) {
            Sereen sereen = new Sereen(titles[i], contents[i]);
            sereenArrayList.add(sereen);
            sereenArrayListResult.add(sereen);
        }

        sereensAdapter = new SereensAdapter(sereenArrayListResult, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Collections.sort(sereenArrayList, Sereen.BY_NAME_ALPHABETICAL);
        recyclerView.setAdapter(sereensAdapter);

        sereensAdapter.setOnClickListener(new OnItemClickListener1() {
            @Override
            public void onItemClick(Sereen item) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.app_name1)+" "+"\n\n"+" "+
                                getString(R.string.sereen)+" : "+"\n\n"+" "+
                                item.getTitle()+" "+"\n\n"+" "+
                                item.getContent()+" "+"\n\n"+
                                "https://play.google.com/store/apps/details?id="+getPackageName());
                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SereenActivity.this, getString(R.string.installmessenger), Toast.LENGTH_LONG).show();
                }
            }
        });
        sereensAdapter.setOnClickListener(new OnItemClickListener2() {
            @Override
            public void onItemClick(Sereen item) {
                startActivity(new Intent(SereenActivity.this, SereenDetails.class)
                        .putExtra(Contains.name, item.getTitle())
                        .putExtra(Contains.content, item.getContent()));
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                appSharedPreferences.writeInteger(Contains.cont_ads, x+1);
                x = appSharedPreferences.readInteger(Contains.cont_ads);
                if (x==1 | x==5) {
                    // Show Interstitial Ads
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
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

        final RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(SereenActivity.this) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

        alf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(0);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(alf.getText().toString());
                resetColorChar();
                alf.setTextColor(Color.parseColor("#168B01"));
            }
        });
        baa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(40);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(baa.getText().toString());
                resetColorChar();
                baa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        taa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(91);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(taa.getText().toString());
                resetColorChar();
                taa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        thaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(122);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(thaa.getText().toString());
                resetColorChar();
                thaa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        geem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(133);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(geem.getText().toString());
                resetColorChar();
                geem.setTextColor(Color.parseColor("#168B01"));
            }
        });
        h7aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(174);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(h7aa.getText().toString());
                resetColorChar();
                h7aa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        khaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(233);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(khaa.getText().toString());
                resetColorChar();
                khaa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        dal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(277);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(dal.getText().toString());
                resetColorChar();
                dal.setTextColor(Color.parseColor("#168B01"));
            }
        });
        thal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(315);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(thal.getText().toString());
                resetColorChar();
                thal.setTextColor(Color.parseColor("#168B01"));
            }
        });
        raa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(326);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(raa.getText().toString());
                resetColorChar();
                raa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        zain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(371);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(zain.getText().toString());
                resetColorChar();
                zain.setTextColor(Color.parseColor("#168B01"));
            }
        });
        seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(399);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(seen.getText().toString());
                resetColorChar();
                seen.setTextColor(Color.parseColor("#168B01"));
            }
        });
        sheen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(467);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(sheen.getText().toString());
                resetColorChar();
                sheen.setTextColor(Color.parseColor("#168B01"));
            }
        });
        saad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(507);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(saad.getText().toString());
                resetColorChar();
                saad.setTextColor(Color.parseColor("#168B01"));
            }
        });
        daad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(548);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(daad.getText().toString());
                resetColorChar();
                daad.setTextColor(Color.parseColor("#168B01"));
            }
        });
        ttaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(563);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(ttaa.getText().toString());
                resetColorChar();
                ttaa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        thhaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(587);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(thhaa.getText().toString());
                resetColorChar();
                thhaa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        aain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(594);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(aain.getText().toString());
                resetColorChar();
                aain.setTextColor(Color.parseColor("#168B01"));
            }
        });
        gain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(662);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(gain.getText().toString());
                resetColorChar();
                gain.setTextColor(Color.parseColor("#168B01"));
            }
        });
        faa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(688);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(faa.getText().toString());
                resetColorChar();
                faa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        gaaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(731);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(gaaf.getText().toString());
                resetColorChar();
                gaaf.setTextColor(Color.parseColor("#168B01"));
            }
        });
        kaaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(780);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(kaaf.getText().toString());
                resetColorChar();
                kaaf.setTextColor(Color.parseColor("#168B01"));
            }
        });
        laam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(852);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(laam.getText().toString());
                resetColorChar();
                laam.setTextColor(Color.parseColor("#168B01"));
            }
        });
        meem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(880);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(meem.getText().toString());
                resetColorChar();
                meem.setTextColor(Color.parseColor("#168B01"));
            }
        });
        noon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(981);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(noon.getText().toString());
                resetColorChar();
                noon.setTextColor(Color.parseColor("#168B01"));
            }
        });
        haa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(1026);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(haa.getText().toString());
                resetColorChar();
                haa.setTextColor(Color.parseColor("#168B01"));
            }
        });
        oaao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(1041);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(oaao.getText().toString());
                resetColorChar();
                oaao.setTextColor(Color.parseColor("#168B01"));
            }
        });
        iaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothScroller.setTargetPosition(1078);
                linearLayoutManager.startSmoothScroll(smoothScroller);
                largChar(iaa.getText().toString());
                resetColorChar();
                iaa.setTextColor(Color.parseColor("#168B01"));
            }
        });
    }
    public void largChar(String char1){

        largChar.setText(char1);
        largChar.setVisibility(View.VISIBLE);
        myHandler = new Handler();
        myRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                largChar.setVisibility(View.GONE);
                removeCallbacks();
            }
        };
        myHandler.postDelayed(myRunnable, 2000);
    }
    private void removeCallbacks(){
        myHandler.removeCallbacks(myRunnable);
    }

    private void resetColorChar(){
        alf.setTextColor(Color.parseColor("#FCADADAD"));
        baa.setTextColor(Color.parseColor("#FCADADAD"));
        taa.setTextColor(Color.parseColor("#FCADADAD"));
        thaa.setTextColor(Color.parseColor("#FCADADAD"));
        geem.setTextColor(Color.parseColor("#FCADADAD"));
        h7aa.setTextColor(Color.parseColor("#FCADADAD"));
        khaa.setTextColor(Color.parseColor("#FCADADAD"));
        dal.setTextColor(Color.parseColor("#FCADADAD"));
        thal.setTextColor(Color.parseColor("#FCADADAD"));
        raa.setTextColor(Color.parseColor("#FCADADAD"));
        zain.setTextColor(Color.parseColor("#FCADADAD"));
        seen.setTextColor(Color.parseColor("#FCADADAD"));
        sheen.setTextColor(Color.parseColor("#FCADADAD"));
        saad.setTextColor(Color.parseColor("#FCADADAD"));
        daad.setTextColor(Color.parseColor("#FCADADAD"));
        ttaa.setTextColor(Color.parseColor("#FCADADAD"));
        thhaa.setTextColor(Color.parseColor("#FCADADAD"));
        aain.setTextColor(Color.parseColor("#FCADADAD"));
        gain.setTextColor(Color.parseColor("#FCADADAD"));
        faa.setTextColor(Color.parseColor("#FCADADAD"));
        gaaf.setTextColor(Color.parseColor("#FCADADAD"));
        kaaf.setTextColor(Color.parseColor("#FCADADAD"));
        laam.setTextColor(Color.parseColor("#FCADADAD"));
        meem.setTextColor(Color.parseColor("#FCADADAD"));
        noon.setTextColor(Color.parseColor("#FCADADAD"));
        haa.setTextColor(Color.parseColor("#FCADADAD"));
        oaao.setTextColor(Color.parseColor("#FCADADAD"));
        iaa.setTextColor(Color.parseColor("#FCADADAD"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchitem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hear));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String resultEditText = s;
                int textLength = resultEditText.length();
                sereenArrayListResult.clear();
                for (int i = 0; i < sereenArrayList.size(); i++) {
                    String resultNamelist = sereenArrayList.get(i).getTitle().trim();
                    if (!resultEditText.equals("")) {
                        constraintLayout5.setVisibility(View.INVISIBLE);
                        int xx = 0;
                        for (String ss : resultEditText.split(" ",2)) {
                            str[xx] = ss;
                            xx++;
                        }
                        if (textLength <= resultNamelist.length()) {
                            if (resultNamelist.toLowerCase(Locale.getDefault()).contains(str[0]) & resultNamelist.toLowerCase(Locale.getDefault()).contains(str[1])) {
                                sereenArrayListResult.add(sereenArrayList.get(i));
                            }
                        }
                    }
                    else {
                        constraintLayout5.setVisibility(View.VISIBLE);
                        sereenArrayListResult.add(sereenArrayList.get(i));
                    }
                }
                sereensAdapter.notifyDataSetChanged();
                return false;
            }
        });

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mainBanner2));
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchitem:
                break;
            default: onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
