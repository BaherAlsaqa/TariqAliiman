package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.tariqaliiman.tariqaliiman.Models.FirstLevel.FirstLevel;
import com.tariqaliiman.tariqaliiman.Models.FirstLevel.FirstLevelBody;
import com.tariqaliiman.tariqaliiman.Models.FirstLevel.FirstLevelData;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.adapters.MyFirstLevelsAdapterPagination;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener4;
import com.tariqaliiman.tariqaliiman.listeners.PaginationScrollListener;
import com.tariqaliiman.tariqaliiman.retrofit.APIInterface;
import com.tariqaliiman.tariqaliiman.retrofit.ServiceGenerator;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstLevelActivity extends AppCompatActivity {

    Call<FirstLevelBody> call;
    //    APIInterface apiInterface;
    RecyclerView mRecyclerView;
    MyFirstLevelsAdapterPagination adapter;
    ArrayList<FirstLevel> booksArrayList;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swiperefresh;
    AppSharedPreferences appSharedPreferences;
    AVLoadingIndicatorView progress;

    //pagination
    private static int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    private View noInternet, emptyData, error;
    private Integer bookId;
    private String bookName;
    private String search = "";

    private int x = 0;
    private InterstitialAd mInterstitialAd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finds
        mRecyclerView = findViewById(R.id.recyclerview);
        swiperefresh = findViewById(R.id.swiperefresh);
        progress = findViewById(R.id.progress);
        noInternet = findViewById(R.id.nointernet);
        emptyData = findViewById(R.id.emptydata);
        error = findViewById(R.id.error);

        ///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        // Interstitial Ads
        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId(getString(R.string.booksenter));
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

        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

//        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize
        adapter = new MyFirstLevelsAdapterPagination(this);
        booksArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appSharedPreferences = new AppSharedPreferences(FirstLevelActivity.this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra(Constants.bookId, 0) > 0) {
                bookId = intent.getIntExtra(Constants.bookId, 0);
                Log.d(Constants.log, "bookId is = " + bookId);
                bookName = intent.getStringExtra(Constants.bookName);
                LayoutInflater inflator = LayoutInflater.from(this);
                View v = inflator.inflate(R.layout.titleview, null);
                ((TextView) v.findViewById(R.id.title1)).setText(bookName);
                getSupportActionBar().setCustomView(v);
                loadFirstPage(bookId, "");
            }
        }

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage(bookId, "");
                search = "";
            }
        });

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(Contains.LOG + "addOnScroll", "addOnScrollListener");

                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(Contains.LOG + "addOnScroll", "addOnScrollListener");

                        loadNextPage(bookId, search);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        adapter.setOnClickListener(new OnItemClickListener4() {
            @Override
            public void onItemClick(FirstLevel item) {
            if (item.getIsEndLevel().equalsIgnoreCase("on")){
                startActivity(new Intent(FirstLevelActivity.this, LevelHadethActivity.class)
                .putExtra(Constants.levelId, item.getId())
                .putExtra(Constants.bookName, item.getTitleAr()));
            }else{
                startActivity(new Intent(FirstLevelActivity.this, SubLevelActivity.class)
                .putExtra(Constants.levelId, item.getId())
                .putExtra(Constants.bookName, item.getTitleAr()));
            }

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

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.booksbanner));
        adView = findViewById(R.id.adView);
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

    private ArrayList<FirstLevel> fetchResults(Response<FirstLevelBody> response) {
        FirstLevelBody FirstLevelBody = response.body();
        assert FirstLevelBody != null;
        FirstLevelData itemsObject = FirstLevelBody.getData();
        return itemsObject.getData();
    }

    public void loadFirstPage(int bookId, String search) {
        Log.d(Constants.log, "loadFirstPage()");
        ///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////
        progress.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
        emptyData.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        if (!isNetworkConnected()) {
            swiperefresh.setRefreshing(false);
            noInternet.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }

        callTopRatedMoviesApi(bookId, search).enqueue(new Callback<FirstLevelBody>() {
            @Override
            public void onResponse(@NotNull Call<FirstLevelBody> call, @NotNull Response<FirstLevelBody> response) {
                Log.d(Constants.log, "on response = " + response.message() + " | " + response.code());
                if (response.isSuccessful()) {
                    FirstLevelBody resource = response.body();
                    Log.d(Constants.log, response.body().toString());
                    assert response.body() != null;
                    boolean status = response.body().getSuccess();
                    String code = response.code() + "";

                    Log.d(Contains.log + "FirstLevelBody", "Status = " + status + " | Code = " + code);

                    if (status) {
                        progress.setVisibility(View.INVISIBLE);
                        swiperefresh.setRefreshing(false);
                        adapter.clear();
                        assert resource != null;
                        booksArrayList = resource.getData().getData();
                        TOTAL_PAGES = resource.getData().getLastPage();
                        Log.d(Contains.LOG + "1", booksArrayList.size() + "");
                        adapter.addAll(booksArrayList);

                        if (booksArrayList.size() <= 0) {
                            emptyData.setVisibility(View.VISIBLE);
                            adapter.clear();
                        }

                        if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                } else {
                    Log.d(Constants.log + "error", "error = " + response.code());
                    error.setVisibility(View.VISIBLE);
                    emptyData.setVisibility(View.GONE);
                    noInternet.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FirstLevelBody> call, @NotNull Throwable t) {
                Log.d(Constants.log + "onFailure", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.log + "onFailure", "onFailure = " + t.getMessage() + "");
                if (t instanceof IOException) {
                    swiperefresh.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                    emptyData.setVisibility(View.GONE);
                } else {
                    error.setVisibility(View.VISIBLE);
                    emptyData.setVisibility(View.GONE);
                    noInternet.setVisibility(View.GONE);
                }
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void loadNextPage(int bookId,  String search) {

        Log.d(Contains.LOG + "loadNextPage", "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(bookId, search).enqueue(new Callback<FirstLevelBody>() {
            @Override
            public void onResponse(@NotNull Call<FirstLevelBody> call, @NotNull Response<FirstLevelBody> response) {

                FirstLevelBody resource = response.body();

                assert resource != null;
                boolean status = resource.getSuccess();
                int code = response.code();

                Log.d(Contains.LOG + "FirstLevelBody", "Status = " + status + " | Code = " + code);

                if (status) {

                    booksArrayList = fetchResults(response);

                    if (isLoading) {
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        adapter.addAll(booksArrayList);
                    }

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(@NotNull Call<FirstLevelBody> call, @NotNull Throwable t) {
                t.printStackTrace();
                if (t instanceof IOException) {
                    swiperefresh.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                    error.setVisibility(View.GONE);
                    emptyData.setVisibility(View.GONE);
                } else {
                    error.setVisibility(View.VISIBLE);
                    noInternet.setVisibility(View.GONE);
                    emptyData.setVisibility(View.GONE);
                }
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });
    }

    private Call<FirstLevelBody> callTopRatedMoviesApi(int bookId, String search) {
        Log.d(Constants.log, "book id call = " + bookId);
        APIInterface apiInterface = ServiceGenerator.createService(APIInterface.class, "book", "book123456");
        if (search.equals("")||search.length() <= 0)
            call = apiInterface.getFirstLevel(bookId, currentPage);
        else
            call = apiInterface.searchLevels(search, currentPage);
        return call;
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
                if (s.length() > 0) {
                    loadFirstPage(0, s);
                    search = s;
                }else{
                    loadFirstPage(bookId, s);
                }
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
            default: onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        } else {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

}
