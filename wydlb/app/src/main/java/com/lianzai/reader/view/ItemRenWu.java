package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

public class ItemRenWu extends RelativeLayout{

	private Context context;


	
	
	public ItemRenWu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemRenWu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemRenWu(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	ImageView iv_logo;
	TextView tv_desc;

	TextView tv_amount1;
	TextView tv_amount2;
	TextView tv_amount3;
	ImageView zhankai_iv;
	TextView click_tv;
	TextView more_tv;

	protected void initView() {
		inflate(getContext(), R.layout.item_renwu, this);
		 iv_logo = findViewById(R.id.iv_logo);
		 tv_desc = findViewById(R.id.tv_desc);
		 tv_amount1 = findViewById(R.id.tv_amount1);
		 tv_amount2 = findViewById(R.id.tv_amount2);
		 tv_amount3 = findViewById(R.id.tv_amount3);
		 zhankai_iv = findViewById(R.id.zhankai_iv);
		zhankai_iv.setVisibility(View.GONE);
		 click_tv = findViewById(R.id.click_tv);
		 more_tv = findViewById(R.id.more_tv);
	}

	public void bindData(TaskCenterBean.DataBean.DailyTaskListBean bean) {
		RxImageTool.loadLogoImage(context,bean.getTaskImg(),iv_logo);
		tv_desc.setText(bean.getTaskName());

		if(bean.getRewardStatus() == 2){
			//未完成状态
			click_tv.setText(bean.getUnfinishedStatusDesc());
			click_tv.setBackgroundResource(R.drawable.blue_15dpconor_bg);
//			click_tv.setAlpha(1.0f);
		}else if(bean.getRewardStatus() == 0){
			//已完成状态
			click_tv.setText("领取奖励");
			click_tv.setBackgroundResource(R.drawable.jianbian_chengse_corner_renwu);
//			click_tv.setAlpha(1.0f);
		}else{
			//已领取奖励
			click_tv.setText("已领取");
			click_tv.setBackgroundResource(R.drawable.chengse_15dpconor_bg);
//			click_tv.setAlpha(0.8f);
		}


		List<TaskCenterBean.DataBean.RewardListBean> listtemp = bean.getRewardList();
		tv_amount1.setVisibility(View.GONE);
		tv_amount2.setVisibility(View.GONE);
		tv_amount3.setVisibility(View.GONE);
		if(null == listtemp){
		}else if(listtemp.isEmpty()){
		}else{
			//20000金钻，20001阅券，20002阅点，20003金币, 20004推荐票
			for(int i = 0; i < listtemp.size() ; i ++) {
				if(listtemp.get(i).getRewardAmount() > 0){
					StringBuilder sbStr = new StringBuilder();
					sbStr.append("+");
					sbStr.append(String.valueOf((int)listtemp.get(i).getRewardAmount()));
					if (listtemp.get(i).getRewardType() == 20000) {
						sbStr.append("金钻");
					} else if (listtemp.get(i).getRewardType() == 20001) {
						sbStr.append("阅券");
					} else if (listtemp.get(i).getRewardType() == 20002) {
						sbStr.append("阅点");
					} else if (listtemp.get(i).getRewardType() == 20003) {
						sbStr.append("金币");
					}else if (listtemp.get(i).getRewardType() == 20004) {
						sbStr.append("推荐票");
					}
					switch (i) {
						case 0:
							tv_amount1.setVisibility(View.VISIBLE);
							tv_amount1.setText(sbStr.toString());
							break;
						case 1:
							tv_amount2.setVisibility(View.VISIBLE);
							tv_amount2.setText(sbStr.toString());
							break;
						case 2:
							tv_amount3.setVisibility(View.VISIBLE);
							tv_amount3.setText(sbStr.toString());
							break;
					}
				}
			}
		}

		click_tv.setOnClickListener(
				v -> {
					if(bean.getRewardStatus() == 2){
						//跳转到不同页面
						if(context instanceof ActivityTaskCenter){
							((ActivityTaskCenter)context).getFragment().goClick(bean);
						}

					}else{
						//已完成状态，领取任务奖励
						if(context instanceof ActivityTaskCenter){
							((ActivityTaskCenter)context).getFragment().getAward(bean);
						}

					}
				}
		);

	}




}

