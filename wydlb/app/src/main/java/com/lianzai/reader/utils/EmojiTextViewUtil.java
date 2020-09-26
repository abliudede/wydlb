package com.lianzai.reader.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiTextViewUtil {
    private static final float DEF_SCALE = 0.5f;
    private static final float SMALL_SCALE = 0.5F;

    public static void identifyFaceExpression(Context context,
                                              View textView, String value, int align) {
        identifyFaceExpression(context, textView, value, align, DEF_SCALE);
    }

    public static void identifyFaceExpressionAndATags(Context context,
                                                      View textView, String value, int align) {
        SpannableString mSpannableString = makeSpannableStringTags(context, value, DEF_SCALE, align);
        viewSetText(textView, mSpannableString);
    }

    /**
     * @param context         上下文
     * @param textView        着色控件
     * @param value           正文
     * @param align
     * @param primayColorText @who 部分
     */
    public static void identifyFaceExpressionAndATags(Context context,
                                                      View textView, String value, int align, String primayColorText) {
        if (TextUtils.isEmpty(primayColorText)) {
            identifyFaceExpressionAndATags(context, textView, value, align);
            return;
        }
        SpannableString mSpannableString = makeSpannableStringTags(context, value, DEF_SCALE, align, true, primayColorText);
        viewSetText(textView, mSpannableString);
    }

    /**
     * 具体类型的view设置内容
     *
     * @param textView
     * @param mSpannableString
     */
    private static void viewSetText(View textView, SpannableString mSpannableString) {
        if (textView instanceof EditText) {
            EditText et = (EditText) textView;
            et.setText(mSpannableString);
            if (mSpannableString != null && mSpannableString.length() > 0) {
                et.setSelection(mSpannableString.length());
                RxLogTool.e("MoonUtil", "mSpannableString.length() = " + mSpannableString.length());
            }
        } else if (textView instanceof TextView) {
            TextView tv = (TextView) textView;
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setText(mSpannableString);
        }
    }

    public static void identifyFaceExpression(Context context,
                                              View textView, String value, int align, float scale) {
        SpannableString mSpannableString = replaceEmoticons(context, value, scale, align);
        viewSetText(textView, mSpannableString);
    }

    /**
     * 只需显示a标签对应的文本
     */
    public static void identifyFaceExpressionAndTags(Context context,
                                                     View textView, String value, int align, float scale) {
        SpannableString mSpannableString = makeSpannableStringTags(context, value, scale, align, true);
        viewSetText(textView, mSpannableString);
    }

    private static SpannableString replaceEmoticons(Context context, String value, float scale, int align) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }

        SpannableString mSpannableString = new SpannableString(value);
        Matcher matcher = EmojiManager.getPattern().matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String emot = value.substring(start, end);
            Drawable d = getEmotDrawable(context, emot, scale);
            if (d != null) {
                ImageSpan span = new ImageSpan(d, align);
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return mSpannableString;
    }

    private static Pattern mATagPattern = Pattern.compile("<a.*?>.*?</a>");

    public static SpannableString makeSpannableStringTags(Context context, String value, float scale, int align) {
        return makeSpannableStringTags(context, value, DEF_SCALE, align, true);
    }

    public static SpannableString makeSpannableStringTags(Context context, String value, float scale, int align, boolean bTagClickable, String... colorString) {
        ArrayList<ATagSpan> tagSpans = new ArrayList<ATagSpan>();
        if (TextUtils.isEmpty(value)) {
            value = "";
        }

        if (null != colorString && colorString.length > 0) {
            value = colorString[0] + value;
            RxLogTool.e("ATagSpan colorString:" + colorString[0]);
        }
        RxLogTool.e("ATagSpan value:" + value);
        //a标签需要替换原始文本,放在moonutil类中
        Matcher aTagMatcher = mATagPattern.matcher(value);

        int start = 0;
        int end = 0;
        while (aTagMatcher.find()) {
            start = aTagMatcher.start();
            end = aTagMatcher.end();
            String atagString = value.substring(start, end);
            ATagSpan tagSpan = getTagSpan(context, atagString);
            value = value.substring(0, start) + tagSpan.getTag() + value.substring(end);
            tagSpan.setRange(start, start + tagSpan.getTag().length());
            tagSpans.add(tagSpan);
            aTagMatcher = mATagPattern.matcher(value);
        }


        SpannableString mSpannableString = new SpannableString(value);
        Matcher matcher = EmojiManager.getPattern().matcher(value);
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            String emot = value.substring(start, end);
            Drawable d = getEmotDrawable(context, emot, scale);
            if (d != null) {
                ImageSpan span = new ImageSpan(d, align);
                mSpannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (bTagClickable) {
            for (ATagSpan tagSpan : tagSpans) {
                mSpannableString.setSpan(tagSpan, tagSpan.start, tagSpan.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return mSpannableString;
    }

    public static void replaceEmoticons(Context context, Editable editable, int start, int count) {
        if (count <= 0 || editable.length() < start + count)
            return;

        CharSequence s = editable.subSequence(start, start + count);
        Matcher matcher = EmojiManager.getPattern().matcher(s);
        while (matcher.find()) {
            int from = start + matcher.start();
            int to = start + matcher.end();
            String emot = editable.subSequence(from, to).toString();
            Drawable d = getEmotDrawable(context, emot, SMALL_SCALE);
            if (d != null) {
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                editable.setSpan(span, from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static Drawable getEmotDrawable(Context context, String text, float scale) {
        Drawable drawable = EmojiManager.getDrawable(context, text);

        // scale
        if (drawable != null) {
            int width = (int) (drawable.getIntrinsicWidth() * scale);
            int height = (int) (drawable.getIntrinsicHeight() * scale);
            drawable.setBounds(3, 0, width, height);
        }

        return drawable;
    }

    private static ATagSpan getTagSpan(Context context, String text) {
        String value = null;
        String tag = null;
        if (text.toLowerCase().contains("href")) {
            int start = text.indexOf("'");
            int end = text.indexOf("'", start + 1);
            if (end > start)
                value = text.substring(start + 1, end);
        }
        int start = text.indexOf(">");
        int end = text.indexOf("<", start);
        if (end > start)
            tag = text.substring(start + 1, end);
        RxLogTool.e("ATagSpan tag:" + tag);
        RxLogTool.e("ATagSpan value:" + value);
        return new ATagSpan(tag, value, context);

    }

    private static class ATagSpan extends ClickableSpan {
        private int start;
        private int end;
        private String mValue;
        private String tag;
        public Context mContext;

        ATagSpan(String tag, String value, Context context) {
            this.tag = tag;
            this.mValue = value;
            mContext = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(Color.parseColor("#007FFF")); //设置颜色
        }

        public String getTag() {
            return tag;
        }

        public void setRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void onClick(View widget) {
            RxLogTool.e("ATagSpan:onClick");
            if (TextUtils.isEmpty(mValue))
                return;
        }
    }

    private static Map<String, SpannableString> imageCash = imageCash = new HashMap<>();

    public static void needCash() {
        RxLogTool.e( "needCash");
        imageCash.clear();
    }

    public static void clearCash() {
        RxLogTool.e("clearCash");
        imageCash.clear();
    }


    public enum ClickItem {
        CLICK_ON_TARGET, CLICK_ON_IMAGE
    }

    private interface LoadImageFinish {
        void onFinish(SpannableString spanString);
    }

    public static class BlueShowName {
        private String nick;
        private String userId;

        public BlueShowName(String userId, String nick) {
            this.nick = nick;
            this.userId = userId;
        }

        public BlueShowName(int userId, String nick) {
            this.nick = nick;
            this.userId = String.valueOf(userId);
        }

        @Override
        public String toString() {
            if (TextUtils.isEmpty(this.userId) || TextUtils.isEmpty(this.nick)) {
                return "";
            }
            return "<a href='" + this.userId + "'>" + "@" + this.nick + "</a>";
        }
    }
}
