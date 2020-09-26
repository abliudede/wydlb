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
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

public class ItemCircleRead extends RelativeLayout{

	private Context context;

	public ItemCircleRead(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemCircleRead(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemCircleRead(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	TextView tv_tag;

	protected void initView() {
		inflate(getContext(), R.layout.item_circle_read_head, this);
		tv_tag = findViewById(R.id.tv_tag);
	}

	public void bindData(String bean) {
		tv_tag.setText(bean);
	}


	public void setChoose(String choose) {
		if(tv_tag.getText().toString().equals(choose)){
			tv_tag.setTextColor(getResources().getColor(R.color.white_text_color));
			tv_tag.setBackgroundResource(R.drawable.v2_blue_5corner_bg);
		}else {
			tv_tag.setTextColor(getResources().getColor(R.color.normal_text_color));
			tv_tag.setBackgroundResource(R.drawable.gray_corner_bg_f5f5f5);
		}
	}
}

