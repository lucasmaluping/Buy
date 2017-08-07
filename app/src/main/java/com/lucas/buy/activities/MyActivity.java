package com.lucas.buy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lucas.buy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 111 on 2017/8/2.
 */

public class MyActivity extends Activity {
    @BindView(R.id.edit_message) EditText username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        ButterKnife.bind(this);
        View v = new View(this);
    }

    @OnClick(R.id.my_btn) void click() {
        username.setText("hello world!");
    }


}
