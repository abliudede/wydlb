/**
 * Copyright 2017 玄链科技
 *Author QichengQing
 *Date 2017-7-19
 */
package com.wydlb.first.api.support;


import com.wydlb.first.utils.RxLogTool;

/**
 * @author yuyh.
 * @date 2016/12/13.
 */
public class Logger implements LoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        RxLogTool.e("http : " + message);
    }
}
