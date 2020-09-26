package com.lianzai.reader.utils;

import android.text.TextUtils;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;

/**
 * Created by lrz on 2017/10/21.
 */

public class RxLoginTool {

    public static  boolean isLogin(){
        AccountTokenBean accountTokenBean=RxSharedPreferencesUtil.getInstance().getObject(Constant.ACCOUNT_TOKEN, AccountTokenBean.class);
        if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
            return  true;//已登录
        }else {
            String str = RxSharedPreferencesUtil.getInstance().getString(Constant.ACCOUNT_TOKEN3);
            if(!TextUtils.isEmpty(str)){
                accountTokenBean = GsonUtil.getBean(str,AccountTokenBean.class);
            }
            if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
                return  true;//已登录
            }
        }
        return false;
    }
    public static  AccountTokenBean getLoginAccountToken(){
        AccountTokenBean accountTokenBean=RxSharedPreferencesUtil.getInstance().getObject(Constant.ACCOUNT_TOKEN, AccountTokenBean.class);
        if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
            return  accountTokenBean;//已登录
        }else {
            String str = RxSharedPreferencesUtil.getInstance().getString(Constant.ACCOUNT_TOKEN3);
            if(!TextUtils.isEmpty(str)){
                accountTokenBean = GsonUtil.getBean(str,AccountTokenBean.class);
            }
            if (null!=accountTokenBean&&null!=accountTokenBean.getData()){
                return  accountTokenBean;//已登录
            }
        }
        return null;
    }

    public static  void saveToken(AccountTokenBean token){
        RxSharedPreferencesUtil.getInstance().putObject(Constant.ACCOUNT_TOKEN,token);
        RxSharedPreferencesUtil.getInstance().putString(Constant.ACCOUNT_TOKEN3,GsonUtil.toJsonString(token));
    }

    public static  void removeToken(){
        RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_TOKEN);//清除本地token
        RxSharedPreferencesUtil.getInstance().remove(Constant.ACCOUNT_TOKEN3);//清除本地token
    }

}
