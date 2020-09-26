package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.bean.CommentDetailBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.UrlKeyValueBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.BarPostFloorItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CommentEditText;
import com.lianzai.reader.view.CommentOptionPopup;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by lrz on 2019/03/20.
 * 评论详情弹框
 */

public class ActivityPostFloor extends BaseActivityForTranslucent implements BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.ed_reply)
    CommentEditText ed_reply;

    @Bind(R.id.activity_creatquanzi_recycle)
    RecyclerView activity_creatquanzi_recycle;

    @Bind(R.id.tv_show_post)
    TextView tv_show_post;

    @Bind(R.id.tv_send)
    TextView tv_send;//发送按钮

    /**
     * 显示的数据
     */
    private List<CommentDetailBean.DataBean.ListBean> replyListData = new ArrayList<>();
    /**
     * RecyclerView的适配器
     */
    private BarPostFloorItemAdapter barPostFloorItemAdapter;

    CustomLoadMoreView customLoadMoreView;

    int page = 1;


    String commentId;//评论ID

    CommentOptionPopup commentOptionPopup;
    //选择的回复Id
    private int replyId;
    private CommentDetailBean baseBean;
    //用于定位的回复Id
    private String focusId;

    //原动态id
    private String postId;
    private String circleId;
    private int userType = Constant.Role.FANS_ROLE;

    @Override
    public int getLayoutId() {
        return R.layout.activity_post_floor;
    }

    public static void startActivity(Context context, String commentId, String focusId,String postId,String circleId) {
//        RxActivityTool.removeActivityPostFloor();
        Bundle bundle = new Bundle();
        bundle.putString("commentId", commentId);
        bundle.putString("focusId", focusId);
        bundle.putString("postId", postId);
        bundle.putString("circleId", circleId);
        RxActivityTool.skipActivity(context, ActivityPostFloor.class, bundle);
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }
    @Override
    public void configViews(Bundle savedInstanceState) {
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        try{
            commentId = getIntent().getExtras().getString("commentId");
            focusId = getIntent().getExtras().getString("focusId");
            postId = getIntent().getExtras().getString("postId");
            circleId = getIntent().getExtras().getString("circleId");

        }catch (Exception e){

        }

        if(TextUtils.isEmpty(postId)){
            tv_show_post.setVisibility(View.GONE);
        }else {
            tv_show_post.setVisibility(View.VISIBLE);
        }

        ed_reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = ed_reply.getSelectionStart();
                if (index>=3&&s.toString().substring(index-3,index).equals("\n\n\n")){
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0,index-1));
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }
                if (index>=3&&s.toString().substring(index-3,index).equals("\n\n ")){
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0,index-1));
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }
                if(index >= 1 &&s.length() >= index+2 && s.toString().substring(index-1,index+2).equals("\n\n\n")){
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }
                if(index >= 1 &&s.length() >= index+2 && s.toString().substring(index-1,index+2).equals(" \n\n")){
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }
                if(index >= 2 && s.length() >= index+1 && s.toString().substring(index-2,index+1).equals("\n\n\n")){
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }
                if(index >= 2 && s.length() >= index+1 && s.toString().substring(index-2,index+1).equals("\n \n")){
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if(s.length() > index){
                        sb.append(s.toString().substring(index,s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index-1);
                }

                if(TextUtils.isEmpty(ed_reply.getText())){
                    tv_send.setTextColor(getResources().getColor(R.color.third_text_color));
                }else {
                    tv_send.setTextColor(getResources().getColor(R.color.bluetext_color));
                }

            }
        });

        tv_send.setOnClickListener(
                new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        RxKeyboardTool.hideKeyboard(ActivityPostFloor.this, ed_reply);
                        if(RxLoginTool.isLogin()) {
                            String content = ed_reply.getPublishText();
                            if (!TextUtils.isEmpty(content)) {
                                //回复的主函数
                                getOneUrlResultForReplyComment(content);
                            }
                        }else {
                            ActivityLoginNew.startActivity(ActivityPostFloor.this);
                        }
                    }
                });

        barPostFloorItemAdapter = new BarPostFloorItemAdapter(replyListData);
        barPostFloorItemAdapter.setOnLoadMoreListener(this, activity_creatquanzi_recycle);
        customLoadMoreView = new CustomLoadMoreView();
        barPostFloorItemAdapter.setLoadMoreView(customLoadMoreView);

        barPostFloorItemAdapter.setContentClickListener(new BarPostFloorItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityPostFloor.this, String.valueOf(replyListData.get(pos).getReplyUserId()));
                } catch (Exception e) {
                }
            }

            @Override
            public void praiseClick(View v, int pos) {
                try {
                    CommentDetailBean.DataBean.ListBean bean = replyListData.get(pos);
                    //点赞
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostFloor.this);
                        return;
                    }
                    if(!bean.isIsLike())
                    praiseRequest(String.valueOf(bean.getId()), 3,pos);
                } catch (Exception e) {

                }
            }

            @Override
            public void replyClick(View v, int pos) {
            }

            @Override
            public void contentClick(View v, int pos) {
                try {
                    CommentDetailBean.DataBean.ListBean bean = replyListData.get(pos);
                    //弹出操作框
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostFloor.this);
                        return;
                    }
                    if (null == commentOptionPopup) {
                        commentOptionPopup = new CommentOptionPopup(ActivityPostFloor.this);
                    }
                    commentOptionPopup.getTv_reply().setOnClickListener(
                            v1 -> {
                                //回复点击
                                replyId = bean.getId();
                                ed_reply.setHint("回复" + bean.getReplyUserName() + "：");
                                ed_reply.setText("");
                                showInputMethod();
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_copy().setOnClickListener(
                            v1 -> {
                                //复制点击
                                RxClipboardTool.copyText(ActivityPostFloor.this, bean.getContentShow());
                                commentOptionPopup.dismiss();
                                RxToast.custom("复制成功").show();
                            }
                    );

                    commentOptionPopup.getTv_report().setOnClickListener(
                            v1 -> {
                                //举报点击
                                try {
                                    //举报5：圈子动态 6：圈子评论 7：圈子回复
                                    againstCircleUser("7",String.valueOf(bean.getId()));
                                } catch (Exception e) {
                                }
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_delete().setOnClickListener(
                            v1 -> {
                                //删除点击
                                //删除
                                AccountDetailBean account = RxTool.getAccountBean();
                                if(null != account) {
                                    if (account.getData().getUid().equals(String.valueOf(bean.getReplyUserId()))) {
                                        //删除自己的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("contentType", 3);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        deleteWay(jsonObject.toString(),1,pos);
                                    } else {
                                        //删除别人的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("circleId", bean.getCircleId());
                                        jsonObject.addProperty("objectType", 3);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        jsonObject.addProperty("objectUserId", bean.getReplyUserId());
                                        deleteWay(jsonObject.toString(),2,pos);
                                    }
                                }else {
                                    ActivityLoginNew.startActivity(ActivityPostFloor.this);
                                }

                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_mute().setOnClickListener(
                            v1 -> {
                                //禁言
                                muteWay(bean.getReplyUserId(),String.valueOf(bean.getCircleId()));
                                commentOptionPopup.dismiss();
                            }
                    );
                    commentOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            v.setSelected(false);
                        }
                    });

                    try {
                        //区分是自己还是别人的动态
                        AccountDetailBean account = RxTool.getAccountBean();
                        if (null != account) {
                            boolean equal = false;
                            if (account.getData().getUid().equals(String.valueOf(bean.getReplyUserId()))) {
                                equal = true;
                            }else {
                                equal = false;
                            }
                            if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE&& equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else {
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }
                        } else {
                            commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                            commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                            commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        RxLogTool.e(e.toString());
                    }
                    v.setSelected(true);
                    commentOptionPopup.showPopupWindow(ActivityPostFloor.this, v);

                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });


        activity_creatquanzi_recycle.setLayoutManager(new RxLinearLayoutManager(this));
        //设置添加,移除item的动画,DefaultItemAnimator为默认的
        activity_creatquanzi_recycle.setItemAnimator(new DefaultItemAnimator());
        activity_creatquanzi_recycle.setAdapter(barPostFloorItemAdapter);
//        activity_creatquanzi_recycle.addItemDecoration(new RxRecyclerViewDividerTool(0,0 , 0,RxImageTool.dip2px(10)));


        requestCommentDetail(true);
        getCircleRole();
    }


    @OnClick(R.id.tv_show_post)
    void tv_show_postClick(){
        //隐藏键盘
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                RxKeyboardTool.hideKeyboard(ActivityPostFloor.this,ed_reply);
            }
        },10);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityPostDetail.startActivity(ActivityPostFloor.this,postId,commentId);
            }
        },200);

    }

    private void showInputMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    RxKeyboardTool.showSoftInput(ActivityPostFloor.this, ed_reply);
                } catch (Exception e) {
                }
            }
        }, 200);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.img_back)void closeClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }


    @Override
    public void onLoadMoreRequested() {
        requestCommentDetail(false);
    }

    /**
     * 话题详情
     */
    private void requestCommentDetail(boolean isRefresh) {

        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNumber", String.valueOf(page));
        map.put("pageSize", String.valueOf(100));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/commentDetail/" + commentId, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/commentDetail/").show();
                    barPostFloorItemAdapter.loadMoreFail();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("requestHistory:" + response);
                    baseBean = GsonUtil.getBean(response, CommentDetailBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (isRefresh) {
                            tv_title.setText("共" + baseBean.getData().getTotal() + "条回复");
                            replyListData.clear();
                            if (null != baseBean.getData().getList()) {
                                replyListData.addAll(baseBean.getData().getList());
                            }
                            if (replyListData.size() > 0) {
                                barPostFloorItemAdapter.replaceData(replyListData);
                                barPostFloorItemAdapter.setEnableLoadMore(true);
                                barPostFloorItemAdapter.loadMoreComplete();
                                //进行匹配定位
                                if(!TextUtils.isEmpty(focusId)){
                                    for(int i = 0; i< replyListData.size() ; i++){
                                        String tempId = String.valueOf(replyListData.get(i).getId());
                                        if(tempId.equals(focusId)){
                                            activity_creatquanzi_recycle.scrollToPosition(i + barPostFloorItemAdapter.getHeaderLayoutCount());
                                            break;
                                        }
                                    }
                                    focusId = null;
                                }
                            } else {
                                barPostFloorItemAdapter.replaceData(replyListData);
                                barPostFloorItemAdapter.setEnableLoadMore(false);
                                barPostFloorItemAdapter.loadMoreComplete();
                            }
                        } else {
                            if (null != baseBean.getData().getList() && !baseBean.getData().getList().isEmpty()) {
                                replyListData.addAll(baseBean.getData().getList());
                                barPostFloorItemAdapter.setNewData(replyListData);
                                barPostFloorItemAdapter.loadMoreComplete();
                            } else {
                                barPostFloorItemAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        barPostFloorItemAdapter.loadMoreFail();
                        RxToast.custom(baseBean.getMsg()).show();
                    }

                } catch (Exception e) {
                    barPostFloorItemAdapter.loadMoreFail();
                }
            }
        });
    }

    //实时解析网页链接
    public void getOneUrlResultForReplyComment(String content) {
        if (!TextUtils.isEmpty(content)) {
            //获取链接数组和新拼接的带空格字符串
            List<String> urlList = URLUtils.getUrlsInContent(content);
            //有链接时。
            if (urlList != null && !urlList.isEmpty()) {
                List<UrlKeyValueBean.DataBean> mList = new ArrayList<>();
                for (int i = 0 ; i <  urlList.size() ; i++) {
                    //从后往前的顺序显示。
                    String temp = urlList.get(i);
                    //去解析
                    //解析网址，解析成功则显示解析后的结果
                    Observable.create(new ObservableOnSubscribe<Map>() {
                        @Override
                        public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                            Map map = null;
                            try {
                                //这里开始是做一个解析，需要在非UI线程进行
                                Document document = Jsoup.parse(new URL(temp), 5000);
                                String title = document.head().getElementsByTag("title").text();
                                map = new HashMap();
                                map.put("code", "1");
                                map.put("title", title);
                                emitter.onNext(map);
                            } catch (IOException e) {
                                map = new HashMap();
                                map.put("code", "0");
                                emitter.onNext(map);
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Map>() {
                                @Override
                                public void accept(Map map) throws Exception {

                                    if (map.get("code").equals("1") && !TextUtils.isEmpty(map.get("title").toString())) {
                                        String title = map.get("title").toString();
                                        if (title.length() > 20) title = title.substring(0, 19) + "...";
                                        //存入列表
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle(title);
                                        mList.add(info);
                                    } else {
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
//
//                                            String title = sb.toString();
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        } else {
//                                            //存入数据库
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = temp;
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        }
                                        //存入数据库
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle("网页链接");
                                        mList.add(info);
                                    }

                                    //判断是不是所有的链接都有了结果
                                    if(mList.size() == urlList.size()){
                                        UrlKeyValueBean uploadBean = new UrlKeyValueBean();
                                        uploadBean.setList(mList);
                                        String uploadStr = GsonUtil.toJsonString(uploadBean);
                                        replyComment(content,uploadStr);
                                    }
                                }
                            });
                }
            }else {
                replyComment(content,"");
            }
        }
    }

    //所有回复操作，commentId填所属评论的id，parentId假如有回复的回复对象时，填对象id
    private void replyComment(String content,String uploadStr) {
        if(null != tv_send){
            tv_send.setEnabled(false);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commentId", commentId);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("urlTitle", uploadStr);
        jsonObject.addProperty("platformId", circleId);
        if(replyId != 0){
            jsonObject.addProperty("parentId", replyId);
        }
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/dynamicReply", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            //此处展示金钻消耗toast
                            RxToast.showJinzuanCharge(circleDynamicBean);
                            ed_reply.setText("");
                            requestCommentDetail(true);
                    }else if(circleDynamicBean.getCode() == Constant.ResponseCodeStatus.DISABLE_CIRCLE) {
                        //跳到圈子已关闭的页面
                        ActivityCircleClose.startActivity(ActivityPostFloor.this);
                        finish();
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /*1、动态点赞2、评论点赞3、回复点赞*/
    public void praiseRequest(String objectId, int likeType,int position) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("likeType", likeType);
        jsonObject.addProperty("objectId", objectId);
        jsonObject.addProperty("platformId", circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/like", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            //此处展示金钻消耗toast
                            RxToast.showJinzuanCharge(circleDynamicBean);

                        //更新点赞状态
                        CommentDetailBean.DataBean.ListBean listBean = replyListData.get(position);
                        int count = listBean.getLikeCount();
                        count++;
                        listBean.setIsLike(true);
                        listBean.setLikeCount(count);
                        replyListData.set(position,listBean);
                        barPostFloorItemAdapter.replaceData(replyListData);
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除动态或者评论内容类型：1-帖子，2-评论，3-回复
     */
    private void requestDelete(String json,int pos) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/delete", json, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("删除成功").show();
                        CommentDetailBean.DataBean.ListBean temp = barPostFloorItemAdapter.getData().get(pos);
                        temp.setAuditStatus(4);
                        barPostFloorItemAdapter.setData(pos,temp);
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 删除动态
     */
    private void requestDeleteOther(String json,int postion) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/deleteCircleContent",json, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("删除成功").show();
                        CommentDetailBean.DataBean.ListBean temp = barPostFloorItemAdapter.getData().get(postion);
                        temp.setAuditStatus(4);
                        barPostFloorItemAdapter.setData(postion,temp);
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 举报圈子用户
     */
    private void againstCircleUser(String source,String userId){
        HashMap map=new HashMap();
        map.put("source",source);
        map.put("targetId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/against/circleUser",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("举报成功").show();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示
    private void muteWay(int bannedUserId,String circleId){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("禁言");
        rxDialogSureCancelNew.setContent("确定对Ta进行禁言吗？禁言后，Ta将不能再在圈子里发表任何言论。");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                requestMute(4,bannedUserId,circleId);
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    private void deleteWay(String json,int type,int pos){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("删除");
        rxDialogSureCancelNew.setContent("确定删除该内容吗？");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                if(type == 2){
                    requestDeleteOther(json,pos);
                }else if(type == 1){
                    requestDelete(json,pos);
                }
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    /**
     * 禁言动态
     */
    private void requestMute(int bannedReplyType ,int bannedUserId,String circleId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bannedReplyType", bannedReplyType);
        jsonObject.addProperty("bannedUserId", bannedUserId);
        jsonObject.addProperty("circleId",circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/bannedCircleMember",jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("禁言成功").show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /* 获取圈子角色*/
    private void getCircleRole () {
        Map<String, String> map = new HashMap<>();
        map.put("objectType", "2");
        map.put("objectId",commentId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/getCircleRole", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseCountBean baseCountBean = GsonUtil.getBean(response, BaseCountBean.class);
                    if (baseCountBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        userType = baseCountBean.getData();
                    }else{//加载失败
                        RxToast.custom(baseCountBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }

}
