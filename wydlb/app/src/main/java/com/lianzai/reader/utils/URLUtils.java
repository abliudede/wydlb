package com.lianzai.reader.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.bean.CommentInfoBean;
import com.lianzai.reader.bean.UrlKeyValueBean;
import com.lianzai.reader.interfaces.CommentClickListener;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.view.CommentListTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiajing on 2018/5/29.
 */

public class URLUtils {

    public static String getUrlInContent(String content) {
        String contenttemp = content;
        List<String> termList = new ArrayList<String>();

        String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
//        String patternString ="((http[s]{0,1}|ftp)://[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?)";
        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(contenttemp);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        if(!termList.isEmpty()){
            return termList.get(0);
        }else {
            return null;
        }
    }

    public static String getKouLingInContent(String content) {
        String contenttemp = content;
        List<String> termList = new ArrayList<String>();

        String patternString = "≡(.+?)≡";
        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(contenttemp);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        if(!termList.isEmpty()){
            return termList.get(0);
        }else {
            return null;
        }
    }

    public static void replaceWeborBook(String value, SpannableString mSpannableString,Context context ,int in) {
        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(mSpannableString)) {
            return;
        }
        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }
            String temp = matcher1.group();
            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText (true);
                }

                @Override
                public void onClick(View widget) {
                    ActivitySearchFirst.skiptoSearch(temp.substring(1,temp.length()-1),context);
                }
            }, start + 1, end - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        Pattern pattern2 = Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*");
        Matcher matcher2 = pattern2.matcher(value);
        while (matcher2.find()) {
            int start = matcher2.start();
            int end = matcher2.end();
            if (end - start <= 2) {
                continue;
            }
            String temp = matcher2.group();
            mSpannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
//                    ds.setStrikeThruText(true);
                    if(in == 1) {
                        ds.setColor(0xFF3865FE);
                    }else if(in == 2){
                        ds.setColor(0xFFAFECFF);
                    }
                }
                @Override
                public void onClick(View widget) {
                    ActivityWebView.startActivityNeedUrlAuth(context,temp);

                }
            }, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
    }


    public static void replaceBookForReplyNick(String userName ,String value, Context context, TextView tv,String json) {
        if(null == tv){
            return;
        }
        if (TextUtils.isEmpty(value)) {
            tv.setText("");
            return;
        }

        //字符串重整
        StringBuilder valueResultTemp = new StringBuilder();

        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        int index = 0;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }

            valueResultTemp.append(value.substring(index,end - 1));
            valueResultTemp.append(" ");
            index = end - 1;

        }
        valueResultTemp.append(value.substring(index,value.length()));

        String valueResult = valueResultTemp.toString();

        //解析网页的标题信息
        UrlKeyValueBean bean = GsonUtil.getBean(json,UrlKeyValueBean.class);
//        if(null == bean || null == bean.getList() || bean.getList().isEmpty()){
//            //无网页情况展示
//            //改变样式
//            valueResult = "回复 " + userName + "：" + valueResult;
//            SpannableString mSpannableString = new SpannableString(valueResult);
//            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName), 3,3 + userName.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            Pattern pattern3 = Pattern.compile("《[^》]+》");
//            Matcher matcher3 = pattern3.matcher(valueResult);
//            while (matcher3.find()) {
//                int start = matcher3.start();
//                int end = matcher3.end();
//                if (end - start <= 2) {
//                    continue;
//                }
//
//                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String searchStr = matcher3.group();
//                mSpannableString.setSpan(new ClickableSpan() {
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                        ds.setColor(0xFF3865FE);
//                    }
//
//                    @Override
//                    public void onClick(View widget) {
//                        ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                    }
//                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            }
//
//            tv.setText(mSpannableString);
//            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//        }else {

        List<UrlKeyValueBean.DataBean> listBean = new ArrayList<>();
        if(null != bean && null != bean.getList() && !bean.getList().isEmpty()) {
            listBean = bean.getList();
        }
            //寻找网页链接并按顺序加入结果列表中
            List<UrlKeyValueBean.DataBean> listResult = new ArrayList<UrlKeyValueBean.DataBean>();
            String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
            Pattern pattern2 = Pattern.compile(patternString);
            Matcher matcher2 = pattern2.matcher(valueResult);
            while (matcher2.find()) {
                String tempStr = matcher2.group();
                boolean has = false;
                if(!listBean.isEmpty()) {
                    for (UrlKeyValueBean.DataBean item : listBean) {
                        if (item.getUrl().equals(tempStr)) {
                            listResult.add(item);
                            has = true;
                            break;
                        }
                    }
                }
                //假如是没识别的，则生成一个加入列表
                if(!has){
                    UrlKeyValueBean.DataBean item = new UrlKeyValueBean.DataBean();
                    item.setTitle("网页链接");
                    item.setUrl(tempStr);
                    listResult.add(item);
                }
            }
            //重整字符串
            for(UrlKeyValueBean.DataBean item : listResult){
                String temp = item.getUrl();
                String tempStr = valueResult.replace(temp," " + item.getTitle() + "  ");
                valueResult = tempStr;
            }

            //改变样式
            valueResult = "回复 " + userName + "：" + valueResult;
            SpannableString mSpannableString = new SpannableString(valueResult);
            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName), 3,3 + userName.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            Pattern pattern3 = Pattern.compile("《[^》]+》");
            Matcher matcher3 = pattern3.matcher(valueResult);
            while (matcher3.find()) {
                int start = matcher3.start();
                int end = matcher3.end();
                if (end - start <= 2) {
                    continue;
                }

                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String searchStr = matcher3.group();
                mSpannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                        ds.setColor(0xFF3865FE);
                    }

                    @Override
                    public void onClick(View widget) {
                        ActivitySearchFirst.skiptoSearch(searchStr.substring(1,searchStr.length()-1),context);
                    }
                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }


            if(!listResult.isEmpty()) {
                List<Integer> hasIndex = new ArrayList<>();
                for (UrlKeyValueBean.DataBean item : listResult) {
                    String temp = " " + item.getTitle() + " ";
                    int indexTemp = valueResult.indexOf(temp);
                    while (hasIndex.contains(indexTemp)) {
                        indexTemp = valueResult.indexOf(temp, indexTemp + temp.length());
                    }
                    //加入池子，下次不再允许匹配到此位置
                    hasIndex.add(indexTemp);


                    Drawable d1 = RxImageTool.getDrawable(12, 12, R.mipmap.icon_url);
                    MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span1, indexTemp, indexTemp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyImageSpan span2 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span2, indexTemp + temp.length() - 1, indexTemp + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mSpannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                            ds.setColor(0xFF3865FE);
                        }

                        @Override
                        public void onClick(View widget) {
                            ActivityWebView.startActivityNeedUrlAuth(context, item.getUrl());
                        }
                    }, indexTemp + 1, indexTemp + temp.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }


            tv.setText(mSpannableString);
            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//        }

    }

    public static void replaceBookForNick(String userName ,String value, Context context, TextView tv ,String json) {

        if(null == tv){
            return;
        }
        if (TextUtils.isEmpty(value)) {
            tv.setText("");
            return;
        }

        //字符串重整
        StringBuilder valueResultTemp = new StringBuilder();

        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        int index = 0;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }

            valueResultTemp.append(value.substring(index,end - 1));
            valueResultTemp.append(" ");
            index = end - 1;

        }
        valueResultTemp.append(value.substring(index,value.length()));

        String valueResult = valueResultTemp.toString();

        //解析网页的标题信息
        UrlKeyValueBean bean = GsonUtil.getBean(json,UrlKeyValueBean.class);
//        if(null == bean || null == bean.getList() || bean.getList().isEmpty()){
//            //无网页情况展示
//            //改变样式
//            valueResult = userName + "：" + valueResult;
//            SpannableString mSpannableString = new SpannableString(valueResult);
//            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName), 0,userName.length()+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            Pattern pattern3 = Pattern.compile("《[^》]+》");
//            Matcher matcher3 = pattern3.matcher(valueResult);
//            while (matcher3.find()) {
//                int start = matcher3.start();
//                int end = matcher3.end();
//                if (end - start <= 2) {
//                    continue;
//                }
//
//                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String searchStr = matcher3.group();
//                mSpannableString.setSpan(new ClickableSpan() {
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                        ds.setColor(0xFF3865FE);
//                    }
//
//                    @Override
//                    public void onClick(View widget) {
//                        ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                    }
//                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            }
//
//            tv.setText(mSpannableString);
//            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//        }else {
        List<UrlKeyValueBean.DataBean> listBean = new ArrayList<>();
        if(null != bean && null != bean.getList() && !bean.getList().isEmpty()) {
            listBean = bean.getList();
        }
            //寻找网页链接并按顺序加入结果列表中
            List<UrlKeyValueBean.DataBean> listResult = new ArrayList<UrlKeyValueBean.DataBean>();
            String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
            Pattern pattern2 = Pattern.compile(patternString);
            Matcher matcher2 = pattern2.matcher(valueResult);
            while (matcher2.find()) {
                String tempStr = matcher2.group();
                boolean has = false;
                if(!listBean.isEmpty()) {
                    for (UrlKeyValueBean.DataBean item : listBean) {
                        if (item.getUrl().equals(tempStr)) {
                            listResult.add(item);
                            has = true;
                            break;
                        }
                    }
                }
                //假如是没识别的，则生成一个加入列表
                if(!has){
                    UrlKeyValueBean.DataBean item = new UrlKeyValueBean.DataBean();
                    item.setTitle("网页链接");
                    item.setUrl(tempStr);
                    listResult.add(item);
                }
            }
            //重整字符串
            for(UrlKeyValueBean.DataBean item : listResult){
                String temp = item.getUrl();
                String tempStr = valueResult.replace(temp," " + item.getTitle() + "  ");
                valueResult = tempStr;
            }

            //改变样式
            valueResult = userName + "：" + valueResult;
            SpannableString mSpannableString = new SpannableString(valueResult);
            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName), 0,userName.length()+1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            Pattern pattern3 = Pattern.compile("《[^》]+》");
            Matcher matcher3 = pattern3.matcher(valueResult);
            while (matcher3.find()) {
                int start = matcher3.start();
                int end = matcher3.end();
                if (end - start <= 2) {
                    continue;
                }

                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String searchStr = matcher3.group();
                mSpannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                        ds.setColor(0xFF3865FE);
                    }

                    @Override
                    public void onClick(View widget) {
                        ActivitySearchFirst.skiptoSearch(searchStr.substring(1,searchStr.length()-1),context);
                    }
                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }


            if(!listResult.isEmpty()) {
                List<Integer> hasIndex = new ArrayList<>();
                for (UrlKeyValueBean.DataBean item : listResult) {
                    String temp = " " + item.getTitle() + " ";
                    int indexTemp = valueResult.indexOf(temp);
                    while (hasIndex.contains(indexTemp)) {
                        indexTemp = valueResult.indexOf(temp, indexTemp + temp.length());
                    }
                    //加入池子，下次不再允许匹配到此位置
                    hasIndex.add(indexTemp);


                    Drawable d1 = RxImageTool.getDrawable(12, 12, R.mipmap.icon_url);
                    MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span1, indexTemp, indexTemp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyImageSpan span2 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span2, indexTemp + temp.length() - 1, indexTemp + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mSpannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                            ds.setColor(0xFF3865FE);
                        }

                        @Override
                        public void onClick(View widget) {
                            ActivityWebView.startActivityNeedUrlAuth(context, item.getUrl());
                        }
                    }, indexTemp + 1, indexTemp + temp.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

            }

            tv.setText(mSpannableString);
            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());

    }

    public static SpannableString replaceBookForNickNick(CommentInfoBean mInfo,String mTalkStr,int mI,CommentClickListener mListener,int mNameColor,Context context,int mCommentColor) {
        if (null == mInfo) {
            return new SpannableString("");
        }
        String value = mInfo.getComment();
        String json = mInfo.getJson();
        String userName = mInfo.getNickName();
        String toNickName = mInfo.getToNickName();

        //字符串重整
        StringBuilder valueResultTemp = new StringBuilder();


        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        int index = 0;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }

            valueResultTemp.append(value.substring(index,end - 1));
            valueResultTemp.append(" ");
            index = end - 1;

        }
        valueResultTemp.append(value.substring(index,value.length()));

        String valueResult = valueResultTemp.toString();

        //解析网页的标题信息
        UrlKeyValueBean bean = GsonUtil.getBean(json,UrlKeyValueBean.class);
//        if(null == bean || null == bean.getList() || bean.getList().isEmpty()){
//            //无网页情况展示
//            //改变样式
//            if (TextUtils.isEmpty(toNickName)) {
//                valueResult = userName + "：" +valueResult;
//            } else {
//                valueResult = userName + mTalkStr + toNickName + "：" + valueResult;
//            }
//            SpannableString mSpannableString = new SpannableString(valueResult);
//            int start = 0;
//            int end = userName.length ();
//            mSpannableString.setSpan (new ClickableSpan() {
//
//                @Override
//                public void updateDrawState (TextPaint ds) {
//                    /**
//                     * 是否有下划线
//                     */
//                    ds.setUnderlineText (false);
//                    /**
//                     * 橘红色字体
//                     */
//                    ds.setColor (mNameColor);
//                }
//
//                @Override
//                public void onClick (final View widget) {
//                    CommentListTextView.isNickNameClick = true;
//                    if (mListener != null) {
//                        mListener.onNickNameClick (mI, mInfo);
//                    }
//                }
//            }, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            if (!TextUtils.isEmpty(toNickName)) {
//                start = userName.length () + mTalkStr.length ();
//                end = userName.length () + mTalkStr.length () + toNickName.length ();
//                mSpannableString.setSpan(new TextAppearanceSpan(context,R.style.StyleReplyName),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                mSpannableString.setSpan (new ClickableSpan () {
//                    @Override
//                    public void updateDrawState (TextPaint ds) {
//                        /**
//                         * 是否有下划线
//                         */
//                        ds.setUnderlineText (false);
//                        /**
//                         * 橘红色字体
//                         */
//                        ds.setColor (mNameColor);
//                    }
//
//                    @Override
//                    public void onClick (final View widget) {
//                        CommentListTextView.isNickNameClick = true;
//                        if (mListener != null) {
//                            mListener.onToNickNameClick (mI, mInfo);
//                        }
//                    }
//                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//
//
//            Pattern pattern3 = Pattern.compile("《[^》]+》");
//            Matcher matcher3 = pattern3.matcher(valueResult);
//            while (matcher3.find()) {
//                start = matcher3.start();
//                end = matcher3.end();
//                if (end - start <= 2) {
//                    continue;
//                }
//
//                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String searchStr = matcher3.group();
//                mSpannableString.setSpan(new ClickableSpan() {
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                        ds.setColor(0xFF3865FE);
//                    }
//
//                    @Override
//                    public void onClick(View widget) {
//                        CommentListTextView.isNickNameClick = true;
//                        if (mListener != null)
//                            mListener.onSearchClick(searchStr.substring(1,searchStr.length()-1));
//                    }
//                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//
//          return  mSpannableString;
//        }else {
        List<UrlKeyValueBean.DataBean> listBean = new ArrayList<>();
        if(null != bean && null != bean.getList() && !bean.getList().isEmpty()) {
            listBean = bean.getList();
        }
            //寻找网页链接并按顺序加入结果列表中
            List<UrlKeyValueBean.DataBean> listResult = new ArrayList<UrlKeyValueBean.DataBean>();
            String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
            Pattern pattern2 = Pattern.compile(patternString);
            Matcher matcher2 = pattern2.matcher(valueResult);
            while (matcher2.find()) {
                String tempStr = matcher2.group();
                boolean has = false;
                if(!listBean.isEmpty()) {
                    for (UrlKeyValueBean.DataBean item : listBean) {
                        if (item.getUrl().equals(tempStr)) {
                            listResult.add(item);
                            has = true;
                            break;
                        }
                    }
                }
                //假如是没识别的，则生成一个加入列表
                if(!has){
                    UrlKeyValueBean.DataBean item = new UrlKeyValueBean.DataBean();
                    item.setTitle("网页链接");
                    item.setUrl(tempStr);
                    listResult.add(item);
                }
            }
            //重整字符串
            for(UrlKeyValueBean.DataBean item : listResult){
                String temp = item.getUrl();
                String tempStr = valueResult.replace(temp," " + item.getTitle() + "  ");
                valueResult = tempStr;
            }

            //改变样式
            if (TextUtils.isEmpty(toNickName)) {
                valueResult = userName + "：" +valueResult;
            } else {
                valueResult = userName + mTalkStr + toNickName + "：" + valueResult;
            }
            SpannableString mSpannableString = new SpannableString(valueResult);
            int start = 0;
            int end = userName.length ();
            mSpannableString.setSpan (new ClickableSpan() {

                @Override
                public void updateDrawState (TextPaint ds) {
                    /**
                     * 是否有下划线
                     */
                    ds.setUnderlineText (false);
                    /**
                     * 橘红色字体
                     */
                    ds.setColor (mNameColor);
                }

                @Override
                public void onClick (final View widget) {
                    CommentListTextView.isNickNameClick = true;
                    if (mListener != null) {
                        mListener.onNickNameClick (mI, mInfo);
                    }
                }
            }, 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.StyleReplyName),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (!TextUtils.isEmpty(toNickName)) {
                start = userName.length () + mTalkStr.length ();
                end = userName.length () + mTalkStr.length () + toNickName.length ();
                mSpannableString.setSpan(new TextAppearanceSpan(context,R.style.StyleReplyName),start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mSpannableString.setSpan (new ClickableSpan () {
                    @Override
                    public void updateDrawState (TextPaint ds) {
                        /**
                         * 是否有下划线
                         */
                        ds.setUnderlineText (false);
                        /**
                         * 橘红色字体
                         */
                        ds.setColor (mNameColor);
                    }

                    @Override
                    public void onClick (final View widget) {
                        CommentListTextView.isNickNameClick = true;
                        if (mListener != null) {
                            mListener.onToNickNameClick (mI, mInfo);
                        }
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }




            Pattern pattern3 = Pattern.compile("《[^》]+》");
            Matcher matcher3 = pattern3.matcher(valueResult);
            while (matcher3.find()) {
                start = matcher3.start();
                end = matcher3.end();
                if (end - start <= 2) {
                    continue;
                }

                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String searchStr = matcher3.group();
                mSpannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                        ds.setColor(0xFF3865FE);
                    }

                    @Override
                    public void onClick(View widget) {
                        CommentListTextView.isNickNameClick = true;
                        if (mListener != null)
                        mListener.onSearchClick(searchStr.substring(1,searchStr.length()-1));
                    }
                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


            if(!listResult.isEmpty()) {
                List<Integer> hasIndex = new ArrayList<>();
                for (UrlKeyValueBean.DataBean item : listResult) {
                    String temp = " " + item.getTitle() + " ";
                    int indexTemp = valueResult.indexOf(temp);
                    while (hasIndex.contains(indexTemp)) {
                        indexTemp = valueResult.indexOf(temp, indexTemp + temp.length());
                    }
                    //加入池子，下次不再允许匹配到此位置
                    hasIndex.add(indexTemp);


                    Drawable d1 = RxImageTool.getDrawable(12, 12, R.mipmap.icon_url);
                    MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span1, indexTemp, indexTemp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyImageSpan span2 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span2, indexTemp + temp.length() - 1, indexTemp + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mSpannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                            ds.setColor(0xFF3865FE);
                        }

                        @Override
                        public void onClick(View widget) {
                            CommentListTextView.isNickNameClick = true;
                            if (mListener != null)
                                mListener.onWebClick(item.getUrl());
                        }
                    }, indexTemp + 1, indexTemp + temp.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }

           return mSpannableString;

    }


    //支持书名号和网页标题的显示
    public static void replaceBook(String value, Context context, TextView tv ,String json) {
        if(null == tv){
            return;
        }
        if (TextUtils.isEmpty(value)) {
            tv.setText("");
            return;
        }

        //字符串重整
        StringBuilder valueResultTemp = new StringBuilder();

        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        int index = 0;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }

            valueResultTemp.append(value.substring(index,end - 1));
            valueResultTemp.append(" ");
            index = end - 1;

        }
        valueResultTemp.append(value.substring(index,value.length()));

        String valueResult = valueResultTemp.toString();

        //解析网页的标题信息
        UrlKeyValueBean bean = GsonUtil.getBean(json,UrlKeyValueBean.class);
//        if(null == bean || null == bean.getList() || bean.getList().isEmpty()){
//            //无网页情况展示
//            //改变样式
//            SpannableString mSpannableString = new SpannableString(valueResult);
//            Pattern pattern3 = Pattern.compile("《[^》]+》");
//            Matcher matcher3 = pattern3.matcher(valueResult);
//            while (matcher3.find()) {
//                int start = matcher3.start();
//                int end = matcher3.end();
//                if (end - start <= 2) {
//                    continue;
//                }
//
//                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                String searchStr = matcher3.group();
//                mSpannableString.setSpan(new ClickableSpan() {
//                    @Override
//                    public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                        ds.setColor(0xFF3865FE);
//                    }
//
//                    @Override
//                    public void onClick(View widget) {
//                        ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                    }
//                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            }
//
//            tv.setText(mSpannableString);
//            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//        }else {
        List<UrlKeyValueBean.DataBean> listBean = new ArrayList<>();
        if(null != bean && null != bean.getList() && !bean.getList().isEmpty()) {
            listBean = bean.getList();
        }
            //寻找网页链接并按顺序加入结果列表中
            List<UrlKeyValueBean.DataBean> listResult = new ArrayList<UrlKeyValueBean.DataBean>();
            String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
            Pattern pattern2 = Pattern.compile(patternString);
            Matcher matcher2 = pattern2.matcher(valueResult);
            while (matcher2.find()) {
                String tempStr = matcher2.group();
                boolean has = false;
                if(!listBean.isEmpty()) {
                    for (UrlKeyValueBean.DataBean item : listBean) {
                        if (item.getUrl().equals(tempStr)) {
                            listResult.add(item);
                            has = true;
                            break;
                        }
                    }
                }
                //假如是没识别的，则生成一个加入列表
                if(!has){
                    UrlKeyValueBean.DataBean item = new UrlKeyValueBean.DataBean();
                    item.setTitle("网页链接");
                    item.setUrl(tempStr);
                    listResult.add(item);
                }
            }
            //重整字符串
            for(UrlKeyValueBean.DataBean item : listResult){
                String temp = item.getUrl();
                String tempStr = valueResult.replace(temp," " + item.getTitle() + "  ");
                valueResult = tempStr;
            }


            //改变样式
            SpannableString mSpannableString = new SpannableString(valueResult);
            Pattern pattern3 = Pattern.compile("《[^》]+》");
            Matcher matcher3 = pattern3.matcher(valueResult);
            while (matcher3.find()) {
                int start = matcher3.start();
                int end = matcher3.end();
                if (end - start <= 2) {
                    continue;
                }

                Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String searchStr = matcher3.group();
                mSpannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                        ds.setColor(0xFF3865FE);
                    }

                    @Override
                    public void onClick(View widget) {
                        ActivitySearchFirst.skiptoSearch(searchStr.substring(1,searchStr.length()-1),context);
                    }
                }, start + 1, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }


            if(!listResult.isEmpty()) {
                List<Integer> hasIndex = new ArrayList<>();
                for (UrlKeyValueBean.DataBean item : listResult) {
                    String temp = " " + item.getTitle() + " ";
                    int indexTemp = valueResult.indexOf(temp);
                    while (hasIndex.contains(indexTemp)) {
                        indexTemp = valueResult.indexOf(temp, indexTemp + temp.length());
                    }
                    //加入池子，下次不再允许匹配到此位置
                    hasIndex.add(indexTemp);

                    Drawable d1 = RxImageTool.getDrawable(12, 12, R.mipmap.icon_url);
                    MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span1, indexTemp, indexTemp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MyImageSpan span2 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                    mSpannableString.setSpan(span2, indexTemp + temp.length() - 1, indexTemp + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mSpannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
                            ds.setColor(0xFF3865FE);
                        }

                        @Override
                        public void onClick(View widget) {
                            ActivityWebView.startActivityNeedUrlAuth(context, item.getUrl());
                        }
                    }, indexTemp + 1, indexTemp + temp.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            tv.setText(mSpannableString);
            tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
    }


    //支持书名号和网页标题的显示,但不可点击
    public static void replaceBookNoClick(String value, Context context, TextView tv ,String json) {
        if(null == tv){
            return;
        }
        if (TextUtils.isEmpty(value)) {
            tv.setText("");
            return;
        }

        //字符串重整
        StringBuilder valueResultTemp = new StringBuilder();

        Pattern pattern1 = Pattern.compile("《[^》]+》");
        Matcher matcher1 = pattern1.matcher(value);
        int index = 0;
        while (matcher1.find()) {
            int start = matcher1.start();
            int end = matcher1.end();
            if (end - start <= 2) {
                continue;
            }
            valueResultTemp.append(value.substring(index,end - 1));
            valueResultTemp.append(" ");
            index = end - 1;

        }
        valueResultTemp.append(value.substring(index,value.length()));
        String valueResult = valueResultTemp.toString();

        //解析网页的标题信息
        UrlKeyValueBean bean = GsonUtil.getBean(json,UrlKeyValueBean.class);
        List<UrlKeyValueBean.DataBean> listBean = new ArrayList<>();
        if(null != bean && null != bean.getList() && !bean.getList().isEmpty()) {
            listBean = bean.getList();
        }
        //寻找网页链接并按顺序加入结果列表中
        List<UrlKeyValueBean.DataBean> listResult = new ArrayList<UrlKeyValueBean.DataBean>();
        String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
        Pattern pattern2 = Pattern.compile(patternString);
        Matcher matcher2 = pattern2.matcher(valueResult);
        while (matcher2.find()) {
            String tempStr = matcher2.group();
            boolean has = false;
            if(!listBean.isEmpty()) {
                for (UrlKeyValueBean.DataBean item : listBean) {
                    if (item.getUrl().equals(tempStr)) {
                        listResult.add(item);
                        has = true;
                        break;
                    }
                }
            }
            //假如是没识别的，则生成一个加入列表
            if(!has){
                UrlKeyValueBean.DataBean item = new UrlKeyValueBean.DataBean();
                item.setTitle("网页链接");
                item.setUrl(tempStr);
                listResult.add(item);
            }
        }
        //重整字符串
        for(UrlKeyValueBean.DataBean item : listResult){
            String temp = item.getUrl();
            String tempStr = valueResult.replace(temp," " + item.getTitle() + "  ");
            valueResult = tempStr;
        }

        //改变样式
        SpannableString mSpannableString = new SpannableString(valueResult);
        Pattern pattern3 = Pattern.compile("《[^》]+》");
        Matcher matcher3 = pattern3.matcher(valueResult);
        while (matcher3.find()) {
            int start = matcher3.start();
            int end = matcher3.end();
            if (end - start <= 2) {
                continue;
            }
            Drawable d1 = RxImageTool.getDrawable(8, 16, R.mipmap.reg_icon_search);
            MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
            mSpannableString.setSpan(span1, end - 2, end - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.BlueNormalStyle), start + 1, end - 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        if(!listResult.isEmpty()) {
            List<Integer> hasIndex = new ArrayList<>();
            for (UrlKeyValueBean.DataBean item : listResult) {
                String temp = " " + item.getTitle() + " ";
                int indexTemp = valueResult.indexOf(temp);
                while (hasIndex.contains(indexTemp)) {
                    indexTemp = valueResult.indexOf(temp, indexTemp + temp.length());
                }
                //加入池子，下次不再允许匹配到此位置
                hasIndex.add(indexTemp);


                Drawable d1 = RxImageTool.getDrawable(12, 12, R.mipmap.icon_url);
                MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span1, indexTemp, indexTemp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MyImageSpan span2 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
                mSpannableString.setSpan(span2, indexTemp + temp.length() - 1, indexTemp + temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                mSpannableString.setSpan(new TextAppearanceSpan(context, R.style.BlueNormalStyle), indexTemp + 1, indexTemp + temp.length() - 1 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        tv.setText(mSpannableString);
        tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
    }


    //支持书名号和富文本网页标签的显示
//    public static void replaceBook(String value, Context context, TextView tv) {
//        if(null == tv){
//            return;
//        }
//        if (TextUtils.isEmpty(value)) {
//            tv.setText("");
//            return;
//        }
//
//        //字符串重整，修改书名相关
//        StringBuilder valueResultTemp = new StringBuilder();
//        Pattern pattern1 = Pattern.compile("《[^》]+》");
//        Matcher matcher1 = pattern1.matcher(value);
//        int index = 0;
//        while (matcher1.find()) {
//            int start = matcher1.start();
//            int end = matcher1.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            valueResultTemp.append(value.substring(index,end - 1));
//            valueResultTemp.append(" ");
//            index = end - 1;
//
//        }
//        valueResultTemp.append(value.substring(index,value.length()));
//
//        //解析html富文本，并设置点击事件
//        Spanned spannedHtml = Html.fromHtml(valueResultTemp.toString());
//        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
//        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
//        for (final URLSpan span : urls) {
//            setLinkClickable(clickableHtmlBuilder, span,context);
//        }
//
//
//
//        Pattern pattern3 = Pattern.compile("《[^》]+》");
//        Matcher matcher3 = pattern3.matcher(clickableHtmlBuilder);
//        while (matcher3.find()) {
//            int start = matcher3.start();
//            int end = matcher3.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//            MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//            clickableHtmlBuilder.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            String searchStr = matcher3.group();
//            clickableHtmlBuilder.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                    ds.setColor(0xFF3865FE);
//                }
//
//                @Override
//                public void onClick(View widget) {
//                    ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                }
//            }, start, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//
//
//        tv.setText(clickableHtmlBuilder);
//        tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//    }
//
//    private static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
//                                         final URLSpan urlSpan, final Context context) {
//        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
//        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
//        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
//        ClickableSpan clickableSpan = new ClickableSpan() {
//
//            public void onClick(View view) {
//                //Do something with URL here.
//                String url = urlSpan.getURL();
//                ActivityWebView.startActivityNeedUrlAuth(context,url);
//            }
//            public void updateDrawState(TextPaint ds) {
//                //设置颜色
//                ds.setColor(0xFF3865FE);
//                //设置是否要下划线
//                ds.setUnderlineText(false);
//            }
//
//        };
//        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
//    }



    //支持书名号和网页链接字样的显示
//    public static void replaceBook(String value, Context context, TextView tv) {
//        if(null == tv){
//            return;
//        }
//        if (TextUtils.isEmpty(value)) {
//            tv.setText("");
//            return;
//        }
//
//        //字符串重整
//        StringBuilder valueResultTemp = new StringBuilder();
//
//        Pattern pattern1 = Pattern.compile("《[^》]+》");
//        Matcher matcher1 = pattern1.matcher(value);
//        int index = 0;
//        while (matcher1.find()) {
//            int start = matcher1.start();
//            int end = matcher1.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            valueResultTemp.append(value.substring(index,end - 1));
//            valueResultTemp.append(" ");
//            index = end - 1;
//
//        }
//
//        valueResultTemp.append(value.substring(index,value.length()));
//
//        String valueResult = valueResultTemp.toString();
//
//        Pattern pattern2 = Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*");
//        Matcher matcher2 = pattern2.matcher(valueResult);
//        List<String> urlList = new ArrayList<>();
//        while (matcher2.find()) {
//            int start = matcher2.start();
//            int end = matcher2.end();
//            if (end - start <= 2) {
//                continue;
//            }
//            String temp = matcher2.group();
//            urlList.add(temp);
//            String tempStr = valueResult.replace(temp," 网页链接");
//            valueResult = tempStr;
//        }
//
//
//        //改变样式
//        SpannableString mSpannableString = new SpannableString(valueResult);
//
//        Pattern pattern3 = Pattern.compile("《[^》]+》");
//        Matcher matcher3 = pattern3.matcher(valueResult);
//        while (matcher3.find()) {
//            int start = matcher3.start();
//            int end = matcher3.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//            MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//            mSpannableString.setSpan(span1, end - 2, end-1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            String searchStr = matcher3.group();
//            mSpannableString.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                    ds.setColor(0xFF3865FE);
//                }
//
//                @Override
//                public void onClick(View widget) {
//                    ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                }
//            }, start, end - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//
//        Pattern pattern4 = Pattern.compile("网页链接");
//        Matcher matcher4 = pattern4.matcher(valueResult);
//        index = 0;
//        while (matcher4.find()) {
//            int start = matcher4.start();
//            int end = matcher4.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            Drawable d1 = RxImageTool.getDrawable(12,12,R.mipmap.icon_url);
//            MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//            mSpannableString.setSpan(span1, start - 1, start , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            String temp = urlList.get(index);
//            index++;
//            mSpannableString.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                    ds.setColor(0xFF3865FE);
//                }
//
//                @Override
//                public void onClick(View widget) {
//                    ActivityWebView.startActivityNeedUrlAuth(context,temp);
//                }
//            }, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//
//
//        tv.setText(mSpannableString);
//        tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//    }


    //支持书名号和完整网页链接显示
//    public static void replaceBook(String value, Context context, TextView tv) {
//        if(null == tv){
//            return;
//        }
//        if (TextUtils.isEmpty(value)) {
//            tv.setText("");
//            return;
//        }
//
//        Pattern pattern1 = Pattern.compile("《[^》]+》");
//        Matcher matcher1 = pattern1.matcher(value);
//
//        StringBuilder valueResult = new StringBuilder();
//        //制造全新的字符串并且记录每一个匹配的起始和结束坐标
//        List<String> valueList = new ArrayList<>();
//        List<Integer> startList = new ArrayList<>();
//        List<Integer> endList = new ArrayList<>();
//        int index = 0;
//        int count  = 0;
//        while (matcher1.find()) {
//            int start = matcher1.start();
//            int end = matcher1.end();
//            if (end - start <= 2) {
//                continue;
//            }
//
//            valueResult.append(value.substring(index,end - 1));
//            valueResult.append(" ");
//            index = end - 1;
//
//            String temp = matcher1.group();
//            valueList.add(temp);
//            startList.add(start + count);
//            endList.add(end + count);
//            count ++;
//        }
//
//        valueResult.append(value.substring(index,value.length()));
//
//        //改变样式
//        SpannableString mSpannableString = new SpannableString(valueResult);
//        for(int i = 0 ; i < valueList.size() ; i ++) {
//            Drawable d1 = RxImageTool.getDrawable(8,16,R.mipmap.reg_icon_search);
//            MyImageSpan span1 = new MyImageSpan(d1, ImageSpan.ALIGN_BOTTOM);
//            mSpannableString.setSpan(span1, endList.get(i) - 1, endList.get(i) , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            String searchStr = valueList.get(i);
//            mSpannableString.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
////                    ds.setUnderlineText (true);
//                    ds.setColor(0xFF3865FE);
//                }
//
//                @Override
//                public void onClick(View widget) {
//                    ActivitySearch.startActivity(context,searchStr.substring(1,searchStr.length()-1));
//                }
//            }, startList.get(i) + 1, endList.get(i) - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//
//        Pattern pattern2 = Pattern.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*");
//        Matcher matcher2 = pattern2.matcher(valueResult);
//        while (matcher2.find()) {
//            int start = matcher2.start();
//            int end = matcher2.end();
//            if (end - start <= 2) {
//                continue;
//            }
//            String temp = matcher2.group();
//            mSpannableString.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
////                    ds.setStrikeThruText(true);
//                    ds.setColor(0xFF3865FE);
//                }
//                @Override
//                public void onClick(View widget) {
//                    ActivityWebView.startActivityNeedUrlAuth(context,temp);
//
//                }
//            }, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//
//
//        tv.setText(mSpannableString);
//        tv.setMovementMethod(CustomLinkMovementMethod.getInstance());
//    }


    //支持书名号显示
//    public static void replaceBook(SpannableString value, Context context, TextView tv) {
//        if (TextUtils.isEmpty(value)) {
//            return;
//        }
//        if(null == tv){
//            return;
//        }
//        Pattern pattern1 = Pattern.compile("《[^》]+》");
//        Matcher matcher1 = pattern1.matcher(value);
//        while (matcher1.find()) {
//            int start = matcher1.start();
//            int end = matcher1.end();
//            if (end - start <= 2) {
//                continue;
//            }
//            String temp = matcher1.group();
//            value.setSpan(new ClickableSpan() {
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    ds.setUnderlineText (true);
////                    ds.setColor(0xFF3865FE);
//                }
//
//                @Override
//                public void onClick(View widget) {
//                    ActivitySearch.startActivity(context,temp.substring(1,temp.length()-1));
//                }
//            }, start + 1, end - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//        tv.setText(value);
//    }

    /*以下是url爬虫流程*/
    public static List<String> getUrlsInContent(String content) {
        String contenttemp = content;
        List<String> termList = new ArrayList<String>();

        String patternString = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
//        String patternString ="((http[s]{0,1}|ftp)://[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\\\.\\\\-]+\\\\.([a-zA-Z]{2,4})(:\\\\d+)?(/[a-zA-Z0-9\\\\.\\\\-~!@#$%^&*+?:_/=<>]*)?)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(contenttemp);

        while (matcher.find()) {
            termList.add(matcher.group());
        }

        return termList;
    }
//    //实时解析网页链接
//    public static void getOneUrlResult(String content) {
//        if (!TextUtils.isEmpty(content)) {
//            //获取链接数组和新拼接的带空格字符串
//            List<String> urlList = getUrlsInContent(content);
//            //有链接时。
//            if (urlList != null && !urlList.isEmpty()) {
//                List<UrlKeyValueBean.DataBean> mList = new ArrayList<>();
//                for (int i = 0 ; i <=  urlList.size() ; i++) {
//                    //从后往前的顺序显示。
//                    String temp = urlList.get(i);
//                    //去解析
//                    //解析网址，解析成功则显示解析后的结果
//                    Observable.create(new ObservableOnSubscribe<Map>() {
//                        @Override
//                        public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
//                            Map map = null;
//                            try {
//                                //这里开始是做一个解析，需要在非UI线程进行
//                                Document document = Jsoup.parse(new URL(temp), 5000);
//                                String title = document.head().getElementsByTag("title").text();
//                                map = new HashMap();
//                                map.put("code", "1");
//                                map.put("title", title);
//                                emitter.onNext(map);
//                            } catch (IOException e) {
//                                map = new HashMap();
//                                map.put("code", "0");
//                                emitter.onNext(map);
//                            }
//                        }
//                    }).subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<Map>() {
//                                @Override
//                                public void accept(Map map) throws Exception {
//
//                                    if (map.get("code").equals("1") && !TextUtils.isEmpty(map.get("title").toString())) {
//                                        String title = map.get("title").toString();
//                                        if (title.length() > 15) title = title.substring(0, 14) + "...";
//                                        //存入列表
//                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//                                        info.setUrl(temp);
//                                        info.setTitle(title);
//                                        mList.add(info);
//                                    } else {
//                                        String[] str = temp.split("\\.");
//                                        if (str.length > 1) {
//                                            //存入数据库
//                                            String str1 = str[str.length - 2];
//                                            String str2 = str[str.length - 1];
//                                            String[] strtemp = str2.split("\\/");
//                                            if(strtemp.length > 1){
//                                                str2 = strtemp[0];
//                                            }
//                                            StringBuilder sb = new StringBuilder();
//                                            sb.append(str1);
//                                            sb.append(".");
//                                            sb.append(str2);
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//                                            info.setUrl(temp);
//                                            info.setTitle(sb.toString());
//                                            mList.add(info);
//                                        } else {
//                                            //存入数据库
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//                                            info.setUrl(temp);
//                                            info.setTitle(temp);
//                                            mList.add(info);
//                                        }
//                                    }
//
//                                    //判断是不是所有的链接都有了结果
//                                    if(mList.size() == urlList.size()){
//                                        return mList;
//                                    }
//                                }
//                            });
//                }
//            }
//        }
//    }



}
