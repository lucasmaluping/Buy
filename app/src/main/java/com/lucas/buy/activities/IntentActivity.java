package com.lucas.buy.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lucas.buy.R;

import java.util.ArrayList;

/**
 * Created by 111 on 2017/8/21.
 */

public class IntentActivity extends BaseActivity {
    private static final String TAG = "IntentActiviy";

    @Override
    protected void handler(Message msg) {

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.intent_layout);

        //打开网页
//        Uri uri = Uri.parse("https://www.baidu.com");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);

        //定位
//        Uri u = Uri.parse("geo:38.899533,-77.036476");
//        startActivity(new Intent(Intent.ACTION_VIEW, u));

        //路径规划
//        startActivity(new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en")));

        //拨打电话
//        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15192266473")));

        //发送短信
//        Uri uri1 = Uri.parse("smsto:15192266473");
//        Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri1);
//        intent1.putExtra("sms_body","hello nimoo");
//        startActivity(intent1);

        //发送邮件
//        Uri uriEmail = Uri.parse("mailto:luckynimoo@sina.com");
//        Intent intentEmail = new Intent(Intent.ACTION_SENDTO, uriEmail);
//        intentEmail.putExtra(Intent.EXTRA_TEXT,"hello nimoo");
//        startActivity(intentEmail);

//        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
//        intentEmail.putExtra(Intent.EXTRA_EMAIL, "luckynimoo@sina.com");
//        intentEmail.putExtra(Intent.EXTRA_TEXT, "hello nimoo");
//        intentEmail.setType("text/plain");
//        startActivity(Intent.createChooser(intentEmail, "Choose Email Client"));          //没有应用可执行此操作


        //搜索应用
//        Uri uri = Uri.parse("market://search?q=pname:pkg_name");
//        startActivity(new Intent(Intent.ACTION_VIEW, uri));

        //后台发送短信
        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);
        // register the Broadcast Receivers
        this.registerReceiver(new SendReceiver(), new IntentFilter(SENT_SMS_ACTION));

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
// create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0);
        this.registerReceiver(new DeliverReceiver(), new IntentFilter(DELIVERED_SMS_ACTION));
        String message = "hello";
        // 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
        if (message != null) {
            SmsManager smsManager = SmsManager.getDefault();
            //如果短息没有超过长度，则返回一个list
            ArrayList<String> strings = smsManager.divideMessage(message);
            Log.i(TAG, "...strings:" + strings);
            for (String s : strings) {
                smsManager.sendTextMessage("15110141032", null, s, sentPI, deliverPI);
            }
        }


    }

    public class SendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "....code:" + getResultCode() + "...data:" + getResultData());
            switch (getResultCode()) {

                case Activity.RESULT_OK:
                    Toast.makeText(IntentActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    break;
            }
        }
    }

    public class DeliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(IntentActivity.this,
                    "收信人已经成功接收", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
