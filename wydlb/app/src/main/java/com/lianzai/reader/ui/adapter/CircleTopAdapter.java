package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.PlacedTopListBean;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.EllipsizeTextView;

/**
 * 圈子置顶部分视图
 */
public class CircleTopAdapter extends RelativeLayout{

    private Context context;

    public CircleTopAdapter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public CircleTopAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CircleTopAdapter(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    //用户信息
    EllipsizeTextView tv_content;
    LinearLayout ly_content;

    protected void initView() {
        inflate(getContext(), R.layout.item_circle_top, this);
        tv_content = findViewById(R.id.tv_content);
        ly_content = findViewById(R.id.ly_content);
    }


    public void bindData(PlacedTopListBean.DataBean listBean) {
            //用户信息
        if(!TextUtils.isEmpty(listBean.getContent())) {
            tv_content.setVisibility(View.VISIBLE);
            URLUtils.replaceBookNoClick(listBean.getContent().replace("\n"," "),context,tv_content,listBean.getUrlTitle());
        }else {
            tv_content.setVisibility(View.GONE);
            tv_content.setText("");
        }
        ly_content.setOnClickListener(
                    v -> {
                        ActivityPostDetail.startActivity(context,String.valueOf(listBean.getPostId()));
                    }
            );
    }

}
