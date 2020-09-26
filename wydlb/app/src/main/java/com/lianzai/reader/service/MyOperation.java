package com.lianzai.reader.service;

import com.lianzai.reader.view.page.PageParagraphVo;

import java.util.List;
import java.util.Map;

interface MyOperation {

    public void play(List<PageParagraphVo> pageParagraphVos);//播放
    public void moveon();//继续
    public void stop();//暂停
    public void setParams(int type);//设置参数

    public void changeVoice();//停止，用于切换声音
    public void dissmissNotification();//隐藏通知

}