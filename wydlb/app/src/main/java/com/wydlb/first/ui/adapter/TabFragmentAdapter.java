package com.wydlb.first.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments=new ArrayList<>();

    private FragmentManager fm;


    String [] titles;

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments=fragments;
    }

    public TabFragmentAdapter(FragmentManager mfm) {
        super(mfm);
        fm=mfm;
    }

    @Override

    public Fragment getItem(int position) {
//        if (position==0){
//            return ((BookStoreFragment)getFragments().get(position));
//        }else if(position==1){
//            return ((ChasingBookFragment)getFragments().get(position));
//        }else if(position==2){
//            return ((FindFragment)getFragments().get(position));
//        }else if(position==3){
//            return ((MineFragment)getFragments().get(position));
//        }
        return getFragments().get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitles()[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁,某些tab必须保持不销毁，否则会出错
//        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
//        if (object instanceof BookStoreFragment) {
//            ((BookStoreFragment) object).updateViews();
//        }  else if (object instanceof FindFragment) {
//            ((FindFragment) object).updateViews();
//        } else if (object instanceof MineFragment) {
//            ((MineFragment) object).updateViews();
//        }else if (object instanceof BarPostReplyListFragment) {
//            ((BarPostReplyListFragment) object).updateViews();
//        }else if (object instanceof MyProfileFragment) {
//            ((MyProfileFragment) object).updateViews();
//        }
        return super.getItemPosition(object);
    }
}

