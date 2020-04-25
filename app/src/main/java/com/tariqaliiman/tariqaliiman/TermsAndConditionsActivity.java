package com.tariqaliiman.tariqaliiman;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


import com.google.android.material.appbar.AppBarLayout;


public class TermsAndConditionsActivity extends AppCompatActivity {
    public static final String EXTRA_DISPLAY_ONLY = "extra_display_only";

    private boolean displayOnly = false;
    private Button btnAgree;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private WebView webView;
    private String url = "https://albseet.com/privacypolicy/privacy_policy.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        btnAgree = findViewById(R.id.btn_agree);
        appbar = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar1);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

// you need to setWebViewClient for forcefully open in your webview
        webView.setWebViewClient(new WebViewClient() {
            boolean success = true;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                success = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                btnAgree.setEnabled(success);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                btnAgree.setEnabled(success);
            }
        });

        /*mBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions);
        mBinding.btnAgree.setOnClickListener(this);
        mBinding.scrollView.getSettings().setJavaScriptEnabled(true);
        mBinding.scrollView.loadUrl("https://cdn.rawgit.com/alphamu/PrayTime-Android/a6f942f9/privacypolicy.html");
        mBinding.scrollView.setWebViewClient(new WebViewClient() {
            boolean success = true;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                success = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mBinding.btnAgree.setEnabled(success);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                mBinding.btnAgree.setEnabled(success);
            }
        });*/

        if (getIntent().hasExtra(EXTRA_DISPLAY_ONLY) && getIntent().getBooleanExtra(EXTRA_DISPLAY_ONLY, false)) {
            btnAgree.setVisibility(View.GONE);
            displayOnly = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            appbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void finish() {
        if (displayOnly) {
            setResult(RESULT_OK);
            overridePendingTransition(R.anim.no_animation, R.anim.exit_from_bottom);
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (displayOnly) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}