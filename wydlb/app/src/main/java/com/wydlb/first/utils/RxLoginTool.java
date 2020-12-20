package com.wydlb.first.utils;

import android.text.TextUtils;

import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountTokenBean;

/**
 * Created by lrz on 2017/10/21.
 */

public class RxLoginTool {

    public static  boolean isLogin(){
        AccountTokenBean accountTokenBean = null;
            String str = RxSharedPreferencesUtil.getInstance().getString(Constant.ACCOUNT_TOKEN);
            if(!TextUtils.isEmpty(str)){
                accountTokenBean = GsonUtil.getBean(str,AccountTokenBean.class);
            }
            if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
                return  true;//已登录
            }else {
                return false;
            }
    }
    public static  AccountTokenBean getLoginAccountToken(){
        AccountTokenBean accountTokenBean = null;
        String str = RxSharedPreferencesUtil.getInstance().getString(Constant.ACCOUNT_TOKEN);
        if(!TextUtils.isEmpty(str)){
            accountTokenBean = GsonUtil.getBean(str,AccountTokenBean.class);
        }
        if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
            return  accountTokenBean;//已登录
        }else {
            return null;
        }
    }

    public static  void saveToken(AccountTokenBean token){
        RxSharedPreferencesUtil.getInstance().putString(Constant.ACCOUNT_TOKEN,GsonUtil.toJsonString(token));
    }

    public static  void removeToken(){
        RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_TOKEN);//清除本地token
    }

}
