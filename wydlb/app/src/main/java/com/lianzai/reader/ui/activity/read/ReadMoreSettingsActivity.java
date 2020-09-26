package com.lianzai.reader.ui.activity.read;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.dialog.ReadSettingDialog;
import com.lianzai.reader.view.dialog.RxDialogSingleHand;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Copyright (C), 2018
 * FileName: TempReadActivity
 * Author: lrz
 * Date: 2018/11/5 11:17
 * Description: ${DESCRIPTION}
 */
public class ReadMoreSettingsActivity extends BaseActivity {

    @Bind(R.id.tv_common_title)
    TextView tv_common_title;


    @Bind(R.id.cb_volume_key_page)
    CheckBox cb_volume_key_page;

    @Bind(R.id.cb_single_hand)
    CheckBox cb_single_hand;


    @Bind(R.id.rb_screen_keep_on)
    RadioButton rb_screen_keep_on;

    @Bind(R.id.rb_screen_follow_system)
    RadioButton rb_screen_follow_system;

    @Bind(R.id.rg_brightness)
            RadioGroup rg_brightness;


    RxDialogSingleHand rxDialogSingleHand;//单手提示弹框

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,ReadMoreSettingsActivity.class));

    }
    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_read_more_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_common_title.setText("更多设置");
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        //实现状态栏图标和文字颜色为暗色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        cb_volume_key_page.setChecked(ReadSettingManager.getInstance().isVolumeTurnPage());

        cb_single_hand.setChecked(ReadSettingManager.getInstance().isSingleHand());

        if (ReadSettingManager.getInstance().isScreenKeepOn()){
            rb_screen_keep_on.setChecked(true);
        }else{

            rb_screen_follow_system.setChecked(true);
        }


        rg_brightness.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rb_screen_keep_on){
                    ReadSettingManager.getInstance().setScreenKeepOn(true);
                }else if(i==R.id.rb_screen_follow_system){
                    ReadSettingManager.getInstance().setScreenKeepOn(false);
                }
            }
        });
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    /**
     * 音量键翻页
     * @param isChecked
     */
    @OnCheckedChanged(R.id.cb_volume_key_page)void volumeKeyPageChanged(boolean isChecked){
        ReadSettingManager.getInstance().setVolumeTurnPage(isChecked);
    }

    /**
     * 单手操作
     * @param isChecked
     */
    @OnCheckedChanged(R.id.cb_single_hand)void singleHandChanged(boolean isChecked){
        ReadSettingManager.getInstance().setSingleHand(isChecked);
    }


    /**
     * 单手模式弹框提示
     */
    @OnClick(R.id.tv_single_hand)void singleHandClick(){
        if (null==rxDialogSingleHand){
            rxDialogSingleHand=new RxDialogSingleHand(this);
        }
        rxDialogSingleHand.show();
    }


}
