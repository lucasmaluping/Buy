package com.lucas.buy.utils;

import android.content.ContentUris;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lucas.buy.interfaces.LoginCallBack;
import com.lucas.buy.interfaces.MyCallBack;
import com.lucas.buy.actiivities.MyApplication;
import com.lucas.buy.contents.UserContents;
import com.lucas.buy.domain.Customer;
import com.lucas.buy.interfaces.RegistCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void login(final String name, final String password, final LoginCallBack callBack) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserContents.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"...response:" + response);
                if (response.equals(UserContents.ok)) {
                    callBack.success(UserContents.ok);
                } else {
                    callBack.errr(UserContents.error);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"...error:" + error);
                callBack.errr(UserContents.error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name",name);
                map.put("password", password);
                return map;
            }
        };

        stringRequest.setTag(VOLLEY_TAG);
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /**
     * 注册新用户的方法
     * @param name 用户名
     * @param password
     * @param age
     * @param gender
     * @param callBack 回调接口，作用发送数据到MainActivity
     */
    public void regist(final String name, final String password, final String age, final String gender, final RegistCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserContents.registUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"...response:" + response);
                callBack.registSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"...error:" + error);
                callBack.registError(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("password", password);
                map.put("age", age);
                map.put("gender", gender);
                return map;
            }
        };
        stringRequest.setTag(VOLLEY_TAG);
        MyApplication.getRequestQueue().add(stringRequest);
    }
}
