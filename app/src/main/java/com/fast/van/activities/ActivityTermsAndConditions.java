package com.fast.van.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.transformer.Transformer;

/**
 * Created by Amandeep Singh Bagli on 24/12/15.
 */
public class ActivityTermsAndConditions extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_termsandconditions);
        findViewById(R.id.backbutton).setOnClickListener(this);
        TextView title=(TextView)findViewById(R.id.screenTitle);
        title.setText("Terms & Conditions");
        WebView webView = (WebView) findViewById(R.id.termsandconditionsPageWeb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://fastvan.com/terms&conditions_app.html");

    }

    @Override
    public void onClickView(View view) {

        onBackPressed();

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            LoadingBox.dismissLoadingDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LoadingBox.showLoadingDialog(activity, "Loading...");
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Transformer.leftToRight(activity);

    }
}
