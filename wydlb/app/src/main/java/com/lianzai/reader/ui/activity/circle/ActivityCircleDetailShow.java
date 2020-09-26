package com.lianzai.reader.ui.activity.circle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.bean.CircleBookBean;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.PostDetailBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.BarPostItemAdapter;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.CommentEditText;
import com.lianzai.reader.view.CommentOptionPopup;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/13.
 * 圈子详情页的展示
 */

public class ActivityCircleDetailShow extends BaseActivity {

    @Bind(R.id.iv_blur_img)
    ImageView iv_blur_img;//模糊背景

    @Bind(R.id.iv_circle_detail_cover)
    SelectableRoundedImageView iv_circle_detail_cover;//书籍封面

    @Bind(R.id.tv_circle_detail_name)
    TextView tv_circle_detail_name;//书籍标题


    @Bind(R.id.tv_book_author)
    TextView tv_book_author;//书记作者


    @Bind(R.id.ll_circle_detail_tags)//普通书籍标签
    LinearLayout ll_circle_detail_tags;

    @Bind(R.id.ll_circle_detail_tags_guanfang)//官方标签
     LinearLayout ll_circle_detail_tags_guanfang;

    @Bind(R.id.tv_circle_detail_description)
    TextView tv_circle_detail_description;//书记描述


    private String platformCover;
    private String platformName;
    private String penName;
    private String platformIntroduce;
    private String typeText;
    private String categoryName;
    private int platformType;
    Bitmap blurBitmap;

    private final String LIANZAIGUANFANG = "连载官方";

    public static void startActivity(Context context, String platformCover, String platformName, String penName,
                                     String platformIntroduce, String typeText, String categoryName, int platformType) {
//        RxActivityTool.removeActivityPostDetail();
        Bundle bundle = new Bundle();
        bundle.putString("platformCover", platformCover);
        bundle.putString("platformName", platformName);
        bundle.putString("penName", penName);
        bundle.putString("platformIntroduce", platformIntroduce);
        bundle.putString("typeText", typeText);
        bundle.putString("categoryName", categoryName);
        bundle.putInt("platformType", platformType);
        RxActivityTool.skipActivity(context, ActivityCircleDetailShow.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail_show;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }

        try {
            platformCover = getIntent().getExtras().getString("platformCover");
            platformName = getIntent().getExtras().getString("platformName");
            penName = getIntent().getExtras().getString("penName");
            platformIntroduce = getIntent().getExtras().getString("platformIntroduce");
            typeText = getIntent().getExtras().getString("typeText");
            categoryName = getIntent().getExtras().getString("categoryName");
            platformType = getIntent().getExtras().getInt("platformType");
        } catch (Exception e) {

        }


        //圈子头像背景显示
        int errorImgId=RxImageTool.getNoCoverImgByRandom();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.placeholder(errorImgId);
        requestOptions.error(errorImgId);
        requestOptions.skipMemoryCache(false);
        requestOptions.dontAnimate();
        Glide.with(ActivityCircleDetailShow.this).load(platformCover).apply(requestOptions).listener(new RequestListener<Drawable>() {
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
        }).into(iv_circle_detail_cover);

        tv_circle_detail_name.setText(platformName);
        if (platformType == Constant.UserIdentity.BOOK_PUBLIC_NUMBER || platformType == Constant.UserIdentity.BOOK_OUTSIED_NUMBER){
            tv_book_author.setVisibility(View.VISIBLE);
            tv_book_author.setText(penName + "著");

            //显示圈子标签
            List<String> tags = new ArrayList<>();
            if(!TextUtils.isEmpty(typeText)) {
                tags.add(typeText);
            }
            if(!TextUtils.isEmpty(categoryName)) {
                tags.add(categoryName);
            }
            if(tags.size() > 0) {
                showBookListTags(tags,ll_circle_detail_tags);
            }
            ll_circle_detail_tags.setVisibility(View.VISIBLE);
            ll_circle_detail_tags_guanfang.setVisibility(View.INVISIBLE);
        }else {
            tv_book_author.setVisibility(View.GONE);
            //显示圈子标签
            List<String> tags = new ArrayList<>();
            tags.add(LIANZAIGUANFANG);
            showBookListTags(tags,ll_circle_detail_tags_guanfang);
            ll_circle_detail_tags.setVisibility(View.INVISIBLE);
            ll_circle_detail_tags_guanfang.setVisibility(View.VISIBLE);
        }

        tv_circle_detail_description.setText(platformIntroduce);

    }



    @OnClick(R.id.iv_close)
    void tv_sendClick() {
      finish();
    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float radius = 100;//模糊程度
        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

    /**
     * 动态添加标签
     *
     * @param tags
     */
    private void showBookListTags(List<String> tags,LinearLayout ly) {
        if (null == tags) return;
        ly.removeAllViews();//清除所有Tag
        for (String tag : tags) {
            TextView textView = new TextView(this);
            textView.setPadding(RxImageTool.dip2px(10), RxImageTool.dip2px(3), RxImageTool.dip2px(10), RxImageTool.dip2px(3));

            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.label_corner_bg);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, RxImageTool.dip2px(8), 0);
            textView.setLayoutParams(layoutParams);

            textView.setText(tag);
            if(tag.equals("连载官方")){
                textView.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_lianzai_guanfang),null,null,null);
            }
            ly.addView(textView);
        }
    }



}
