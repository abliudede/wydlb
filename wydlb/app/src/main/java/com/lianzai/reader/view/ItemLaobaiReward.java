package com.lianzai.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.ReceiveWhiteBookCouponBean;
import com.lianzai.reader.utils.RxImageTool;

public class ItemLaobaiReward extends RelativeLayout{

	private Context context;




	public ItemLaobaiReward(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemLaobaiReward(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemLaobaiReward(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	ImageView iv_logo;
	TextView tv_amount;

	TextView tv_title;
	TextView tv_des;

	protected void initView() {
		inflate(getContext(), R.layout.item_laobai_reward, this);
		 iv_logo = findViewById(R.id.iv_logo);
		tv_amount = findViewById(R.id.tv_amount);
		tv_title = findViewById(R.id.tv_title);
		tv_des = findViewById(R.id.tv_des);
	}

	public void bindData(ReceiveWhiteBookCouponBean.DataBean.BalancesBean bean) {
		RxImageTool.loadAwardImage(context,bean.getIcon(),iv_logo);
		tv_title.setText(bean.getTitle());
		tv_des.setText(bean.getSubtitle());
		tv_amount.setText(String.valueOf(bean.getAmt()));
	}




}

