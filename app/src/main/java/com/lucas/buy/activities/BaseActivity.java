package com.lucas.buy.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.lucas.buy.domain.Domine;
import com.lucas.buy.domain.Employee;
import com.lucas.buy.domain.MData;
import com.lucas.buy.interfaces.IDataCallBack;
import com.lucas.buy.interfaces.IHandler;
import com.lucas.buy.utils.UIHandler;

/**
 * Created by 111 on 2017/7/7.
 */

public abstract class BaseActivity extends Activity{
    // 可以把常量单独放到一个Class中
    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_PUSH_DATA = "fm.data.push.action";
    public static final String ACTION_NEW_VERSION = "apk.update.action";


    protected static UIHandler handler = new UIHandler(Looper.myLooper());

    //数据回调接口，都传递Domine的子类实体
    protected IDataCallBack<MData<? extends Domine>> dataCallback;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBase();
        setHandler();
        initContentView(savedInstanceState);
    }

    //可能全屏，或者没有ActionBar
    private void setBase() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void setHandler() {
        handler.setHandler(new IHandler() {
            @Override
            public void handleMessage(Message msg) {
                handler(msg);
            }
        });
    }

    protected abstract void handler(Message msg);

    //初始化UI setContentView等
    protected abstract void initContentView(Bundle savedInstanceState);

    protected void addLeftMenu(boolean enable) {
        //如果你的项目有侧滑栏，可以处理此方法
        if(enable){

        } else {

        }
    }

    //横屏竖屏键盘等
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //可以添加多个Action捕捉
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORK_CHANGE);
        filter.addAction(ACTION_PUSH_DATA);
        filter.addAction(ACTION_NEW_VERSION);

        registerReceiver(receiver, filter);
        //还可能发送统计数据，比如第三方sdk，做统计需求
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //处理各种情况
            String action = intent.getAction();
            if(ACTION_NETWORK_CHANGE.equals(action)) {
                //
            } else if(ACTION_PUSH_DATA.equals(action)) {
                Bundle b = intent.getExtras();
                MData<Employee> mdata = (MData<Employee>) b.get("data");
                if(mdata!= null) {
                    dataCallback.onNewData(mdata);
                }
            } else if(ACTION_NEW_VERSION.equals(action)) {

            }
        }
    };

    public void setDataCallback(IDataCallBack<MData<? extends Domine>> dataCallback) {
        this.dataCallback = dataCallback;
    }
}
