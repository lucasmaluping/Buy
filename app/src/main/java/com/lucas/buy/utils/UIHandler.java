package com.lucas.buy.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.lucas.buy.interfaces.IHandler;

/**
 * Created by 111 on 2017/8/7.
 */

public class UIHandler extends Handler {
    private IHandler handler;//回调接口，消息传给注册者

    public UIHandler(Looper looper) {
        super(looper);
    }

    public UIHandler(Looper looper, IHandler handler) {
        super(looper);
        this.handler = handler;
    }

    public void setHandler(IHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (handler != null) {
            handler.handleMessage(msg);
        }
    }
}


