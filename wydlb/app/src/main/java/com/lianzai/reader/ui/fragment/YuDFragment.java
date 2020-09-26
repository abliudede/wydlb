package com.lianzai.reader.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RecordButtonYuD;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;

import butterknife.Bind;

/**
 * Created by lrz on 2018/11/30.
 */
public class YuDFragment extends BaseFragment implements  RecordButtonYuD.OnRecordListener {

    @Bind(R.id.btn_audio)
    RecordButtonYuD btn_audio;

    AccountDetailBean accountDetailBean;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_yudi;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        accountDetailBean = RxTool.getAccountBean();
        btn_audio.setOnRecordListener(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止语音播放
        MessageAudioControl.getInstance(getActivity()).stopAudio();
    }


    @Override
    public void fetchData() {

    }


    @Override
    public void onFinishedRecord(int length, String audioPath) {
        RxLogTool.e("onFinishedRecord audioPath:" + audioPath + "--length：" + length);
//        if (System.currentTimeMillis() - sendTime > 1000) {//一秒内不能重复发送消息
//            sendAudioMessage(new File(audioPath), length * 1000);
//        }
    }

    boolean hasAudioPermission = false;
    @Override
    public boolean checkPermission() {

        RxLogTool.e("checkPermission......");
        checkPermission(new PermissionActivity.CheckPermListener() {

            @Override
            public void superPermission() {
                hasAudioPermission = true;
            }
            @Override
            public void noPermission() {

            }
        }, R.string.im_record_audio, Manifest.permission.RECORD_AUDIO);

        return hasAudioPermission;
    }


}