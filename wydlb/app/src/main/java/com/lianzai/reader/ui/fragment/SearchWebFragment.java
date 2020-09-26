package com.lianzai.reader.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.WebSearchBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.AndroidInterface;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLogTool;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 搜索--网页
 */
public class SearchWebFragment extends BaseFragment  {

    @Bind(R.id.parent_view)
    LinearLayout parent_view;

    AgentWeb mAgentWeb;

    ActivitySearch2 activitySearch2;

    String openUrl = "https://m.sm.cn/";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitySearch2 =(ActivitySearch2)activity;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(parent_view, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator(getResources().getColor(R.color.color_black_333333),2)// 使用默认进度条
                .setWebChromeClient(new WebChromeClient())
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go(openUrl);

        mAgentWeb.getAgentWebSettings().getWebSettings().setAllowFileAccess(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setJavaScriptEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setAppCacheEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDomStorageEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDatabaseEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath());
        String ua = mAgentWeb.getAgentWebSettings().getWebSettings().getUserAgentString();
        mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString(ua + "lianzai");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAgentWeb.getAgentWebSettings().getWebSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mAgentWeb.clearWebCache();
        AgentWebConfig.debug();
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWeb,activitySearch2));

    }

    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();//默认同意
        }

        @TargetApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl()+"");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            if (!url.equals(openUrl) && url.startsWith("http")){//跳转页面,只有点击是链接的时候才跳转
                mAgentWeb.getUrlLoader().loadUrl(openUrl);
                ActivityWebView.startActivity(activitySearch2,url,true);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            RxLogTool.e( "mUrl:" + url + " onPageStarted  target:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String mUrl) {
            super.onPageFinished(view, mUrl);
            RxLogTool.e("onPageFinished..url:"+mUrl);
        }
    };

    @Override
    public void onDestroy() {
        if (null!=mAgentWeb){
            mAgentWeb.clearWebCache();
            RxLogTool.e("mAgentWeb clearWebCache.....");
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
    @Override
    public void onPause() {
        if (null!=mAgentWeb)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        if (null!=mAgentWeb)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }


    @Override
    public void fetchData() {


    }

    public void searchWeb(String keyword) {
        searchWebRequest(keyword);
    }

    private void searchWebRequest(String keyword){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/search/bookSearch/" + keyword, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    WebSearchBean wbSearchBean = GsonUtil.getBean(response, WebSearchBean.class);
                    if (wbSearchBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        openUrl = wbSearchBean.getData();
                        mAgentWeb.getUrlLoader().loadUrl(openUrl);
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
