package com.lianzai.reader.ui.activity.taskCenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.ui.fragment.TaskCenterFragment;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.SystemBarUtils;

/**
 * Created by lrz on 2019/2/27.
 * 任务中心嵌套activity
 */

public class ActivityTaskCenter extends PermissionActivity{


    private int tab = 0;
    private boolean isopen = false;
    private TaskCenterFragment fragment;

    public TaskCenterFragment getFragment() {
        return fragment;
    }

    public static void startActivity(Context context , int tab){
        Bundle bundle=new Bundle();
        bundle.putInt("tab",tab);
        RxActivityTool.skipActivity(context,ActivityTaskCenter.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskcenter);
        SystemBarUtils.fullScreen(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = (TaskCenterFragment) fragmentManager.findFragmentById(R.id.fragment);

        isopen = true;
        try {
            tab=getIntent().getExtras().getInt("tab");
        }catch (Exception e){

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isopen && null != fragment){
            isopen = false;
            fragment.setTabType(tab);
        }
    }

}
