package com.wydlb.first.service;

import android.os.Binder;

import com.wydlb.first.interfaces.IDownloadManager;
import com.wydlb.first.interfaces.OnDownloadListener;
import com.wydlb.first.utils.RxLogTool;

import java.lang.ref.WeakReference;


/**
 * Copyright (C), 2018
 * FileName: TaskBuilder
 * Author: lrz
 * Date: 2018/11/21 11:24
 * Description: ${DESCRIPTION}
 */
public class TaskBuilder extends Binder implements IDownloadManager {


    WeakReference<OnDownloadListener> reference;



    public OnDownloadListener getOnDownloadListener() {
        return reference.get();
    }

    @Override
    public void setOnDownloadListener(OnDownloadListener listener) {
        RxLogTool.e("TaskBuilder setOnDownloadListener");
        reference=new WeakReference<>(listener);
    }

}
