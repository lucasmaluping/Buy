package com.lucas.buy.domain;

import java.io.Serializable;

/**
 * Created by 111 on 2017/8/7.
 */

public class MData<T> implements Serializable {
    public String type;
    public int id;
    public T dataList;
}
