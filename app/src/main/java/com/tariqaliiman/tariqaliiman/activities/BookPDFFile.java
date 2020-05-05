package com.tariqaliiman.tariqaliiman.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import com.tariqaliiman.tariqaliiman.utils.AppSharedPreferences;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BookPDFFile extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private static final String TAG = "";
    private PDFView pdfView;
    private Integer pageNumber = 0;
    private String pdfFileName;
    private View v;
    private AppSharedPreferences appSharedPreferences;
    private int x = 0;
    String bookName;
    private AVLoadingIndicatorView progress;
//    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_kareem);

        pdfView = findViewById(R.id.pdfView);
        progress = findViewById(R.id.progress);
//        webView = findViewById(R.id.webview);

        appSharedPreferences = new AppSharedPreferences(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null){
            if (intent.getIntExtra(Constants.pageNumber, 0) > -1) {
                pageNumber = intent.getIntExtra(Constants.pageNumber, 0)-1;
                pdfFileName = intent.getStringExtra(Constants.pdfFile);
            }
        }

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LayoutInflater inflator = LayoutInflater.from(this);
        v = inflator.inflate(R.layout.titleview, null);
        bookName = appSharedPreferences.readString(Constants.bookName);
        ((TextView)v.findViewById(R.id.title1)).setText(bookName);
        getSupportActionBar().setCustomView(v);

        // Initialize Calss Mobile Ads
        MobileAds.initialize(this, getString(R.string.IDAPP));

        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                getString(R.string.app_folder_path) +
                getString(R.string.book_folder_path) +
                "/"+pdfFileName.split("/")[1]);

        pdfView.fromFile(pdfFile)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .defaultPage(pageNumber)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(5)
                .autoSpacing(false)
                .pageFitPolicy(FitPolicy.WIDTH)
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .onPageChange(BookPDFFile.this)
                .enableAnnotationRendering(true)
                .onLoad(BookPDFFile.this)
                .scrollHandle(new DefaultScrollHandle(BookPDFFile.this))
                .onPageError(BookPDFFile.this)
                .load();

        /*try {
            progress.setVisibility(View.VISIBLE);
            Log.d(Constants.log+"pdf", "pdf url = "+Constants.pdfURL+pdfFileName);
            new RetrievePdfStream().execute(Constants.pdfURL+pdfFileName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constants.log+"pdf", "pdf url catch = "+Constants.pdfURL+pdfFileName);
        }*/
        /*Log.d(Constants.log, "URL = "+Constants.pdfURL+pdfFileName);
        progressBar.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constants.pdfURL+pdfFileName);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.d(Constants.log+"web", "shouldOverrideUrlLoading = "+url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(Constants.log+"web", "shouldOverrideUrlLoading = "+request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(Constants.log+"web", "onReceivedError = "+error.getErrorCode());
                Log.d(Constants.log+"web", "onReceivedError description = "+error.getDescription());
                Log.d(Constants.log+"web", "Error = "+error.toString());
                super.onReceivedError(view, request, error);
            }

            public void onPageFinished(WebView view, String url){
                // do your stuff here
                Log.d(Constants.log+"web", "onPageFinished");
                progressBar.setVisibility(View.GONE);
            }
        });*/

        // View Ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.pdfbookbanner));
        adView = findViewById(R.id.adView);
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

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        String pageNum = String.format("%s %s / %s", bookName.length() > 14 ?
                bookName.substring(0, 15) : bookName+"..", page + 1, pageCount);
        ((TextView)v.findViewById(R.id.title1)).setText(pageNum);
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.d("logerroe", "Cannot load page " + page);
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

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            Log.d(Constants.log+"pdf", "doInBackground");
            try {
                Log.d(Constants.log+"pdf", "doInBackground >> try");
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    Log.d(Constants.log+"pdf", "doInBackground >> 200 = "+urlConnection.getResponseCode());
                }else{
                    Log.d(Constants.log+"pdf", "doInBackground >> != 200 = "+urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.d(Constants.log+"pdf", "doInBackground catch");
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            Log.d(Constants.log+"pdf", "onPostExecute = "+inputStream.toString());
            pdfView.fromStream(inputStream)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .defaultPage(pageNumber)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(5)
                    .autoSpacing(false)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .pageSnap(true) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .onPageChange(BookPDFFile.this)
                    .enableAnnotationRendering(true)
                    .onLoad(BookPDFFile.this)
                    .scrollHandle(new DefaultScrollHandle(BookPDFFile.this))
                    .onPageError(BookPDFFile.this)
                    .load();
            progress.setVisibility(View.GONE);
        }
    }

}
