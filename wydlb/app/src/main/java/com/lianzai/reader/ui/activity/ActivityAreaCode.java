package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.bean.AreaCodeBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.adapter.AreaCodeAdapter;
import com.lianzai.reader.ui.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.lianzai.reader.ui.adapter.ViewHolder;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 国家和地区代码
 */

public class ActivityAreaCode extends BaseActivity  {

    @Bind(R.id.rv_area_code)
    RecyclerView rv_area_code;

    @Bind(R.id.indexBar)
    IndexBar indexBar;

    @Bind(R.id.tvSideBarHint)
    TextView tvSideBarHint;

    SuspensionDecoration mDecoration;

    AreaCodeAdapter areaCodeAdapter;

    HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;

    ArrayList<AreaCodeBean> areaCodeBeans=new ArrayList<>();

    public static  final int AREA_CODE_REQUEST=300;

    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    public static void startActivityForResult(Activity context){
        RxActivityTool.skipActivityForResult(context,ActivityAreaCode.class,AREA_CODE_REQUEST);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        try{
            areaCodeBeans=RxTool.loadAreaCodes(this);
        }catch (Exception e){
            e.printStackTrace();
            RxLogTool.e("数据加载失败");
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rv_area_code.setLayoutManager(linearLayoutManager);

        areaCodeAdapter=new AreaCodeAdapter(this,areaCodeBeans);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(areaCodeAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.tv_area_name, "header:"+(String) o);
            }
        };

        areaCodeAdapter.setOnRepeatClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                int pos=Integer.parseInt(v.getTag().toString());
                Bundle bundle=new Bundle();
                bundle.putString("name",areaCodeBeans.get(pos).getChinaName());
                bundle.putString("code",areaCodeBeans.get(pos).getInternaCode());
                Intent intent=new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mHeaderAdapter.setHeaderView(R.layout.item_area_code,"测试标题");

        rv_area_code.setAdapter(areaCodeAdapter);


        mDecoration = new SuspensionDecoration(this, areaCodeBeans).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()-1);
        mDecoration.setColorTitleBg(getResources().getColor(R.color.line_bg));
        mDecoration.setColorTitleFont(getResources().getColor(R.color.second_text_color));
        rv_area_code.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        rv_area_code.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));


        indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(linearLayoutManager);//设置RecyclerView的LayoutManager

        indexBar.setmSourceDatas(areaCodeBeans)//设置数据
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()-1)//设置HeaderView数量
                .invalidate();

        mHeaderAdapter.notifyDataSetChanged();
        mDecoration.setmDatas(areaCodeBeans);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.img_back)void closeClick(){
        setResult(RESULT_OK);
        finish();
    }

}
