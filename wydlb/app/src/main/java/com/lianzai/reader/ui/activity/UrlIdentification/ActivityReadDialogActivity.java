package com.lianzai.reader.ui.activity.UrlIdentification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

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
