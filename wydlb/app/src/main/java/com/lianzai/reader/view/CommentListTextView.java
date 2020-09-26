package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.CommentInfoBean;
import com.lianzai.reader.interfaces.CommentClickListener;
import com.lianzai.reader.utils.URLUtils;

import java.util.List;

/**
 * Created by lrz on 2018/1/8.
 */

public class CommentListTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     * 所有评论数据
     */
    private List<CommentInfoBean> mInfos;
    private CommentClickListener mListener;

    /**
     * 点击文字会触发两个回调，用这个变量控制屏蔽掉一个
     */
    public static boolean isNickNameClick = false;


    /**
     * 最大显示行数，超过指定行数多一行显示为查看更多文本，可设置文本
     */
    private int mMaxlines = 3;
    /**
     * 当前评论数
     */
    private int mCount = 1;

    /**
     * 当超过n行后，n+1行显示为这个文本；
     */
    private String mMoreStr = "查看全部评论";
    /**
     * 谁回复谁中回复字符串，可以变成别的
     */
    private String mTalkStr = "回复";

    /**
     * 人名称颜色
     */
    private int mNameColor = Color.parseColor ("#4972C9");
    private int mCommentColor = Color.parseColor ("#2A2A2A");
    private int mTalkColor = Color.parseColor ("#424242");

    public int getTalkColor () {
        return mTalkColor;
    }

    public CommentListTextView setTalkColor (final int mTalkColor) {
        this.mTalkColor = mTalkColor;
        return this;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public int getMaxlines () {
        return mMaxlines;
    }

    public CommentListTextView setMaxlines (final int mMaxlines) {
        this.mMaxlines = mMaxlines;
        return this;
    }

    public String getMoreStr () {
        return mMoreStr;
    }

    public CommentListTextView setMoreStr (final String mMoreStr) {
        this.mMoreStr = mMoreStr;
        return this;
    }

    public String getTalkStr () {
        return mTalkStr;
    }

    public CommentListTextView setTalkStr (final String mTalkStr) {
        this.mTalkStr = mTalkStr;
        return this;
    }

    public int getNameColor () {
        return mNameColor;
    }

    public CommentListTextView setNameColor (final int mNameColor) {
        this.mNameColor = mNameColor;
        return this;
    }

    public int getCommentColor () {
        return mCommentColor;
    }

    public CommentListTextView setCommentColor (final int mCommentColor) {
        this.mCommentColor = mCommentColor;
        return this;
    }


    public CommentListTextView (final Context context) {
        super (context);
    }



    public CommentListTextView (final Context context, final AttributeSet attrs) {
        super (context, attrs);
    }

    public CommentListTextView setonCommentListener (final CommentClickListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public void setData (List<CommentInfoBean> mInfos) {
        this.mInfos = mInfos;
        /**
         * textview必须设置，否则自定义点击事件没响应
         */
        setMovementMethod (LinkMovementMethod.getInstance ());
        setHighlightColor (Color.TRANSPARENT);
        setText (getCommentString ());
        setOnClickListener (new OnClickListener () {
            @Override
            public void onClick (final View v) {
                if (isNickNameClick) {
                    isNickNameClick = false;
                    return;
                }
                if (mListener != null) {
                    mListener.onOtherClick ();
                }
            }
        });
    }

    private SpannableStringBuilder getCommentString () {
        SpannableStringBuilder mStringBuilder = new SpannableStringBuilder ();
        /**
         * 对评论数据进行处理，默认超过mMaxlines条则第mMaxlines+1条显示查看全部
         */
        for (int mI = 0; mI < mInfos.size (); mI++) {
            final CommentInfoBean mInfo = mInfos.get (mI);

            //直接使用方法拼接
            SpannableString mString = URLUtils.replaceBookForNickNick(mInfo, mTalkStr, mI, mListener, getNameColor(), getContext(), getCommentColor());

            mStringBuilder.append (mString);
            mStringBuilder.append ("\r\n");
            if (mI == (mMaxlines - 1)) {
                break;
            }
        }


        if (mCount > mMaxlines) {//等于3条就显示更多
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#485CF8"));
            mStringBuilder.append (getMoreStr()).setSpan(foregroundColorSpan,mStringBuilder.toString().length()-getMoreStr().length(),mStringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            /**
             * 如果不大于3条，则删除最后的换行
             */
            mStringBuilder.delete (mStringBuilder.length () - 2, mStringBuilder.length ());
        }
        return mStringBuilder;
    }


}
