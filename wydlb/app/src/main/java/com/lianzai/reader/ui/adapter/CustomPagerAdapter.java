package com.lianzai.reader.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: CustomPagerAdapter
 * Author: lrz
 * Date: 2018/10/25 15:05
 * Description: ${DESCRIPTION}
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;

    public CustomPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
        fm.beginTransaction().commitAllowingStateLoss();
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }
}
