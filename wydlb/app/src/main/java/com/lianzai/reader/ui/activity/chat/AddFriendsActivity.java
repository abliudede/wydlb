package com.lianzai.reader.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.AddFriendsAdapter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AddFriendsActivity extends BaseActivity {

    @Bind(R.id.rv_data)
    RecyclerView rv_data;


    AddFriendsAdapter addFriendsAdapter;

    View headView;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AddFriendsActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_friends;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        headView=getLayoutInflater().inflate(R.layout.view_add_friend_header,null);
        List list=new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add(i);
        }
        addFriendsAdapter=new AddFriendsAdapter(list);
        addFriendsAdapter.addHeaderView(headView);
        rv_data.setAdapter(addFriendsAdapter);
        rv_data.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_data.setLayoutManager(new RxLinearLayoutManager(this));
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @Override
    public void gc() {

    }
}
