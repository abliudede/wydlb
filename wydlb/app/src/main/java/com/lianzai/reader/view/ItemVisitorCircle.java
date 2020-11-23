package com.lianzai.reader.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.VisitorCircleBean;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.utils.RxImageTool;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

public class ItemVisitorCircle extends RelativeLayout{

	private Context context;

	public ItemVisitorCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemVisitorCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemVisitorCircle(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	HeadImageView img_head;
	TextView tv_nickname;
	TextView tv_message;
	TextView tv_date_time;


	protected void initView() {
		inflate(getContext(), R.layout.visitor_circle_item, this);
		img_head = findViewById(R.id.img_head);
		tv_nickname = findViewById(R.id.tv_nickname);
		tv_message = findViewById(R.id.tv_message);
		tv_date_time = findViewById(R.id.tv_date_time);
	}

	public void bindData(VisitorCircleBean.DataBean bean) {
		RxImageTool.loadLogoImage(context,bean.getPlatformCover(),img_head);
		tv_nickname.setText(bean.getPlatformName());
		tv_message.setText(bean.getTitle());

		setOnClickListener(
				v -> {
					if(!TextUtils.isEmpty(bean.getUrl())){
						ActivityWebView.startActivity(getContext(),bean.getUrl());
					}else {
						ActivityCircleDetail.startActivity(getContext(),String.valueOf(bean.getId()));
					}
				}
		);

	}




}

