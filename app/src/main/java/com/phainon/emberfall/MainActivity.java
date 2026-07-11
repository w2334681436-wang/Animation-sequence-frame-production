package com.phainon.emberfall;

import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.CookieManager;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView game;
    private static final String GAME_URL = "https://phainon-emberfall.opal-mint-9707.chatgpt.site";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.BLACK);
        createGameView();
    }

    private void createGameView() {
        try {
            enterFullscreen();
            game = new WebView(getApplicationContext());
            game.setBackgroundColor(0xff080607);
            WebSettings s = game.getSettings();
            s.setJavaScriptEnabled(true);
            s.setDomStorageEnabled(true);
            s.setDatabaseEnabled(true);
            s.setMediaPlaybackRequiresUserGesture(false);
            s.setUseWideViewPort(true);
            s.setLoadWithOverviewMode(true);
            s.setBuiltInZoomControls(false);
            s.setDisplayZoomControls(false);
            s.setCacheMode(WebSettings.LOAD_DEFAULT);
            s.setSupportZoom(false);
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().setAcceptThirdPartyCookies(game, true);
            game.setWebChromeClient(new WebChromeClient());
            game.setWebViewClient(new SafeClient());
            game.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            setContentView(game);
            game.loadUrl(GAME_URL);
        } catch (Throwable error) {
            showRecovery("游戏启动失败，请更新 Android System WebView 后重试。");
        }
    }

    private class SafeClient extends WebViewClient {
        @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (request.isForMainFrame()) showRecovery("网络连接失败，请检查网络后重新打开游戏。");
        }
        @Override public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            if (game != null) { game.destroy(); game = null; }
            createGameView();
            return true;
        }
    }

    private void showRecovery(String message) {
        WebView recovery = new WebView(getApplicationContext());
        recovery.setBackgroundColor(0xff080607);
        recovery.getSettings().setJavaScriptEnabled(true);
        recovery.loadDataWithBaseURL(null, "<html><meta name='viewport' content='width=device-width'><body style='margin:0;background:#080607;color:#f0c978;display:flex;align-items:center;justify-content:center;height:100vh;font-family:sans-serif;text-align:center'><div><h2>白厄：逐火残响</h2><p>"+message+"</p><button style='padding:12px 24px' onclick='location.reload()'>重新加载</button></div></body></html>", "text/html", "UTF-8", null);
        setContentView(recovery);
    }

    private void enterFullscreen() {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController c = getWindow().getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override public void onWindowFocusChanged(boolean focus) { super.onWindowFocusChanged(focus); if (focus) enterFullscreen(); }
    @Override public void onBackPressed() { if (game != null && game.canGoBack()) game.goBack(); else super.onBackPressed(); }
    @Override protected void onDestroy() { if (game != null) { game.stopLoading(); game.destroy(); game = null; } super.onDestroy(); }
}
