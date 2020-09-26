package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.fragment.TaskCenterFragment;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2019/2/27.
 * 展示文字的activity
 */

public class ActivityTextShow extends BaseActivity{

    @Bind(R.id.tv_text)
    TextView tv_text;


    private String show;


    public static void startActivity(Context context , String show){
        Bundle bundle=new Bundle();
        bundle.putString("show",show);
        RxActivityTool.skipActivity(context,ActivityTextShow.class,bundle);
    }


    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_text_show;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        try{
            show = getIntent().getExtras().getString("show");
            tv_text.setText(show);
        }catch (Exception e){
        }

        tv_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RxClipboardTool.copyText(ActivityTextShow.this,tv_text.getText().toString());
                RxToast.custom("已复制").show();
                return false;
            }
        });

    }

    @OnClick(R.id.img_back)
    void circleBackClick() {
        finish();
    }

}
