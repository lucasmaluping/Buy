package com.lucas.buy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lucas.buy.R;

/**
 * Created by 111 on 2017/8/4.
 */

public class BroadCaseTestActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_layout);
        findViewById(R.id.broadcast_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.lucas");
                intent.putExtra("name", "lucas");
                sendBroadcast(intent);
            }
        });


    }
}
