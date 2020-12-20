package com.wydlb.first.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;

/**
 * Created by lrz on 2017/11/2.
 */

public class EmptyView extends RelativeLayout {
    Context mContext;
    LayoutInflater layoutInflater;
    View contentView;
    TextView tv_empty;
    ImageView iv_empty;

    public EmptyView(Context context) {
        super(context);
        this.mContext=context;
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();
    }

    private void init(){
        layoutInflater=LayoutInflater.from(mContext);
        contentView=layoutInflater.inflate(R.layout.view_records_empty,null);
        tv_empty=(TextView)contentView.findViewById(R.id.tv_empty);
        iv_empty=(ImageView) contentView.findViewById(R.id.iv_empty);

        addView(contentView);

    }

    public void setData(int resId,int stringId){
        iv_empty.setImageResource(resId);
        tv_empty.setText(mContext.getResources().getString(stringId));
    }
}
