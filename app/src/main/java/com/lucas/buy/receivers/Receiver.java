package com.lucas.buy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 111 on 2017/8/4.
 */

public class Receiver extends BroadcastReceiver {
    private static final String TAG = "Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        Log.i(TAG,"...name:" + name);
    }
}
