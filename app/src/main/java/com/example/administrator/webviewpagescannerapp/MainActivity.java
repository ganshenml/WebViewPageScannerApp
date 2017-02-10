package com.example.administrator.webviewpagescannerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {
    private WebView contentWebView = null;

    private String[] imageUrls = StringUtils.returnImageUrlsFromHtml();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentWebView = (WebView) findViewById(R.id.webView);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.loadUrl("http://m.dgd.vc/zhongchou/appDetail/1249");
        contentWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls), "imagelistener");
        contentWebView.setWebViewClient(new MyWebViewClient());

    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(MainActivity.this).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
    }
}
