package com.lucas.buy.interfaces;

/**
 * Created by 111 on 2017/8/7.
 */

public interface IDataCallBack<T> {
    public void onNewData(T data);
    public void onError(String msg, int code);
}
