package com.wydlb.first.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.bean.MessageBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.TimeFormatUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 公告详情
 */

public class ActivityNoticeDetail extends BaseActivity {

    @Bind(R.id.tv_big_title)
    TextView tv_title;

    @Bind(R.id.tv_date)
    TextView tv_date;

    @Bind(R.id.tv_content)
    TextView tv_content;

    MessageBean.DataBeanX.DataBean data;

    public static void startActivity(Context context, MessageBean.DataBeanX.DataBean bean){
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",bean);
        RxActivityTool.skipActivity(context,ActivityNoticeDetail.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        if (null!=getIntent().getExtras()){
            data=(MessageBean.DataBeanX.DataBean)getIntent().getExtras().getSerializable("data");
        }
        if (null!=data){
            RxLogTool.e("content:"+data.getContent().getContent());
            tv_content.setText(Html.fromHtml(data.getContent().getContent()).toString());
            tv_title.setText(data.getContent().getTitle());
            tv_date.setText(TimeFormatUtil.getInterval(data.getCreate_time()));

        }
    }


    @Override
    public void gc() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }
}
