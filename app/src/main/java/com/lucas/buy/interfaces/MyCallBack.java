package com.lucas.buy.interfaces;

import com.android.volley.VolleyError;
import com.lucas.buy.domain.Customer;

import java.util.List;

/**
 * Created by 111 on 2017/7/8.
 */

public interface MyCallBack {
    void success(List<Customer> customers);
    void error(VolleyError error);
}
