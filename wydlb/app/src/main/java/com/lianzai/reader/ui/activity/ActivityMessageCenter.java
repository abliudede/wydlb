package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.TabEntity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.ui.fragment.MyMessageFragment;
import com.lianzai.reader.view.tab.CommonTabLayout;
import com.lianzai.reader.view.tab.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 通知页面
 */

public class ActivityMessageCenter extends BaseActivity  {

    @Bind(R.id.tabs)
    CommonTabLayout commonTabLayout;

    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    MyMessageFragment myMessageFragment;
    MyMessageFragment systemMessageFragment;

    TabFragmentAdapter tabFragmentAdapter;

    List<Fragment> fragments=new ArrayList<>();

    public int getLayoutId() {
        return R.layout.activity_message_center;
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        fragments.clear();
        myMessageFragment=new MyMessageFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("type", Constant.MessageType.NOTICE_TYPE);
        myMessageFragment.setArguments(bundle);

        systemMessageFragment=new MyMessageFragment();
        Bundle bundle1=new Bundle();
        bundle1.putInt("type", Constant.MessageType.NOTICE_TYPE);
        systemMessageFragment.setArguments(bundle1);

//        fragments.add(myMessageFragment);
        fragments.add(systemMessageFragment);
        String [] mTitles={"系统公告"};
        ArrayList<CustomTabEntity>mTabEntities=new ArrayList<>();

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],0,0));
        }
        commonTabLayout.setTabData(mTabEntities);
        tabFragmentAdapter=new TabFragmentAdapter(getSupportFragmentManager());
        tabFragmentAdapter.setTitles(mTitles);
        tabFragmentAdapter.setFragments(fragments);
        viewPager.setAdapter(tabFragmentAdapter);

        commonTabLayout.setupViewPager(viewPager);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

}
