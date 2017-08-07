package com.lucas.buy.activities;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by 111 on 2017/7/8.
 */

public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;
    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    @Override
    public void onCreate() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(getRequestQueue(), new VolleyImageCache());

        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59841c04");




        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
        super.onCreate();

    }

    class VolleyImageCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;
        public VolleyImageCache() {
            int maxCacheSize = 1024 * 1024 * 10;
            mCache = new LruCache<String, Bitmap>(maxCacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
