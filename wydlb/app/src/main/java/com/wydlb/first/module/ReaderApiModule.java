/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wydlb.first.module;


import android.os.Build;

import com.wydlb.first.api.ReaderApi;
import com.wydlb.first.api.support.HeaderInterceptor;
import com.wydlb.first.api.support.Logger;
import com.wydlb.first.api.support.LoggingInterceptor;
import com.wydlb.first.api.support.SSLSocketFactoryCompat;
import com.wydlb.first.utils.RequestUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

@Module
public class ReaderApiModule {

//  class NetInterceptor implements Interceptor{
//      @Override
//      public Response intercept(Chain chain) throws IOException {
//          Request request = chain.request().newBuilder().addHeader("Connection","close").build();
//          return chain.proceed(request);
//      }
//  }

    @Provides
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder=null;
        LoggingInterceptor logging = new LoggingInterceptor(new Logger());
        logging.setLevel(LoggingInterceptor.Level.BODY);

        //Unexpected TLS version: NONE,错误尝试解决，自定一个ConnectionSpec置入，效果待观察
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.SSL_3_0,TlsVersion.TLS_1_0,TlsVersion.TLS_1_1,TlsVersion.TLS_1_2,TlsVersion.TLS_1_3)
//                .cipherSuites(
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                .build();

        try {
            //为适配5.0以下机型。TSL1.2支持的问题。
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true) // 失败重发
                        .sslSocketFactory(new SSLSocketFactoryCompat(new RequestUtil.TrustAllCerts()),new RequestUtil.TrustAllCerts())
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(logging);
//                        .connectionSpecs(Collections.singletonList(spec))
            }else{
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

                SSLSocketFactory ssfFactory  = sc.getSocketFactory();
                builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true) // 失败重发
                        .sslSocketFactory(ssfFactory)//全部信任
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        })
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(new HeaderInterceptor())
                        .addInterceptor(logging);
//                        .connectionSpecs(Collections.singletonList(spec))
            }


        } catch (Exception e) {
        }


        return builder.build();
    }

    @Provides
    protected ReaderApi provideReaderService(OkHttpClient okHttpClient) {

        return ReaderApi.getInstance(okHttpClient);
    }


    public class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

}