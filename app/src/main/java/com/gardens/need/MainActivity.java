package com.gardens.need;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog pd = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
        webView=(WebView)findViewById(R.id.webview);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new Myappwebclient());
        webSettings.setDomStorageEnabled(true);
       /* webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);*/
     //   webView.addJavascriptInterface(new MyJavascriptInterface(this), "Android");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // prDialog = ProgressDialog.show(MainActivity.this, null, "loading, please wait...");
                super.onPageStarted(view, url, favicon);
                // prDialog.dismiss();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                //prDialog.dismiss();
                super.onPageFinished(view, url);

            }

        });

        //loading webpage
        webView.loadUrl("https://www.gardensneed.com/ocart/index.php?route=product/allcategories");

    }

    @Override
    public void onBackPressed() {


        if(webView.canGoBack())
        {
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    public class Myappwebclient extends WebViewClient
    {

        // dialog.dismiss();

    }

}
