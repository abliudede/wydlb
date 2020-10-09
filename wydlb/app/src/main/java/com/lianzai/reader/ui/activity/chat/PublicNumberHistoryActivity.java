package com.lianzai.reader.ui.activity.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.PublicNumberHistoryBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.adapter.PublicNumberHistoryAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;

public class PublicNumberHistoryActivity extends BaseActivity implements  BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    PublicNumberHistoryAdapter publicNumberHistoryAdapter;

    View headView;

    boolean isLoadMore = false;
    boolean isError = false;
    int page = 1;
    int pageSize=10;

    CustomLoadMoreView customLoadMoreView;

    List<PublicNumberHistoryBean.DataBeanX.ListBean> listBeans=new ArrayList<>();

    String objectId;

    SelectableRoundedImageView iv_book_cover;

    TextView tv_book_name;

    PublicNumberHistoryBean.DataBeanX dataBeanX;
    private ImageView img_back;
    private TextView tv_book_author;
    private TextView tv_book_status_and_category;
    private ImageView iv_blur_img;
    Bitmap blurBitmap;//头部高斯模糊背景
    private String penname = "";
    private String status = "";
    int type;//角色
    private long startMs;

    public static void startActivity(Context context,String objectId,String penname,String status) {
        Intent intent=new Intent(context, PublicNumberHistoryActivity.class);
        intent.putExtra(Constant.PENNAME, penname);
        intent.putExtra(Constant.STATUS, status);
        intent.putExtra("objectId",objectId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_public_number_history;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //任务-沉浸式状态栏
        SystemBarUtils.fullScreen(this);
        startMs = System.currentTimeMillis();
        try {
            objectId = getIntent().getStringExtra("objectId");
            penname = getIntent().getStringExtra(Constant.PENNAME);
            status = getIntent().getStringExtra(Constant.STATUS);
        }catch (Exception e){

        }

        headView=getLayoutInflater().inflate(R.layout.view_history_header,null);
        iv_blur_img = headView.findViewById(R.id.iv_blur_img);
        img_back = headView.findViewById(R.id.img_back);
        iv_book_cover=headView.findViewById(R.id.iv_book_cover);
        tv_book_name=headView.findViewById(R.id.tv_book_name);
        tv_book_author = headView.findViewById(R.id.tv_book_author);
        tv_book_status_and_category = headView.findViewById(R.id.tv_book_status_and_category);

        if (!TextUtils.isEmpty(penname)){
            tv_book_author.setText(penname);
        }else{
            tv_book_author.setText("");
        }

        if (!TextUtils.isEmpty(status)){
            tv_book_status_and_category.setText(status);
        }else{
            tv_book_status_and_category.setText("");
        }

        img_back.setOnClickListener(
                v -> {
                    backClick();
                }
        );

        publicNumberHistoryAdapter=new PublicNumberHistoryAdapter(listBeans,this);
        publicNumberHistoryAdapter.addHeaderView(headView);

        customLoadMoreView = new CustomLoadMoreView();
        publicNumberHistoryAdapter.setLoadMoreView(customLoadMoreView);
        publicNumberHistoryAdapter.setOnLoadMoreListener(this,rv_data);

        publicNumberHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null==dataBeanX)return;
                PublicNumberHistoryBean.DataBeanX.ListBean bean=publicNumberHistoryAdapter.getData().get(position);
                if (bean.getNewsType()==Constant.EXTENDFIELD.OUTSIDE_CHAPTER_UPADTE_TYPE||bean.getNewsType()==Constant.EXTENDFIELD.LOCAL_CHAPTER_UPADTE_TYPE){
                    UrlReadActivity.startActivityOutsideRead(PublicNumberHistoryActivity.this,String.valueOf(dataBeanX.getBookId()),"",false,"",String.valueOf(bean.getData().getTitle()),0,false);
                }else if(bean.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_WEEKLY){
                    if (!TextUtils.isEmpty(bean.getData().getUrl())){
                        //周报类型2
                        ActivityWebView.startActivity(PublicNumberHistoryActivity.this,bean.getData().getUrl(),bean.getData().getTitle(),bean.getData().getContent(),bean.getData().getImg(),2);
                    }
                } else if(bean.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOTICE){
                    if (!TextUtils.isEmpty(bean.getData().getUrl())){
                        //官方公告类型
                        ActivityWebView.startActivity(PublicNumberHistoryActivity.this,bean.getData().getUrl());
                    }
                }
                else if(bean.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOSHARE){
                    if (!TextUtils.isEmpty(bean.getData().getUrl())){
                        //不可分享类型
                        ActivityWebView.startActivity(PublicNumberHistoryActivity.this,bean.getData().getUrl(),1);
                    }
                }else if(bean.getNewsType()==Constant.EXTENDFIELD.JOIN_TEAM_MESSAGE){
                  //加入群聊类型,判空
                    if(null != bean.getData()){
                        if(0 != bean.getData().getBookId()){
                            joinTeamChat(String.valueOf(bean.getData().getBookId()));
                        }else {
                            RxToast.custom("消息已失效").show();
                        }
                    }else {
                        RxToast.custom("消息已失效").show();
                    }
                }
            }
        });

        rv_data.setAdapter(publicNumberHistoryAdapter);
//        rv_data.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_data.setLayoutManager(new RxLinearLayoutManager(this));


        RxLogTool.e("requestHistory time:" + (System.currentTimeMillis() - startMs));
        requestHistory();
    }

    /**
     * 加入群聊
     *
     * @param teamId
     */
    public void joinTeamChat(String teamId) {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/join" , map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        TeamMessageActivity.startActivity(PublicNumberHistoryActivity.this,teamId);
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @Override
    public void gc() {

    }

    @Override
    public void onLoadMoreRequested() {
        if (publicNumberHistoryAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            if (rv_data.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (rv_data.isComputingLayout() == false)) {
                publicNumberHistoryAdapter.loadMoreEnd();
            }
        } else {
            if (!isError) {
                isLoadMore=true;
                page++;
                requestHistory();
            } else {
                isError = true;
                publicNumberHistoryAdapter.loadMoreFail();
            }
        }
    }


    private void requestHistory(){
        Map<String, String> map=new HashMap<>();
        map.put("pageNum",String.valueOf(page));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/news/"+objectId ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("加载失败/platforms/news/").show();
                    publicNumberHistoryAdapter.loadMoreFail();
                    RxLogTool.e("requestHistory e:"+e.getMessage());
                }catch (Exception ee){

                }

            }

            @Override
            public void onResponse(String response) {
                try{
                    RxLogTool.e("requestHistory gettime:" + (System.currentTimeMillis() - startMs));
                    RxLogTool.e("requestHistory:"+response);
                    PublicNumberHistoryBean baseBean= GsonUtil.getBean(response,PublicNumberHistoryBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if (null==dataBeanX){
                            dataBeanX=baseBean.getData();
                            //模糊背景以及加载
                            int errorImgId=RxImageTool.getNoCoverImgByRandom();
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                            requestOptions.placeholder(errorImgId);
                            requestOptions.error(errorImgId);
                            requestOptions.skipMemoryCache(false);
                            requestOptions.dontAnimate();
                            Glide.with(PublicNumberHistoryActivity.this).load(dataBeanX.getPlatformCover()).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    blurBitmap = RxImageTool.drawable2Bitmap(getResources().getDrawable(errorImgId), 5);
                                    blur(blurBitmap, iv_blur_img);
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    RxLogTool.e("onResourceReady:");
                                    blurBitmap = RxImageTool.drawable2Bitmap(resource, 5);
                                    blur(blurBitmap, iv_blur_img);

                                    return false;
                                }
                            }).into(iv_book_cover);

                            tv_book_name.setText(dataBeanX.getPlatformTitle());
                        }

                        if (!isLoadMore) {
                            listBeans.clear();
                            listBeans.addAll(baseBean.getData().getList());
                            if (listBeans.size() > 0) {
                                publicNumberHistoryAdapter.replaceData(listBeans);
                                publicNumberHistoryAdapter.setEnableLoadMore(true);
                                publicNumberHistoryAdapter.loadMoreComplete();
                            } else {

                                publicNumberHistoryAdapter.replaceData(listBeans);
//                                publicNumberHistoryAdapter.setEmptyView(R.layout.view_records_empty,rv_data);

                            }
                        } else {
                            if (baseBean.getData().getList().size() > 0) {
                                listBeans.addAll(baseBean.getData().getList());
                                publicNumberHistoryAdapter.setNewData(listBeans);
                            } else {
                                publicNumberHistoryAdapter.loadMoreEnd();
                            }
                        }
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }

                }catch (Exception e){
//                    RxToast.custom("加载失败/platforms/news/").show();
                    publicNumberHistoryAdapter.loadMoreFail();
                    RxLogTool.e("requestHistory e:"+e.getMessage());
                }


            }
        });
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {

        float radius = 40;//模糊程度

        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

}
