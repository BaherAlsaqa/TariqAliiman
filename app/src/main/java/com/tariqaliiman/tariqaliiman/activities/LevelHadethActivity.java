package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.Models.LevelHadeth.LevelHadeth;
import com.tariqaliiman.tariqaliiman.Models.LevelHadeth.LevelHadethBody;
import com.tariqaliiman.tariqaliiman.Models.LevelHadeth.LevelHadethData;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.adapters.MyLevelHadethAdapterPagination;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener5;
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

public class LevelHadethActivity extends AppCompatActivity {

    Call<LevelHadethBody> call;
    //    APIInterface apiInterface;
    RecyclerView mRecyclerView;
    MyLevelHadethAdapterPagination adapter;
    ArrayList<LevelHadeth> booksArrayList;
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
    private Integer levelId;
    private String bookName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_hadeth);

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

        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

//        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize
        adapter = new MyLevelHadethAdapterPagination(this);
        booksArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appSharedPreferences = new AppSharedPreferences(LevelHadethActivity.this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra(Constants.levelId, 0) > 0) {
                levelId = intent.getIntExtra(Constants.levelId, 0);
                Log.d(Constants.log, "levelId is = " + levelId);
                bookName = intent.getStringExtra(Constants.bookName);
                LayoutInflater inflator = LayoutInflater.from(this);
                View v = inflator.inflate(R.layout.titleview, null);
                ((TextView) v.findViewById(R.id.title1)).setText(bookName);
                getSupportActionBar().setCustomView(v);
                loadFirstPage(levelId);
            }
        }

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage(levelId);
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

                        loadNextPage(levelId);
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

        adapter.setOnClickListener(new OnItemClickListener5() {
            @Override
            public void onItemClick(LevelHadeth item) {
//            startActivity(new Intent(FirstLevelActivity.this, BookDetails.class));
                Toast.makeText(LevelHadethActivity.this, "level hadeth data pdf", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<LevelHadeth> fetchResults(Response<LevelHadethBody> response) {
        LevelHadethBody LevelHadethBody = response.body();
        assert LevelHadethBody != null;
        LevelHadethData itemsObject = LevelHadethBody.getData();
        return itemsObject.getData();
    }

    public void loadFirstPage(int levelId) {
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

        callTopRatedMoviesApi(levelId).enqueue(new Callback<LevelHadethBody>() {
            @Override
            public void onResponse(@NotNull Call<LevelHadethBody> call, @NotNull Response<LevelHadethBody> response) {
                Log.d(Constants.log, "on response = " + response.message() + " | " + response.code());
                if (response.isSuccessful()) {
                    LevelHadethBody resource = response.body();
                    Log.d(Constants.log, response.body().toString());
                    assert response.body() != null;
                    boolean status = response.body().getSuccess();
                    String code = response.code() + "";

                    Log.d(Contains.log + "LevelHadethBody", "Status = " + status + " | Code = " + code);

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
            public void onFailure(@NotNull Call<LevelHadethBody> call, @NotNull Throwable t) {
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

    public void loadNextPage(int levelId) {

        Log.d(Contains.LOG + "loadNextPage", "loadNextPage: " + currentPage);

        callTopRatedMoviesApi(levelId).enqueue(new Callback<LevelHadethBody>() {
            @Override
            public void onResponse(@NotNull Call<LevelHadethBody> call, @NotNull Response<LevelHadethBody> response) {

                LevelHadethBody resource = response.body();

                assert resource != null;
                boolean status = resource.getSuccess();
                int code = response.code();

                Log.d(Contains.LOG + "LevelHadethBody", "Status = " + status + " | Code = " + code);

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
            public void onFailure(@NotNull Call<LevelHadethBody> call, @NotNull Throwable t) {
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

    private Call<LevelHadethBody> callTopRatedMoviesApi(int levelId) {
        Log.d(Constants.log, "book id call = " + levelId);
        APIInterface apiInterface = ServiceGenerator.createService(APIInterface.class,
                getString(R.string.username), getString(R.string.password));
        return call = apiInterface.getLevelHadiths(levelId, currentPage);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        onBackPressed();

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