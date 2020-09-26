package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.SearchBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 搜索首页
 */

public class ActivitySearchFirst extends BaseActivity {

    @Bind(R.id.et_search)
    EditText et_search;

    @Bind(R.id.icon_search)
    ImageView icon_search;

    @Bind(R.id.rl_view)
    RelativeLayout rl_view;

    @Bind(R.id.iv_logo)
    ImageView iv_logo;



    public static void startActivity(Context context) {
        RxActivityTool.skipActivity(context, ActivitySearchFirst.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_first;
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
        //状态栏字体设置成黑色
        SystemBarUtils.setLightStatusBar(this, true);

        //键盘显示监听
        et_search.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                final Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                final int heightDifference = screenHeight - rect.bottom;
                boolean visible = heightDifference > screenHeight / 3;
                if(visible){
                    //"软键盘显示"
                    iv_logo.setVisibility(View.GONE);
                }else {
                    //"软键盘隐藏"
                    iv_logo.setVisibility(View.VISIBLE);
                }
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    skiptoSearch(et_search.getText().toString(),ActivitySearchFirst.this);
                    return true;
                }
                return false;
            }
        });

        icon_search.setOnClickListener(
                v -> {
                    skiptoSearch(et_search.getText().toString(),ActivitySearchFirst.this);
                }
        );
    }

//    public static void requestSearch(String platformName,Context context){
//        if(TextUtils.isEmpty(platformName)){
//            RxToast.custom("请输入搜索关键词").show();
//            return;
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("platformName", platformName);
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/search/skip", map, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                try {
////                    RxToast.custom("网络错误").show();
//                } catch (Exception ex) {
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    SearchBean searchBean = GsonUtil.getBean(response, SearchBean.class);
//                    if (searchBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                        //1：网页 2：内站圈子首页 3外站列表
//                        switch (searchBean.getData().getType()){
//                            case 1:
//                                skiptoSearch(searchBean.getData().getBookIds(),platformName,context);
//                                break;
//                            case 2:
//                                ActivityCircleDetail.startActivity(context,String.valueOf(searchBean.getData().getPlatformId()));
//                                break;
//                            case 3:
//                                skiptoSearch(searchBean.getData().getBookIds(),platformName,context);
//                                break;
//                        }
//                    } else {
//                        RxToast.custom(searchBean.getMsg()).show();
//                    }
//                } catch (Exception e) {
////                    RxToast.custom("网络错误").show();
//                }
//            }
//        });
//    }



    public static void skiptoSearch(String platformName,Context context){
        if(TextUtils.isEmpty(platformName)){
            RxToast.custom("没有搜索关键词").show();
            return;
        }
        ActivitySearch2.startActivity(context,platformName);
    }

    //跳到搜索页书荒神器tab
    public static void skiptoSearch2(String platformName,Context context){
        if(TextUtils.isEmpty(platformName)){
            RxToast.custom("没有搜索关键词").show();
            return;
        }
        ActivitySearch2.startActivityToBookShortage(context,platformName);
    }


    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }


    @OnClick(R.id.img_back)void img_backClick(){
       finish();
    }

}
