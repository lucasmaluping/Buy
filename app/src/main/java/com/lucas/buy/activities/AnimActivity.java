package com.lucas.buy.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lucas.buy.R;

/**
 * Created by 111 on 2017/7/31.
 */

public class AnimActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim_layout);
        findViewById(R.id.anim_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateyAnimRun(findViewById(R.id.anim_pic));
            }
        });
    }

    public void rotateyAnimRun(View view)
    {
        ObjectAnimator
                .ofFloat(view, "rotationX", 0.0F, 360.0F)//
                .setDuration(500)//
                .start();
    }
}
