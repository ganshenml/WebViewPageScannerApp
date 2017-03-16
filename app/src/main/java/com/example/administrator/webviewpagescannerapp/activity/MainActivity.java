package com.example.administrator.webviewpagescannerapp.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.example.administrator.webviewpagescannerapp.other.MJavascriptInterface;
import com.example.administrator.webviewpagescannerapp.other.MyWebViewClient;
import com.example.administrator.webviewpagescannerapp.R;
import com.example.administrator.webviewpagescannerapp.tool.StringUtils;

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
        contentWebView.getSettings().setAppCacheEnabled(true);
        contentWebView.getSettings().setDatabaseEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);
        contentWebView.loadUrl("http://a.mp.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&client=ucweb&wm_aid=c51bcf6c1553481885da371a16e33dbe&wm_id=482efebe15ed4922a1f24dc42ab654e6&pagetype=share&btifl=100");
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
