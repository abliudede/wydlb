package com.lianzai.reader.ui.activity.UrlIdentification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.bean.CommentDetailBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.UrlKeyValueBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleClose;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.adapter.BarPostFloorItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
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
 * Created by lrz on 2020/01/17.
 * 外站书阅读的全局弹框
 */

public class ActivityReadDialogActivity extends BaseActivityForTranslucent {

    @Bind(R.id.tv_open_browser)
    TextView tv_open_browser;

    @Bind(R.id.tv_skip_tool)
    TextView tv_skip_tool;




    private String bookId;
    private String bookSource;
    private boolean isStartBrowser = false;
    private boolean isStartTool = false;
    private String skipUrl;
    private String downloadUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_read_dialog;
    }

    public static void startActivity(Context context, String bookId,String bookSource, boolean isStartBrowser, boolean isStartTool,String skipUrl,String downloadUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("bookSource", bookSource);
        bundle.putBoolean("isStartBrowser", isStartBrowser);
        bundle.putBoolean("isStartTool", isStartTool);
        bundle.putString("skipUrl", skipUrl);
        bundle.putString("downloadUrl", downloadUrl);
        RxActivityTool.skipActivity(context, ActivityReadDialogActivity.class, bundle);

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
            bookId = getIntent().getExtras().getString("bookId");
            bookSource = getIntent().getExtras().getString("bookSource");
            isStartBrowser = getIntent().getExtras().getBoolean("isStartBrowser");
            isStartTool = getIntent().getExtras().getBoolean("isStartTool");
            skipUrl = getIntent().getExtras().getString("skipUrl");
            downloadUrl = getIntent().getExtras().getString("downloadUrl");
        }catch (Exception e){
        }

        if(isStartBrowser){
            tv_open_browser.setVisibility(View.VISIBLE);
        }else {
            tv_open_browser.setVisibility(View.GONE);
        }

        if(isStartTool){
            tv_skip_tool.setVisibility(View.VISIBLE);
        }else {
            tv_skip_tool.setVisibility(View.GONE);
        }

        tv_open_browser.setOnClickListener(
                v -> {
                    startBrowserActivity(skipUrl);
                    UrlReadActivity.bookHasRead(bookId);
                }
        );

        tv_skip_tool.setOnClickListener(
                v -> {
                    skipToZhuanMa(this,bookId,bookSource,skipUrl,downloadUrl);
                    UrlReadActivity.bookHasRead(bookId);
                }
        );

    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.tv_cancle)void tv_cancleClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }


    private void startBrowserActivity(String url) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);
        } catch (Exception e) {
            RxToast.custom("打开第三方浏览器失败", Constant.ToastType.TOAST_ERROR).show();
        }
    }


    //之后复制到连载神器中去使用。此处先写一个例子。
    private static void skipToZhuanMa(Context context,String bookId,String bookSource ,String bookUrl,String downloadUrl){
        String packageName = "com.zhuanma.reader";
        boolean hasZhuanMa = RxAppTool.checkPackInfo(context, packageName);
        if(hasZhuanMa){
            //唤起转码阅读，并且传入参数
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.putExtra("type", "1");
                intent.putExtra("bookId", bookId);
                intent.putExtra("bookUrl", bookUrl);
                intent.putExtra("bookSource", bookSource);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }else {
            try {
                Intent intent = new Intent();
                intent.setData(Uri.parse(downloadUrl));//跳转码阅读下载页
                intent.setAction(Intent.ACTION_VIEW);
                context.startActivity(intent);
            } catch (Exception e) {
                RxToast.custom("打开第三方浏览器失败", Constant.ToastType.TOAST_ERROR).show();
            }
        }
    }

}
