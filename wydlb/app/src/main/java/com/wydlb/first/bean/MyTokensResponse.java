package com.wydlb.first.bean;

import com.wydlb.first.model.bean.TokenBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrz on 2018/4/4.
 */

public class MyTokensResponse extends BaseBean{

    private List<TokenBean> data=new ArrayList<>();

    public List<TokenBean> getData() {
        return data;
    }

    public void setData(List<TokenBean> data) {
        this.data = data;
    }
}
