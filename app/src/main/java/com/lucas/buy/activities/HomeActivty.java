package com.lucas.buy.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lucas.buy.R;
import com.lucas.buy.fragments.CartFragment;
import com.lucas.buy.fragments.CategoryFragment;
import com.lucas.buy.fragments.IndexFragment;
import com.lucas.buy.fragments.MineFragment;
import com.lucas.buy.utils.VolleyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 111 on 2017/7/7.
 */

public class HomeActivty extends AppCompatActivity implements View.OnClickListener {
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private RadioButton indexBtn;
    private RadioButton categoryBtn;
    private RadioButton cartBtn;
    private RadioButton mineBtn;
    private RadioGroup rg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        VolleyUtil.getInstance().getUserInfo("lucas");

        viewPager = (ViewPager) findViewById(R.id.home_vp);
        indexBtn = (RadioButton) findViewById(R.id.home_rb_index);
        categoryBtn = (RadioButton) findViewById(R.id.home_rb_category);
        cartBtn = (RadioButton) findViewById(R.id.home_rb_cart);
        mineBtn = (RadioButton) findViewById(R.id.home_rb_mine);
        rg = (RadioGroup) findViewById(R.id.home_rg);
        indexBtn.setOnClickListener(this);
        categoryBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        mineBtn.setOnClickListener(this);

        fragments = new ArrayList<>();
        fragments.add(new IndexFragment());
        fragments.add(new CategoryFragment());
        fragments.add(new CartFragment());
        fragments.add(new MineFragment());

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        rg.check(R.id.home_rb_index);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rg.check(R.id.home_rb_index);
                        break;
                    case 1:
                        rg.check(R.id.home_rb_category);
                        break;
                    case 2:
                        rg.check(R.id.home_rb_cart);
                        break;
                    case 3:
                        rg.check(R.id.home_rb_mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_rb_index:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.home_rb_category:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.home_rb_cart:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.home_rb_mine:
                viewPager.setCurrentItem(3 , true);
                break;
        }
    }
}
