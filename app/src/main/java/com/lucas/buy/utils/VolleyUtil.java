package com.lucas.buy.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lucas.buy.MyCallBack;
import com.lucas.buy.actiivities.MyApplication;
import com.lucas.buy.domain.Customer;

import java.util.List;

/**
 * Created by 111 on 2017/7/8.
 */

public class VolleyUtil {
    private static final String TAG = "VolleyUtil";
    private static final String VOLLEY_TAG = "VolleyTag";

    private static VolleyUtil instance;

    public static VolleyUtil getInstance() {
        if(instance == null) {
            instance = new VolleyUtil();
        }
        return instance;
    }

    public void getVolleyDataGet(String url, final MyCallBack callBack) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<Customer> customers = JSON.parseArray(response, Customer.class);
                callBack.success(customers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"....error:" + error);
            }
        });

        stringRequest.setTag(VOLLEY_TAG);
        MyApplication.getRequestQueue().add(stringRequest);
    }


}
