package com.wydlb.first.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.utils.RxImageTool;

/**
 * Created by lrz on 2018/11/7
 * 拼手气头像控件
 */

public class OverlapLogoViewForFightLuck extends RelativeLayout {

    int logoWidth=0;
    int logoHeight=0;
    int logoMarginLeft=0;
    int textSize=0;
    boolean showText = true;
    int maxCount=0;

    private String text;

    TextView textView;

    Context mContext;

    int textColor=0xffffff;


    public OverlapLogoViewForFightLuck(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initData(context,attrs);
    }

    public int getMaxCount() {
        return maxCount;
    }

    private void initData(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverlapLogoView);
        logoWidth = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoWidth, RxImageTool.dp2px(26));
        logoHeight = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoHeight,RxImageTool.dp2px(26) );
        logoMarginLeft = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoMarginLeft,RxImageTool.dp2px(30));
        textSize = a.getDimensionPixelSize(R.styleable.OverlapLogoView_textSize, 14);
        maxCount = a.getInteger(R.styleable.OverlapLogoView_maxCount, 4);
        showText = a.getBoolean(R.styleable.OverlapLogoView_showText, true);
        text=a.getString(R.styleable.OverlapLogoView_text);
        textColor=a.getColor(R.styleable.OverlapLogoView_txtColor,0xff333333);
        a.recycle();
    }

    public void initViewData(String[] logoUrls,String number,String content,int length){
        removeAllViews();
        int textMarginLeft=0;
        if(TextUtils.isEmpty(number)){
            showText = false;
            return;
        }
        else if(Integer.parseInt(number) <= 0){
            showText = false;
            return;
        }else {
            showText = true;
        }

        if (!TextUtils.isEmpty(content))
            text=content;

        for(int i=0;i<(logoUrls.length>maxCount?maxCount:logoUrls.length);i++){
            LayoutParams layoutParams=new LayoutParams(logoWidth,logoHeight);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);

            SelectableRoundedImageView logoView=new SelectableRoundedImageView(mContext);
            logoView.setOval(false);
            logoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            logoView.setBorderColor(mContext.getResources().getColor(R.color.white));
            logoView.setBorderWidthDP(1);
            logoView.setCornerRadiiDP(4,4,4,4);
            RxImageTool.loadLogoImage(mContext,logoUrls[i],logoView);

            int marginLeft=i*logoMarginLeft;
            textMarginLeft=(i+1)*logoMarginLeft;
            layoutParams.setMargins(marginLeft,0,0,0);
            addView(logoView,layoutParams);
        }

        if (showText){
            textView=new TextView(mContext);
            textView.setTextColor(textColor);
            if (!TextUtils.isEmpty(text)) {
                SpannableString ss = new SpannableString(text);
                ss.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 1, 1+length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(ss);
            }
            textView.setTextSize(textSize);
            LayoutParams textLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            textLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            textView.setLayoutParams(textLayoutParams);

            textLayoutParams.setMargins(textMarginLeft,0,0,0);
            addView(textView,textLayoutParams);//添加文字
        }

        postInvalidate();
    }

}
