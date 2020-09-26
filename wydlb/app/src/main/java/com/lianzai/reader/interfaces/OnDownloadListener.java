package com.lianzai.reader.interfaces;

/**
 * Copyright (C), 2018
 * FileName: OnDownloadListener
 * Author: lrz
 * Date: 2018/11/21 13:31
 * Description: ${DESCRIPTION}
 */
public interface OnDownloadListener {
    /**
     *
     * @param pos : Task在item中的位置
     * @param status : Task的状态
     * @param msg: 传送的Msg
     */
    void onDownloadChange(int pos, int status, String msg,int currentTotalChapters,String bookId);
}
