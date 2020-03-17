package com.tariqaliiman.tariqaliiman.activities;

import android.content.Intent;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.adapters.MuslimsAdapter;
import com.tariqaliiman.tariqaliiman.classes.Muslim;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "baherb";
    private RecyclerView recyclerView;
    private MuslimsAdapter muslimsAdapter;
    private ArrayList<Muslim> muslimArrayList;
    private ArrayList<Muslim> muslimArrayListResult;
    private LinearLayoutManager linearLayoutManager;
    String[] str = {"", "", "", ""};
    private AppSharedPreferences appSharedPreferences;
    private int x = 0;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd1;
    private InterstitialAd mInterstitialAd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Find view
        recyclerView = findViewById(R.id.recyclerView);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);
        ((TextView) v.findViewById(R.id.title1)).setText(getString(R.string.muslim));
        getSupportActionBar().setCustomView(v);

        //Initialize
        muslimArrayList = new ArrayList<>();
        muslimArrayListResult = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appSharedPreferences = new AppSharedPreferences(getApplicationContext());


        Resources resources = getResources();
        int[] ids = resources.getIntArray(R.array.muslim_ids);
        String[] titles = resources.getStringArray(R.array.muslim_titles);
        String[] contents = resources.getStringArray(R.array.muslim_contents);
        String[] references = resources.getStringArray(R.array.muslim_references);

        Log.d(TAG, "titles.length = " + ids.length);
        for (int i = 0; i < ids.length; i++) {
            Muslim muslim = new Muslim(ids[i], titles[i], contents[i], references[i]);
            muslimArrayList.add(muslim);
            muslimArrayListResult.add(muslim);
        }

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // Interstitial Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter4));
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

        muslimsAdapter = new MuslimsAdapter(muslimArrayListResult, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(muslimsAdapter);

        muslimsAdapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Muslim item) {
                if (item.getId() == 1 | item.getId() == 2) {
                    startActivity(new Intent(MainActivity.this, SabahMasaaDetails.class)
                            .putExtra(Constants.athkar_id, item.getId())
                            .putExtra(Contains.name, item.getName()));
                }else{
                    startActivity(new Intent(MainActivity.this, MuslimsDetails.class)
                            .putExtra(Contains.name, item.getName())
                            .putExtra(Contains.content, item.content)
                            .putExtra(Contains.reference, item.reference));
                }
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

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mainBanner1));
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
                muslimArrayListResult.clear();
                for (int i = 0; i < muslimArrayList.size(); i++) {
                    String resultNamelist = muslimArrayList.get(i).getName().trim();
                    if (!resultEditText.equals("")) {
                        int xx = 0;
                        for (String ss : resultEditText.split(" ", 2)) {
                            str[xx] = ss;
                            xx++;
                        }
                        if (textLength <= resultNamelist.length()) {
                            if (resultNamelist.toLowerCase(Locale.getDefault()).contains(str[0]) & resultNamelist.toLowerCase(Locale.getDefault()).contains(str[1])) {
                                muslimArrayListResult.add(muslimArrayList.get(i));
                            }
                        }
                    } else
                        muslimArrayListResult.add(muslimArrayList.get(i));
                }
                muslimsAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchitem:

                break;

            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
