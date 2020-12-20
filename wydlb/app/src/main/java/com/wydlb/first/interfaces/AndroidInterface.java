package com.wydlb.first.interfaces;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.just.agentweb.AgentWeb;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.ShowShareBean;
import com.wydlb.first.ui.activity.ActivityImagesPreview;
import com.wydlb.first.ui.activity.ActivityLoginNew;
import com.wydlb.first.ui.activity.ActivityWebView;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxClipboardTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxFileTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogImageOption;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by lrz on 2017/12/11.
 */

public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Activity activity;

    RxDialogImageOption rxDialogImageOption;

    public AndroidInterface(AgentWeb agent, Activity mActivity) {
        this.agent = agent;
        this.activity = mActivity;
        rxDialogImageOption=new RxDialogImageOption(mActivity, R.style.BottomDialogStyle);
    }

    @JavascriptInterface
    public void backClick() {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        });

    }

    @JavascriptInterface
    public void tokenInvalid() {
        try {
            //token已失效
            RxEventBusTool.sendEvents(Constant.EventTag.TOKEN_FAILURE);
        }catch (Exception e){
            RxLogTool.e("tokenInvalid:"+e.getMessage());
        }
    }

    @JavascriptInterface
    public String getToken() {
        try{
            return RxLoginTool.getLoginAccountToken().getData().getToken();
        }catch (Exception e){
        }
        return "";
    }

    @JavascriptInterface
    public String getUid() {
        return String.valueOf(RxLoginTool.getLoginAccountToken().getData().getId());
    }


    //弹出分享框
    @JavascriptInterface
    public void showShare() {
        RxEventBusTool .sendEvents(Constant.EventTag.SHOW_SHARE);
//        try{
//            Uri uri = Uri.parse("market://details?id="+activity.getPackageName());
//            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//        }catch(Exception e){
//            RxToast.custom("您的手机没有安装Android应用市场").show();
//            e.printStackTrace();
//        }
    }

    //弹出分享框,众筹专用code  1021  objId： bookid
    @JavascriptInterface
    public void showShare(String code,String objId,String ext) {
        ShowShareBean sb = new ShowShareBean();
        sb.setCode(code);
        sb.setObjId(objId);
        sb.setExt(ext);

        String json = GsonUtil.toJsonString(sb);
        RxEventBusTool .sendEvents(json,Constant.EventTag.SHOW_SHARE_H5);
    }

    //跳应用市场
    @JavascriptInterface
    public void skipToMarket() {
        try{
            Uri uri = Uri.parse("market://details?id="+activity.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }catch(Exception e){
            RxToast.custom("您的手机没有安装Android应用市场").show();
            e.printStackTrace();
        }
    }

    //弹出图片放大和保存控件
    @JavascriptInterface
    public void clickPictureForAndroid(String imgs,int position) {
        try{
            String[] temp = imgs.split(",");
            ArrayList<String> list = new ArrayList<String>();
            for(String item :temp){
                list.add(item);
            }
            ActivityImagesPreview.startActivity(activity, list,position);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @JavascriptInterface
    public void saveQrCodePicture(String picUrl) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                rxDialogImageOption.show();
                rxDialogImageOption.getTv_save_pic().setOnClickListener(
                        v->{
                            String dirPath= RxFileTool.getRootPath()+ Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
                            RxFileTool.createOrExistsDir(dirPath);

                            OKHttpUtil.okHttpDownloadFile(picUrl, new CallBackUtil.CallBackFile(dirPath,System.currentTimeMillis()+".jpg") {
                                @Override
                                public void onFailure(Call call, Exception e) {
                                    RxToast.custom("图片保存失败").show();
                                }

                                @Override
                                public void onResponse(File response) {
                                    //通知相册更新
                                    RxImageTool.savePicToMedia(activity,response);
                                    RxToast.custom("图片保存成功").show();
                                }
                            });
                            rxDialogImageOption.dismiss();
                        }
                );
            }
        });
    }

    @JavascriptInterface
    public String getPlatform() {
        return  "1";
    }


    /*type:1书籍阅读界面
    * 2书单详情页
    * 3个人主页
    * 4圈子主页5,跳转到首页6，登录页跳转7，进入群聊8，跳转到书架
    * 9跳转到搜索页面
    * 10共享版权成员列表页面
    * 11自动投票页面
    * 12充值页面
    * */
    @JavascriptInterface
    public void skipPage(int type,String id) {
        switch (type) {

            case 6:
                ActivityLoginNew.startActivity(activity);
                break;

            case 7:
                break;

        }
    }



    @JavascriptInterface
    public void startBrowserActivity(String url){
        try{
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            activity.startActivity(intent);
        }catch (Exception e){
            RxToast.custom("打开第三方浏览器失败",Constant.ToastType.TOAST_ERROR).show();
        }
    }

    @JavascriptInterface
    public void goVoteOrExchange(String url){
        try{
//            activity.finish();
            ActivityWebView.startActivity(activity, url, 1);
        }catch (Exception e){
        }
    }

    @JavascriptInterface
    public void goCopyright(String url){
        try{
//            activity.finish();
            ActivityWebView.startActivity(activity, url);
        }catch (Exception e){
        }
    }

    @JavascriptInterface
    public String getAppVersionName(){
        try{
            return RxAppTool.getAppVersionName(activity);
        }catch (Exception e){
        }
        return "";
    }

    @JavascriptInterface
    public void skipToWechat(String str){
        try{
            //复制链接
            RxClipboardTool.copyText(activity,str);
            getWechatApi();
        }catch (Exception e){
        }
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            RxToast.custom("检查到您手机没有安装微信，请安装后使用该功能").show();
        }
    }

}
