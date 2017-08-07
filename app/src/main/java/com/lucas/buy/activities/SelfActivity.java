package com.lucas.buy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lucas.buy.R;



/**
 * Created by 111 on 2017/8/1.
 */

public class SelfActivity extends Activity{
    private static final String TAG = "SelfActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG,"...hasFocus:" + hasFocus);
    }
}
