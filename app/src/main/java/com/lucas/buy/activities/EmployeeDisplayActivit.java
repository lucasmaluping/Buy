package com.lucas.buy.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;

import com.lucas.buy.R;
import com.lucas.buy.domain.Domine;
import com.lucas.buy.domain.Employee;
import com.lucas.buy.domain.MData;
import com.lucas.buy.interfaces.IDataCallBack;

import java.util.List;

/**
 * Created by 111 on 2017/8/7.
 */

public class EmployeeDisplayActivit extends BaseActivity implements IDataCallBack<MData<? extends Domine>>  {
    public EmployeeDisplayActivit() {
    }



    @Override
    public void onNewData(MData<? extends Domine> data) {
        //updata UI 更新UI数据
        final List<Employee> list = (List<Employee>) data.dataList;
        handler.post(new Runnable() {
            @Override
            public void run() {
                //更新UI
            }
        });

        //或者
        handler.sendEmptyMessage(0);//通知handler
    }

    @Override
    public void onError(String msg, int code) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //通知错误消息
            }
        });
        //or
        handler.sendEmptyMessage(0);//通知handler
    }

    @Override
    protected void handler(Message msg) {
        switch (msg.what) {
            case 0:

                break;
            case 1:

                break;
            default:

                break;
        }
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.employeedisplay_layout);
        loadData();
    }

    private void loadData() {
        setDataCallback(this);//设置回调函数
        //我们还可以把这个Callback传给其它获取数据的类，比如HTTP获取数据
        //比如 HttClient.get(url,this);
    }


}
