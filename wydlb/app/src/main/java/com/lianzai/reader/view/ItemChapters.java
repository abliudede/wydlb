package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.bean.RelatedLinksBean2;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.TimeFormatUtil;

import java.util.List;

public class ItemChapters extends RelativeLayout{

	private Context context;

	public ItemChapters(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemChapters(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemChapters(Context context) {
		super(context);
		this.context = context;
		initView();
	}


	TextView tv_title;
	TextView tv_time;

	protected void initView() {
		inflate(getContext(), R.layout.item_chapters, this);
		tv_title = findViewById(R.id.tv_title);
		tv_time = findViewById(R.id.tv_time);
	}

	public void bindData(RelatedLinksBean.DataBean.SourcesBean.ChaptersBean bean,String source,String bookId) {
		tv_title.setText(bean.getChapter_title());
//		tv_time.setText(TimeFormatUtil.getInterval(bean.getUpdated_time()));
		tv_time.setText(bean.getUpdated_time());
		setOnClickListener(
				v -> {
					//指定章节跳转
					//是否开启了自动转码
					if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
						UrlReadActivity.startActivityOutsideRead(getContext(),bookId,source,false,"",bean.getChapter_title(),0,false);
					}else {
						ActivityWebView.startActivityForReadTitle(getContext(),bean.getChapter_url(),bookId,bean.getChapter_title(),source,false,0);
					}

				}
		);
	}

	public void bindData(RelatedLinksBean2.DataBean.ChaptersBean bean, String source, String bookId) {
		tv_title.setText(bean.getChapter_title());
//		tv_time.setText(TimeFormatUtil.getInterval(bean.getUpdated_time()));
		tv_time.setText(bean.getUpdated_time());
		setOnClickListener(
				v -> {
					//指定章节跳转
					//是否开启了自动转码
					if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
						UrlReadActivity.startActivityOutsideRead(getContext(),bookId,source,false,"",bean.getChapter_title(),0,false);
					}else {
						ActivityWebView.startActivityForReadTitle(getContext(),bean.getChapter_url(),bookId,bean.getChapter_title(),source,false,0);
					}

				}
		);
	}

}

