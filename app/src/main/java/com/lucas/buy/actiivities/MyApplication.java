package com.lucas.buy.actiivities;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

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
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(getRequestQueue(), new VolleyImageCache());


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
