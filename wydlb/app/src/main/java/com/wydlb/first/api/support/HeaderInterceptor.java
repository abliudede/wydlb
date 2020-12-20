/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wydlb.first.api.support;


import android.text.TextUtils;

import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.base.Constant;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxEncryptTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.RxSharedPreferencesUtil;
import com.wydlb.first.utils.RxTimeTool;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Retrofit2 Header拦截器。用于保存和设置Cookies
 */
public final class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Headers headers = original.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            RxLogTool.e("HeaderInterceptor-----params:"+name + ": " + headers.value(i));
        }

        Request.Builder builder = original.newBuilder();

        builder.addHeader("Language", RxSharedPreferencesUtil.getInstance().getBoolean(Constant.CURRENT_LANGUAGE_KEY,true)?Constant.Language.CHINESE:Constant.Language.ENGLISH);
        builder.addHeader("terminal",String.valueOf(Constant.Channel.ANDROID));//系统平台 /android ios 客户端标识(header)1:WEB;2:Mobile;3:Android;4:IOS
        builder.addHeader("Content-Type","text/html;charset=utf-8");
        builder.addHeader("AppVersionName",""+ RxAppTool.getAppVersionName(BuglyApplicationLike.getContext()));
//        builder.addHeader("Connection", "close");
        try{
            //使用自己生成的设备号
            //生成唯一设备号
            String deviceNo = RxSharedPreferencesUtil.getInstance().getString("deviceNo");
            if (TextUtils.isEmpty(deviceNo)) {
                int num = (int) ((Math.random() * 9 + 1) * 100000);
                deviceNo = RxTimeTool.getCurTimeString() + "Android" + num;
                deviceNo = RxEncryptTool.encryptMD5ToString(deviceNo, "AndroidSalt");
                RxSharedPreferencesUtil.getInstance().putString("deviceNo", deviceNo);
            }
            builder .addHeader("deviceNo", deviceNo);
        }catch (Exception e){

        }


        try{
            if (RxLoginTool.isLogin()&&null!=RxLoginTool.getLoginAccountToken().getData()&&null!=RxLoginTool.getLoginAccountToken().getData().getToken()){
                builder.addHeader("lz_sso_token", RxLoginTool.getLoginAccountToken().getData().getToken());//token
//                builder.addHeader("lz_sso_token", "E48CE4E280CC48E8B2D74E5D57280293");//token
            }
//          builder .addHeader("deviceNo", RxDeviceTool.getIMEI(BuglyApplicationLike.getsInstance().getApplication().getApplicationContext()));
        }catch (Exception e){
        }
//      builder.addHeader("User-Agent","Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36");
        builder.addHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.67 Safari/537.36");

//      builder.addHeader("deviceToken",RxSharedPreferencesUtil.getInstance().getString("deviceToken",""));
        HttpUrl httpUrl = original.url();

        String encodePath=httpUrl.encodedPath();

        RxLogTool.e("httpUrl:"+httpUrl.toString());

        if (httpUrl.toString().contains("lianzai.com")||httpUrl.toString().contains("bendixing.net")){//通过我们的接口
            builder.addHeader("Accept", "application/json");
        }else{
            builder.addHeader("Accept", "text/html");
//            builder.addHeader("Accept", "application/json");
        }

        if (Constant.appMode==Constant.AppMode.DEV){//dev环境没配域名
            builder.addHeader("Accept", "application/json");
        }

//        RxLogTool.e("encodePath:"+encodePath);
        if (encodePath.equals("/user/sendVerificationCode")){//发短信
            builder.addHeader("lz_token_auth", RxSharedPreferencesUtil.getInstance().getString("lz_token_auth",""));
            builder.addHeader("lz_token_auth_method", "sms");
            builder.addHeader("lz_token_auth_sign", RxSharedPreferencesUtil.getInstance().getString("lz_token_auth_sign",""));
        }else if(encodePath.equals("/user/sendBindThirdCode")){
            builder.addHeader("lz_token_auth", RxSharedPreferencesUtil.getInstance().getString("lz_token_auth",""));
            builder.addHeader("lz_token_auth_method", "bindThirdSms");
            builder.addHeader("lz_token_auth_sign", RxSharedPreferencesUtil.getInstance().getString("lz_token_auth_sign",""));
        } else if(encodePath.equals("/api/exchange")||encodePath.equals("/api/gateway")||encodePath.equals("/api/withdraw") || encodePath.equals("/api/placeRewardOrder")|| encodePath.equals("/api/lzTransferwhiteRabbit")){//支付网关接口
            return chain.proceed(builder.url(Constant.GATEWAY_API_BASE_URL+encodePath).build());
        }
        return chain.proceed(builder.url(httpUrl).build());

    }
}
