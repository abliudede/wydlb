package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogShare;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 关于我们
 */

public class ActivityAboutUs extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_kefu)
    TextView tv_kefu;

    //分享弹窗
    RxDialogWebShare rxDialogWebShare;

    @Bind(R.id.iv_about_us)
    ImageView iv_about_us;

    @Bind(R.id.tv_version)
    TextView tv_version;

    @Bind(R.id.tv_kefu_value)
    TextView tv_kefu_value;

    private WbShareHandler shareHandler;

    public static  final String ABOUT_US_LOGO_URL=Constant.UPAI_HTTP_HEADER+"/logo/icon_about_us_new.png";

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("关于我们");
        //书单分享样式
        rxDialogWebShare = new RxDialogWebShare(this, R.style.BottomDialogStyle);
        rxDialogWebShare.getTv_share_ftf().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_refresh().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);

        RxImageTool.loadNormalImage(this, ABOUT_US_LOGO_URL,iv_about_us);
        if(Constant.appMode == Constant.AppMode.RELEASE) {
            tv_version.setText("版本号：" + RxAppTool.getAppVersionName(this));
        }else {
            tv_version.setText("版本号：" + RxAppTool.getAppVersionName(this) + "beta");
        }

        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);
    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @OnClick(R.id.rl_kefu)void copyKefuClick(){
        RxToast.custom("客服微信复制成功").show();
        RxClipboardTool.copyText(this,tv_kefu_value.getText().toString());
    }

    @OnClick(R.id.tv_share)void shareClick(){
        showShareBarDialog();
    }

    /**
     * 分享bar详情框
     */
    private void showShareBarDialog() {
        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    getCommonShareUrl(1);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    getCommonShareUrl(3);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    getCommonShareUrl(2);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    getCommonShareUrl(5);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    getCommonShareUrl(6);
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.show();
    }

    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int shareMode){

        Map<String,String> map=new HashMap<>();
        map.put("code", "1007");
        map.put("shareMode", String.valueOf(shareMode));

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/common/share/getCommonShareUrl" ,map, new CallBackUtil.CallBackString() {
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
                    GetCommonShareUrlBean baseBean= GsonUtil.getBean(response,GetCommonShareUrlBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                        switch (shareMode){
                            case 1:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file1 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file1, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, true);
                                        }
                                    }.start();
                                }
                                break;
                            case 2:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file2 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file2, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, false);
                                        }
                                    }.start();
                                }
                             break;
                            case 3:
                                RxShareUtils.QQShareUrl(ActivityAboutUs.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityAboutUs.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityAboutUs.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(ActivityAboutUs.this,baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
                                break;

                            default:
                                break;
                        }

                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    RxLogTool.e("requestUnConcernPlatform e:"+e.getMessage());
                }
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
//            RxToast.custom("分享取消").show();
        }

        @Override
        public void onComplete(Object response) {
//            RxToast.custom("分享成功").show();
        }

        @Override
        public void onError(UiError e) {
//            RxToast.custom("分享失败").show();
        }
    };

}
