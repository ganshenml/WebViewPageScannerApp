package com.example.administrator.webviewpagescannerapp.other;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.webviewpagescannerapp.activity.PhotoBrowserActivity;

/**
 * Created by Administrator on 2017/2/10.
 */

public class MJavascriptInterface {
    private Context context;
    private String [] imageUrls;

    public MJavascriptInterface(Context context,String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", imageUrls);
        intent.putExtra("curImageUrl", img);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
        for (int i = 0; i < imageUrls.length; i++) {
            Log.e("图片地址"+i,imageUrls[i].toString());
        }
    }
}
