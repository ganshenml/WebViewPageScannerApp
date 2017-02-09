package com.example.administrator.webviewpagescannerapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoBrowserActivity extends Activity implements View.OnClickListener {
    private ImageView crossIv;
    private ViewPager mPager;
    private TextView photoOrderTv;
    private TextView saveTv;
    private String curImageUrl = "";
    private String[] imageUrls = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_browser);
        imageUrls = getIntent().getStringArrayExtra("imageUrls");
        curImageUrl = getIntent().getStringExtra("curImageUrl");

        photoOrderTv = (TextView) findViewById(R.id.photoOrderTv);
        saveTv = (TextView) findViewById(R.id.saveTv);
        saveTv.setOnClickListener(this);
        crossIv = (ImageView) findViewById(R.id.crossIv);
        crossIv.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }


            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                if (imageUrls[position] != null && !"".equals(imageUrls[position])) {
                    Log.e("instantiateItem", "进入了");
                    photoOrderTv.setText((position + 1) + "/" + imageUrls.length);
                    final PhotoView view = new PhotoView(PhotoBrowserActivity.this);
                    view.enable();
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    final Handler handle = new Handler() {
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 0:
                                    Bitmap bmp = (Bitmap) msg.obj;
                                    view.setImageBitmap(bmp);
                                    break;
                            }
                        }
                    };

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bmp = returnBitMap(imageUrls[position]);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = bmp;
                            handle.sendMessage(msg);
                        }
                    }).start();

                    container.addView(view);
                    return view;
                }
                return null;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        });
        mPager.setCurrentItem(returnClickedPosition() == -1 ? 0 : returnClickedPosition(), true);
        Log.e("点击的图片所处的position是", "" + returnClickedPosition());
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                photoOrderTv.setText((position + 1) + "/" + imageUrls.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Bitmap returnBitMap(final String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp == null) {
            Log.e("bitmap", "获取图片失败");
        }
        return bmp;
    }

    private int returnClickedPosition() {
        if (imageUrls == null || curImageUrl == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.length; i++) {
            if (curImageUrl.equals(imageUrls[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crossIv:
                finish();
                break;
            case R.id.saveTv:
                Toast.makeText(PhotoBrowserActivity.this, "点击了保存", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
