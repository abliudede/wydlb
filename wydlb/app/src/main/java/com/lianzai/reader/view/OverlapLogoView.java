package com.lianzai.reader.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * Created by lrz on 2018/1/15.
 * 打赏头像重叠控件
 */

public class OverlapLogoView extends RelativeLayout {

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


    public OverlapLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initData(context,attrs);
    }

    public int getMaxCount() {
        return maxCount;
    }

    private void initData(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OverlapLogoView);
        logoWidth = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoWidth, RxImageTool.dp2px(28));
        logoHeight = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoHeight,RxImageTool.dp2px(28) );
        logoMarginLeft = a.getDimensionPixelSize(R.styleable.OverlapLogoView_logoMarginLeft,logoWidth/2);
        textSize = a.getDimensionPixelSize(R.styleable.OverlapLogoView_textSize, 12);
        maxCount = a.getInteger(R.styleable.OverlapLogoView_maxCount, 15);
        showText = a.getBoolean(R.styleable.OverlapLogoView_showText, true);
        text=a.getString(R.styleable.OverlapLogoView_text);
        textColor=a.getColor(R.styleable.OverlapLogoView_txtColor,0xfffffff);

        a.recycle();
    }

    public void initViewData(String[] logoUrls,String number,String content){
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
            RelativeLayout.LayoutParams layoutParams=new LayoutParams(logoWidth,logoHeight);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);

            SelectableRoundedImageView logoView=new SelectableRoundedImageView(mContext);
            logoView.setOval(true);
            logoView.setScaleType(ImageView.ScaleType.FIT_XY);
            logoView.setBorderColor(mContext.getResources().getColor(R.color.white));
            logoView.setBorderWidthDP(1);

            RxImageTool.loadLogoImage(mContext,logoUrls[i],logoView);

            int marginLeft=i*logoMarginLeft;
            textMarginLeft=(i+1)*logoWidth-marginLeft;
            layoutParams.setMargins(marginLeft,0,0,0);
            addView(logoView,layoutParams);
        }

        if (showText){
            textView=new TextView(mContext);
            if (!TextUtils.isEmpty(text))
                textView.setText(text);
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            RelativeLayout.LayoutParams textLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            textLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            textView.setLayoutParams(textLayoutParams);

            textLayoutParams.setMargins(textMarginLeft+30,0,0,0);
            addView(textView,textLayoutParams);//添加文字
        }

        postInvalidate();
    }


    /**
     * 只显示头像
     * @param logoUrls
     */
    public void initViewData(String[] logoUrls){
        removeAllViews();

        for(int i=0;i<(logoUrls.length>maxCount?maxCount:logoUrls.length);i++){

            RelativeLayout.LayoutParams layoutParams=new LayoutParams(RxImageTool.dp2px(25),RxImageTool.dp2px(25));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);


            SelectableRoundedImageView logoView=new SelectableRoundedImageView(mContext);
            logoView.setId(i+1);
            logoView.setOval(true);
            logoView.setScaleType(ImageView.ScaleType.FIT_XY);
            logoView.setBorderColor(mContext.getResources().getColor(R.color.transparent));
            logoView.setBorderWidthDP(1);

            RxImageTool.loadLogoImage(mContext,logoUrls[i],logoView);
            if (i==0){
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            }else{
                layoutParams.addRule(RelativeLayout.LEFT_OF,i);//依次排在左边
                layoutParams.setMargins(0,0,-20,0);
            }

            addView(logoView,layoutParams);
        }


        postInvalidate();
    }


    @SuppressLint("ResourceType")
    public void dynamicUpdate(List<String> logoUrls, String content){
        removeAllViews();
        int textMarginLeft=0;
        if (TextUtils.isEmpty(content)){
            showText=false;
        }else{
            showText=true;
        }

        if (!TextUtils.isEmpty(content))
            text=content;

        if (showText){
            textView=new TextView(mContext);
            if (!TextUtils.isEmpty(text))
                textView.setText(text);
            textView.setTextSize(textSize);

            textView.setId(0x1001);

            textView.setTextColor(textColor);
            RelativeLayout.LayoutParams textLayoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            textLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            textView.setLayoutParams(textLayoutParams);

            Drawable drawable=RxImageTool.getDrawable(17,R.mipmap.icon_dynamic_arrow_right);
            textView.setCompoundDrawables(null,null,drawable,null);
            textView.setCompoundDrawablePadding(5);

            textLayoutParams.setMargins(textMarginLeft+10,0,0,0);
            addView(textView,textLayoutParams);//添加文字
        }

        for(int i=0;i<(logoUrls.size()>maxCount?maxCount:logoUrls.size());i++){

            RelativeLayout.LayoutParams layoutParams=new LayoutParams(RxImageTool.dp2px(21),RxImageTool.dp2px(21));
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);


            SelectableRoundedImageView logoView=new SelectableRoundedImageView(mContext);
            logoView.setId(i+1);
            logoView.setOval(true);
            logoView.setScaleType(ImageView.ScaleType.FIT_XY);
            logoView.setBorderColor(mContext.getResources().getColor(R.color.transparent));
            logoView.setBorderWidthDP(1);

            RxImageTool.loadLogoImage(mContext,logoUrls.get(i),logoView);
            if (i==0){
                if (showText){
                    layoutParams.addRule(RelativeLayout.LEFT_OF,0x1001);
                }else{
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                }

            }else{
                layoutParams.addRule(RelativeLayout.LEFT_OF,i);//依次排在左边
                layoutParams.setMargins(0,0,-20,0);
            }

            addView(logoView,layoutParams);
        }



        postInvalidate();
    }

}
