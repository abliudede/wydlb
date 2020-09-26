package com.lianzai.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.GetBookRewardDetailBean;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.bean.RelatedLinksBean2;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;

public class ItemRewardBarrage extends RelativeLayout{

	private Context context;

	public ItemRewardBarrage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemRewardBarrage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemRewardBarrage(Context context) {
		super(context);
		this.context = context;
		initView();
	}


	public LinearLayout getWhite_card() {
		return white_card;
	}

	public void setWhite_card(LinearLayout white_card) {
		this.white_card = white_card;
	}

	LinearLayout white_card;
	CircleImageView iv_logo;
	TextView tv_title;
	TextView tv_des;

	protected void initView() {
		inflate(getContext(), R.layout.item_reward_barrage, this);
		white_card = findViewById(R.id.white_card);
		iv_logo = findViewById(R.id.iv_logo);
		tv_title = findViewById(R.id.tv_title);
		tv_des = findViewById(R.id.tv_des);
	}

	public void bindData(GetBookRewardDetailBean.DataBean bean) {
		RxImageTool.loadLogoImage(getContext(),bean.getAvatar(),iv_logo);
		tv_title.setText(bean.getNickName());
//		StringBuilder sb = new StringBuilder();
//		if(bean.getType() == 1){
//			sb.append("众筹了");
//		}else if(bean.getType() == 2){
//			sb.append("买入积分");
//		}else if(bean.getType() == 3){
//			sb.append("打赏了");
//		}
//		sb.append(bean.getAmt());
//		if(bean.getBalanceType() == 1){
//			sb.append("金币");
//		}else if(bean.getBalanceType() == 3){
//			sb.append("阅点");
//		}else if(bean.getBalanceType() == 4){
//			sb.append("阅券");
//		}
		tv_des.setText(bean.getRemark());
	}


}

