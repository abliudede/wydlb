package com.lianzai.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.ObserverBean;
import com.lianzai.reader.utils.RxImageTool;

public class ItemLuckyBox extends RelativeLayout{

	private Context context;


	public ItemLuckyBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemLuckyBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemLuckyBox(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	CircleImageView iv_logo;
	TextView tv_desc;


	protected void initView() {
		inflate(getContext(), R.layout.item_lucky_box, this);
		 iv_logo = findViewById(R.id.iv_logo);
		 tv_desc = findViewById(R.id.tv_desc);
	}

	public void bindData(ObserverBean.DataBeanX bean) {
		RxImageTool.loadLogoImage(context,bean.getData().getHead(),iv_logo);
		tv_desc.setText("抽中了价值" + bean.getData().getRewardValue() + "阅券 的" + bean.getData().getRewardName() );
	}


}

