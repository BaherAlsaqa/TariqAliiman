package com.tariqaliiman.tariqaliiman.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Contains;
import com.tariqaliiman.tariqaliiman.Models.books.Book;
import com.tariqaliiman.tariqaliiman.Models.books.BooksBody;
import com.tariqaliiman.tariqaliiman.Models.books.BooksData;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.adapters.MyBooksAdapterPagination;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener3;
import com.tariqaliiman.tariqaliiman.listeners.PaginationScrollListener;
import com.tariqaliiman.tariqaliiman.retrofit.APIClient;
import com.tariqaliiman.tariqaliiman.retrofit.APIInterface;
import com.tariqaliiman.tariqaliiman.retrofit.ServiceGenerator;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksActivity extends AppCompatActivity {

    Call<BooksBody> call;
    //    APIInterface apiInterface;
    RecyclerView mRecyclerView;
    MyBooksAdapterPagination adapter;
    ArrayList<Book> booksArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finds
        mRecyclerView = findViewById(R.id.recyclerview);
        swiperefresh = findViewById(R.id.swiperefresh);
        progress = findViewById(R.id.progress);

        ///////////////TODO pagination settings//////////////
        isLoading = false;
        isLastPage = false;
        currentPage = PAGE_START;
        ////////////////////////////

        noInternet = findViewById(R.id.nointernet);
        emptyData = findViewById(R.id.emptydata);
        error = findViewById(R.id.error);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.titleview, null);
        ((TextView) v.findViewById(R.id.title1)).setText(getString(R.string.books));
        getSupportActionBar().setCustomView(v);

        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

//        apiInterface = APIClient.getClient().create(APIInterface.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize
        adapter = new MyBooksAdapterPagination(this);
        booksArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appSharedPreferences = new AppSharedPreferences(BooksActivity.this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        loadFirstPage();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage();
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

                        loadNextPage();
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

        adapter.setOnClickListener(new OnItemClickListener3() {
            @Override
            public void onItemClick(Book item) {
                Log.d(Constants.log+"id", "book id = "+item.getId());
            startActivity(new Intent(BooksActivity.this, FirstLevelActivity.class)
            .putExtra(Constants.bookId, item.getId())
            .putExtra(Constants.bookName, item.getNameAr()));
            }
        });

        adapter.setOnClickListenerBookInfo(new OnItemClickListener3() {
            @Override
            public void onItemClick(Book item) {
                startActivity(new Intent(BooksActivity.this, BookDetails.class)
                        .putExtra(Constants.bimage, item.getImage())
                        .putExtra(Constants.btitle, item.getNameAr())
                        .putExtra(Constants.bauther, item.getAuthorAr())
                        .putExtra(Constants.bdescription, item.getDescriptionAr()));
            }
        });

    }

    private ArrayList<Book> fetchResults(Response<BooksBody> response) {
        BooksBody BooksBody = response.body();
        assert BooksBody != null;
        BooksData homeItemsObject = BooksBody.getData();
        return homeItemsObject.getData();
    }

    public void loadFirstPage() {
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

        callTopRatedMoviesApi().enqueue(new Callback<BooksBody>() {
            @Override
            public void onResponse(@NotNull Call<BooksBody> call, @NotNull Response<BooksBody> response) {
                Log.d(Constants.log, "on response = " + response.message() + " | " + response.code());
                if (response.isSuccessful()) {
                    BooksBody resource = response.body();

                    assert response.body() != null;
                    boolean status = response.body().getSuccess();
                    String code = response.code() + "";

                    Log.d(Contains.log + "BooksBody", "Status = " + status + " | Code = " + code);

                    if (status) {
                        progress.setVisibility(View.INVISIBLE);
                        swiperefresh.setRefreshing(false);
                        adapter.clear();
                        assert resource != null;
                        booksArrayList = resource.getData().getData();
                        TOTAL_PAGES = resource.getData().getLastPage();
                        Log.d(Contains.LOG + "1", booksArrayList.size() + "");
                        adapter.addAll(booksArrayList);

                        if (booksArrayList.size() <= 0){
                            emptyData.setVisibility(View.VISIBLE);
                            adapter.clear();
                        }

                        if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }else{
                    Log.d(Constants.log+"error", "error = "+response.code());
                    error.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BooksBody> call, @NotNull Throwable t) {
                Log.d(Constants.log + "onFailure", "onFailure = " + t.getLocalizedMessage());
                Log.d(Constants.log + "onFailure", "onFailure = " + t.getMessage() + "");
                if (t instanceof IOException) {
                    swiperefresh.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                }
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void loadNextPage() {

        Log.d(Contains.LOG + "loadNextPage", "loadNextPage: " + currentPage);

        callTopRatedMoviesApi().enqueue(new Callback<BooksBody>() {
            @Override
            public void onResponse(@NotNull Call<BooksBody> call, @NotNull Response<BooksBody> response) {

                BooksBody resource = response.body();

                assert resource != null;
                boolean status = resource.getSuccess();
                int code = response.code();

                Log.d(Contains.LOG + "BooksBody", "Status = " + status + " | Code = " + code);

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
            public void onFailure(@NotNull Call<BooksBody> call, @NotNull Throwable t) {
                t.printStackTrace();
                if (t instanceof IOException) {
                    swiperefresh.setRefreshing(false);
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.VISIBLE);
                }
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });
    }

    private Call<BooksBody> callTopRatedMoviesApi() {
        APIInterface apiInterface = ServiceGenerator.createService(APIInterface.class, "book", "book123456");
        return call = apiInterface.getAllBooks(currentPage);
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


