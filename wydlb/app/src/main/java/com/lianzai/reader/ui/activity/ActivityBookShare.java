package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
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
 * Created by lrz on 2018/07/24.
 * 连载号弹出页面
 */

public class ActivityBookShare extends BaseActivityForTranslucent {

    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    @Bind(R.id.tv_book_name)
    TextView tv_book_name;

    @Bind(R.id.tv_book_author)
    TextView tv_book_author;

    @Bind(R.id.tv_book_des)
    TextView tv_book_des;

    @Bind(R.id.tv_share_follow)
    TextView tv_share_follow;

    @Bind(R.id.tv_share_report)
    TextView tv_share_report;

    @Bind(R.id.sv_share_top)
    HorizontalScrollView sv_share_top;



    private String img;
    private String bookname;
    private String athour;
    private String bookdes;
    private String lianzaihaoid;
    private int platformType;

    private WbShareHandler shareHandler;
    private boolean showCancle = false;
    private boolean showReport = true;

    RxDialogSureCancelNew rxDialogSureCancel;

    private String bqtKey;
    private String uid;
    private AccountDetailBean accountDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_share;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    public static void startActivity(Activity context, String img,String bookname,String athour,String bookdes,String lianzaihaoid
    ,int platformType,boolean showCancle){
        Bundle intent=new Bundle();
        intent.putString("img",img);
        intent.putString("bookname",bookname);
        intent.putString("athour",athour);
        intent.putString("bookdes",bookdes);
        intent.putString("lianzaihaoid",lianzaihaoid);
        intent.putInt("platformType",platformType);
        intent.putBoolean("showCancle",showCancle);
//        intent.putExtra("showReport",showReport);
        RxActivityTool.skipActivity(context,ActivityBookShare.class,intent);
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
//        hideSystemBar();
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        img= intent.getStringExtra("img");
        bookname= intent.getStringExtra("bookname");
        athour= intent.getStringExtra("athour");
        bookdes= intent.getStringExtra("bookdes");
        lianzaihaoid= intent.getStringExtra("lianzaihaoid");
        platformType= intent.getIntExtra("platformType",0);
        showCancle= intent.getBooleanExtra("showCancle",false);
//        showReport= intent.getBooleanExtra("showReport",false);

        accountDetailBean = RxTool.getAccountBean();
        if(null != accountDetailBean) {
            uid = accountDetailBean.getData().getUid();
            bqtKey = uid + Constant.BQT_KEY;
        }

        if(platformType == Constant.UserIdentity.CAN_ATTENTION_NUMBER  ){
            sv_share_top.setVisibility(View.GONE);
        }else {
            sv_share_top.setVisibility(View.VISIBLE);
        }

        RxImageTool.loadImage(ActivityBookShare.this,img,iv_book_cover);
        tv_book_name.setText(bookname);
        tv_book_author.setText(athour);
        tv_book_des.setText(bookdes);

        if(showCancle){
            tv_share_follow.setVisibility(View.VISIBLE);
        }else{
            tv_share_follow.setVisibility(View.GONE);
        }

        if(showReport){
            tv_share_report.setVisibility(View.VISIBLE);
        }else{
            tv_share_report.setVisibility(View.GONE);
        }

        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        //红色按钮
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
    }

    private void hideSystemBar() {
        //隐藏
        SystemBarUtils.readStatusBar1(this);
    }

    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }

//    @OnClick(R.id.top_view)void top_viewClick(){
////        if(RxLoginTool.isLogin()) {
////            //登录后可以进连载号
////            PublicNumberDetailActivity.startActivity(ActivityBookShare.this,Integer.parseInt(lianzaihaoid));
////        }
////    }

    @OnClick(R.id.tv_cancel)void tv_cancelClick(){
        //直接关闭
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);

    }


    //功能按钮
    @OnClick(R.id.tv_share_wx)void tv_share_wxClick(){
        getCommonShareUrl(1);
    }

    @OnClick(R.id.tv_share_qq)void tv_share_qqClick(){
        getCommonShareUrl(3);
    }


    @OnClick(R.id.tv_share_timeline)void tv_share_timelineClick(){
        getCommonShareUrl(2);
    }


    @OnClick(R.id.tv_share_weibo)void tv_share_weiboClick(){
        getCommonShareUrl(5);
    }


    @OnClick(R.id.tv_share_follow)void tv_share_followClick(){
        //取消关注
        rxDialogSureCancel.getSureView().setText("确定");
        rxDialogSureCancel.getCancelView().setText("取消");
        rxDialogSureCancel.setTitle("提示");
        rxDialogSureCancel.setContent(getResources().getString(R.string.dialog_cancel_attention_platform));
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitCircle();
                rxDialogSureCancel.dismiss();
            }
        });
        rxDialogSureCancel.show();
    }

    //举报页
    @OnClick(R.id.tv_share_report)void tv_share_reportClick(){
        ActivityWebView.startActivity(ActivityBookShare.this,Constant.H5_BASE_URL+"/author/#/report?source=1&targetId="+lianzaihaoid,1);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 退出圈子
     *
     */
    public void exitCircle() {
        showDialog();
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+ lianzaihaoid +"/exist", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    dismissDialog();
                }catch (Exception ee){

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxEventBusTool.sendEvents(String.valueOf(lianzaihaoid),Constant.EventTag.REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG);
                        RxEventBusTool.sendEvents(Constant.EventTag.CLOSE_CLOSE_CHAT_BY_ACCID_TAG);
                        finish();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加入圈子
     *
     */
    public void joinCircle() {
        showDialog();
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+lianzaihaoid+"/join", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }




    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int shareMode){

        Map<String,String> map=new HashMap<>();
        map.put("code", "1002");
        map.put("shareMode", String.valueOf(shareMode));
        map.put("objId", lianzaihaoid);

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
                                RxShareUtils.QQShareUrl(ActivityBookShare.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityBookShare.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityBookShare.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                                default:
                                    break;
                        }

                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    RxLogTool.e("getCommonShareUrl e:"+e.getMessage());
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
