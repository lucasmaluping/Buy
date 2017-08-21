package com.lucas.buy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Created by 111 on 2017/8/14.
 */

public class ProximityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnter = intent.getBooleanExtra( LocationManager.KEY_PROXIMITY_ENTERING, false);
        if(isEnter) {
            Toast.makeText(context, "你已到达广西电力实验楼附近", Toast.LENGTH_LONG).show();
        } else {
             Toast.makeText(context, "你已离开广西电力实验楼附近", Toast.LENGTH_LONG).show();
        }
    }
}
