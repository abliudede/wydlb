package com.lianzai.reader.service;

import android.os.Binder;

import com.lianzai.reader.interfaces.IDownloadManager;
import com.lianzai.reader.interfaces.OnDownloadListener;
import com.lianzai.reader.utils.RxLogTool;

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
