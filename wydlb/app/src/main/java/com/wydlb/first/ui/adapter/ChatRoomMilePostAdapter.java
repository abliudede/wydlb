package com.wydlb.first.ui.adapter;

import android.app.Activity;

import androidx.annotation.Nullable;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.MilepostBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.TimeFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2018/04/27.
 */

public class ChatRoomMilePostAdapter extends BaseQuickAdapter<MilepostBean.DataBean.ListBean,BaseViewHolder> {


    Activity mActivity;
    private ArrayList<EUidBean> termList;


    public ChatRoomMilePostAdapter(@Nullable List<MilepostBean.DataBean.ListBean> data, Activity activity) {
        super(R.layout.view_item_milepost, data);
        this.mActivity=activity;
        mContext=mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, MilepostBean.DataBean.ListBean bean) {
        if (baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()==0){
            ViewGroup.LayoutParams params=baseViewHolder.getConvertView().getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams=null;

            if (params instanceof ViewGroup.MarginLayoutParams){
                marginLayoutParams=(ViewGroup.MarginLayoutParams) params;
            }else{
                marginLayoutParams=new ViewGroup.MarginLayoutParams(params);
            }
            marginLayoutParams.setMargins(0, RxImageTool.dip2px(10),0,0);

            baseViewHolder.getConvertView().setLayoutParams(marginLayoutParams);
        }
        TextView tv_date=baseViewHolder.getView(R.id.tv_date);
        TextView tv_content=baseViewHolder.getView(R.id.tv_content);

        java.util.Date createAt = new java.util.Date(Long.parseLong(bean.getCreatedAt()));
        String interval = TimeFormatUtil.getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        tv_date.setText(interval);
        //<\lz name=id >
        String tempcontent = bean.getContents();
        String patternString = "</lz[^>]+=\\d+>";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(tempcontent);
        termList = new ArrayList<EUidBean>();
        //循环加入数组
        int from = 0;
        while (matcher.find()) {
            String source = matcher.group();
            try {
                String[] list = source.split("=");
                String name = list[0].substring(4);
                String uid = list[1].replace(">","");
                EUidBean uidbean = new EUidBean();
                uidbean.setSource(source);
                uidbean.setName(name);
                uidbean.setUid(uid);
                //获取字符串开头位置
                int index = tempcontent.indexOf(source, from);
                //得到下次寻找的开头位置
                from = index + name.length();
                tempcontent = tempcontent.replaceFirst(source, name);
                uidbean.setIndex(index);
                termList.add(uidbean);
            }catch (Exception e){
            }
        }
        //根据数组内容显示数据
        SpannableString mm = new SpannableString(tempcontent);
        //有匹配时。
        if (!termList.isEmpty()) {
            //进入循环
            for (EUidBean item : termList) {
                if (!TextUtils.isEmpty(item.getName())) {
                    //文字变色
                    int start = item.getIndex();
                    int end = start + item.getName().length();
                    if(end >= mm.length()) continue;
                    if(start < 0) continue;

                    mm.setSpan(new ClickableSpan() {
                        @Override
                        public void updateDrawState(TextPaint ds) {
                                ds.setColor(0xFF3865FE);
                                ds.setUnderlineText(false);
                        }
                        @Override
                        public void onClick(View widget) {
                            //设置点击事件,根据uid跳转
//                          //  item.getUid();
                        }
                    },start ,end , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        tv_content.setText(mm);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public class EUidBean{
        public String source;
        public String name;
        public String uid;
        public int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

}
