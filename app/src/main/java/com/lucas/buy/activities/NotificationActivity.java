package com.lucas.buy.activities;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lucas.buy.R;
import com.lucas.buy.utils.NotificationsUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 111 on 2017/8/9.
 */

public class NotificationActivity extends BaseActivity{
    @Override
    protected void handler(Message msg) {

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.notification_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.notification_btn)
    void onClick() {
        boolean flag = NotificationsUtils.isNotificationEnabled(this);
        Toast.makeText(this,"....flag" + flag,Toast.LENGTH_SHORT).show();
        if(!flag) {
            NotificationsUtils.requestPermission(0, this);
        }


        //通知管理器
        NotificationManager notificationManager = (NotificationManager)getSystemService(Service.NOTIFICATION_SERVICE);

        Notification.Builder builder1 = new Notification.Builder(NotificationActivity.this);
        builder1.setSmallIcon(R.mipmap.ic_launcher); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (NotificationActivity.this,NotificationActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        notificationManager.notify(124, notification1); // 通过通知管理器发送通知

        //通知
//        Notification notification = new Notification.Builder(NotificationActivity.this)
//                .setAutoCancel(true)
//                .setContentTitle("title")
//                .setContentText("describe")
////                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setWhen(System.currentTimeMillis())
//                .build();
//
//        notificationManager.notify(124, notification); // 通过通知管理器发送通知

    }
}
