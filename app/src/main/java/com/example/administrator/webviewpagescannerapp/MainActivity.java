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

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {
    private WebView contentWebView = null;
//    private String[] imageUrls = new String[]{"http://img1.zczj.com/images/20170109/20170109154702.png", "http://img1.zczj.com/images/20170109/20170109165156.png", "http://img1.zczj.com/images/20170109/20170109165212.png"
//            , "http://img1.zczj.com/images/20170109/20170109165229.png", "http://img1.zczj.com/images/20170109/20170109160730.png", "http://img1.zczj.com/images/20170109/20170109160805.png", "http://img1.zczj.com/images/20170109/20170109165249.png",
//            "http://img1.zczj.com/images/20170109/20170109163153.png", "http://img1.zczj.com/images/20170109/20170109165319.png", "http://img1.zczj.com/images/20170109/20170109180910.jpg"};

    private String [] imageUrls = StringUtils.returnImageUrlsFromHtml();

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentWebView = (WebView) findViewById(R.id.webView);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.loadUrl("http://m.dgd.vc/zhongchou/appDetail/1249");
        contentWebView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        contentWebView.setWebViewClient(new MyWebViewClient());

    }

    private void addImageClickListner() {
        contentWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            Log.e("openImage", "进入了");
            Intent intent = new Intent();
            intent.putExtra("imageUrls", imageUrls);
            intent.putExtra("curImageUrl", img);
            intent.setClass(context, PhotoBrowserActivity.class);
            context.startActivity(intent);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

    }
}
