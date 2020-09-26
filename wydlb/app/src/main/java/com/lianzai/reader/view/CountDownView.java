package com.lianzai.reader.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;

public class CountDownView extends RelativeLayout{

	private Context context;

	public CountDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public CountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public CountDownView(Context context) {
		super(context);
		this.context = context;
		initView();
	}


	TextView tv1;
	TextView tv2;
	TextView tv3;

	protected void initView() {
		inflate(getContext(), R.layout.count_down_view, this);
		tv1 = findViewById(R.id.tv1);
		tv2 = findViewById(R.id.tv2);
		tv3 = findViewById(R.id.tv3);
	}

	private CountDownTimer timer;

	public void bindData(long millseconds) {

		timer = new CountDownTimer(millseconds,1000L) {
			@Override
			public void onTick(long millisUntilFinished) {
				long hours = millisUntilFinished/(1000*60*60);//小时数
				long minutes = (millisUntilFinished - hours * (1000*60*60))/(1000*60);//分钟数
				long seconds = (millisUntilFinished - hours * (1000*60*60)- minutes * (1000*60))/1000;//秒数

				tv1.setText(String.valueOf(hours));
				tv2.setText(String.valueOf(minutes));
				tv3.setText(String.valueOf(seconds));
			}

			@Override
			public void onFinish() {

			}
		};
		timer.start();
	}

}

