package com.tariqaliiman.tariqaliiman.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tariqaliiman.tariqaliiman.R;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    private WebView info_web;
    private ProgressBar progressBar;
    int k = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        info_web = (WebView) findViewById(R.id.webview_company_info);
        info_web.setBackgroundColor(Color.TRANSPARENT);
        info_web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        info_web.setWebViewClient(new myWebClient());
        info_web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        info_web.getSettings().setJavaScriptEnabled(true);
        String infoText = getString(R.string.company_info_web);
        info_web.loadDataWithBaseURL("file:///android_asset/", getWebViewText(infoText), "text/html", "utf-8", null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private String getWebViewText(String text) {

        return String.format("<html>\n" +
                "<head>\n" +
                "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"simple.otf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: large;\n" +
                "    text-align: justify;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body dir=\"rtl\" style=\"color:white\">\n" +
                " %s " +
                "</body>\n" +
                "</html>", text);
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
