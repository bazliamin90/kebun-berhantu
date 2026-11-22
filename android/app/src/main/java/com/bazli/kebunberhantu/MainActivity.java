package com.bazli.kebunberhantu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Kebun Berhantu Android shell.
 * The game itself lives on GitHub Pages; this app is a full-screen window onto it,
 * so updating the website updates the app too.
 */
public class MainActivity extends Activity {

    private WebView web;
    private String gameUrl;
    private String gameHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameUrl = getString(R.string.game_url);
        gameHost = Uri.parse(gameUrl).getHost();

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#0b0816"));
        setContentView(web);
        hideSystemUi();

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);          // saves name, records, options
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT); // always fetch the latest version when online
        s.setUserAgentString(s.getUserAgentString() + " KebunBerhantuApp/1.0");

        web.addJavascriptInterface(new AppBridge(), "KBApp");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                if ("file".equals(u.getScheme())) return false;
                if (u.getHost() != null && u.getHost().equals(gameHost)) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) { }
                return true; // other sites open in the phone's browser
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) view.loadUrl("file:///android_asset/offline.html");
            }
        });

        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl(gameUrl);
    }

    /** Lets the offline page ask the app to try again. */
    private class AppBridge {
        @JavascriptInterface
        public void retry() {
            runOnUiThread(() -> web.loadUrl(gameUrl));
        }
    }

    @Override
    public void onBackPressed() {
        // Ask the game first: it goes back to the menu, and ignores Back during a round.
        web.evaluateJavascript("(window.kbBack&&window.kbBack())?'1':'0'", value -> {
            if (!"\"1\"".equals(value)) {
                if (web.canGoBack()) web.goBack();
                else finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
        web.resumeTimers();
        hideSystemUi();
    }

    @Override
    protected void onPause() {
        web.onPause();
        web.pauseTimers();
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    protected void onDestroy() {
        if (web != null) web.destroy();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    private void hideSystemUi() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
