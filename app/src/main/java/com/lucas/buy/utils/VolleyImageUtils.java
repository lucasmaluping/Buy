package com.lucas.buy.utils;

import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.lucas.buy.R;
import com.lucas.buy.activities.MyApplication;

/**
 * Created by 111 on 2017/7/12.
 */

public class VolleyImageUtils {
    public static void loadImage(String url, View view) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener((ImageView)view, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        MyApplication.getImageLoader().get(url, listener);
    }
}
