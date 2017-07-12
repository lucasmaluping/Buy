package com.lucas.buy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.lucas.buy.R;
import com.lucas.buy.actiivities.MyApplication;
import com.lucas.buy.contents.UserContents;
import com.lucas.buy.utils.VolleyImageUtils;

/**
 * Created by 111 on 2017/7/7.
 */

public class MineFragment extends Fragment {
    private View view;
    private ImageView img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_layout,container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        img = (ImageView) view.findViewById(R.id.mine_img);
        VolleyImageUtils.loadImage(UserContents.imageUrl, img);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }
}
