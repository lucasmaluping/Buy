package com.lucas.buy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.lucas.buy.R;

/**
 * Created by 111 on 2017/7/7.
 */

public class CartFragment extends Fragment {
    private View view;
    private ProgressBar progressBar;
    private Button button;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progressBar.setProgress((int)msg.obj);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        getActivity().requestWindowFeature(Window.FEATURE_PROGRESS);
        view = inflater.inflate(R.layout.cart_layout,container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        button = (Button) view.findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(50);
                                Message message = new Message();
                                message.what = 0;
                                message.obj = i;
                                handler.sendMessage(message);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }
}
