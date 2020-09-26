package com.lianzai.reader.ui.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
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
import com.lianzai.reader.ui.activity.ActivityBookShare;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapCircle;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogCheckBox;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/24.
 * 圈子弹出框
 */

public class ActivityCircleSetting extends BaseActivityForTranslucent {

    //书籍封面
    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    //标题
    @Bind(R.id.tv_book_name)
    TextView tv_book_name;
    //加入天数
    @Bind(R.id.tv_book_days)
    TextView tv_book_days;
    //阅读时长
    @Bind(R.id.tv_book_read)
    TextView tv_book_read;

    @Bind(R.id.iv_user_logo)
    CircleImageView iv_user_logo;
    @Bind(R.id.iv_head_bg)
    ImageView iv_head_bg;



    @Bind(R.id.sb_top_off)
    CheckBox sb_top_off;

    @Bind(R.id.sb_msg_off)
    CheckBox sb_msg_off;

    //圈子成员
    @Bind(R.id.rl_person)
    RelativeLayout rl_person;
    @Bind(R.id.tv_invite)
    TextView tv_invite;
    @Bind(R.id.tv_person_count)
    TextView tv_person_count;

    //版权认证
    @Bind(R.id.tv_copyright)
    TextView tv_copyright;



    //审核举报内容
    @Bind(R.id.tv_shenhe_report)
    TextView tv_shenhe_report;

    //举报
    @Bind(R.id.tv_report)
    TextView tv_report;

    //申请管理员
    @Bind(R.id.tv_shenqing)
    TextView tv_shenqing;
    //什么是圈子？
    @Bind(R.id.tv_whats_circle)
    TextView tv_whats_circle;

    //退出圈子
    @Bind(R.id.tv_exit_circle)
    TextView tv_exit_circle;

    private String circleId;
    private String accid;
    private String cover;
    private String bookname;
    private String days;
    private String minute;
//    private String athourHead;
    private int platformType;
    private boolean notice = false;
    private int userType = Constant.Role.FANS_ROLE;
    private int personCount;
    private int mayApplyRole;
    private boolean isSigned;
    private String signUrl;

    //提示框，包含chkeckbox
    RxDialogCheckBox rxDialogSureCancel;

    //分享弹窗
    RxDialogWebShare rxDialogWebShare;

    private WbShareHandler shareHandler;


    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    // /* 1000超级管理员,2000代圈主 ,3000管理员 4000关注的成员*/
    //    private Integer userType;
    public static void startActivity(Activity context, String circleId, String accid, String cover,String bookname,String days,String minute
    ,int platformType,int userType,int personCount,int mayApplyRole ,boolean isSigned , String signUrl){
        Bundle intent=new Bundle();
        intent.putString("circleId",circleId);
        intent.putString("accid",accid);
        intent.putString("cover",cover);
        intent.putString("bookname",bookname);
        intent.putString("days",days);
        intent.putString("minute",minute);
//        intent.putString("athourHead",athourHead);
        intent.putInt("platformType",platformType);
        intent.putInt("userType",userType);
        intent.putInt("personCount",personCount);
        intent.putInt("mayApplyRole",mayApplyRole);
        intent.putBoolean("isSigned",isSigned);
        intent.putString("signUrl",signUrl);
        RxActivityTool.skipActivity(context,ActivityCircleSetting.class,intent);
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
//        hideSystemBar();
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        try{
            Intent intent = getIntent();
            circleId= intent.getStringExtra("circleId");
            accid= intent.getStringExtra("accid");
            cover= intent.getStringExtra("cover");
            bookname= intent.getStringExtra("bookname");
            days= intent.getStringExtra("days");
            minute= intent.getStringExtra("minute");
//            athourHead= intent.getStringExtra("athourHead");
            platformType= intent.getIntExtra("platformType",0);
            userType= intent.getIntExtra("userType",0);
            personCount= intent.getIntExtra("personCount",0);
            mayApplyRole= intent.getIntExtra("mayApplyRole",0);
            isSigned =  intent.getBooleanExtra("isSigned",false);
            signUrl = intent.getStringExtra("signUrl");
        }catch (Exception e){

        }

        RxImageTool.loadNormalImage(ActivityCircleSetting.this,cover,iv_book_cover);
        tv_book_name.setText(bookname);
        tv_book_read.setText("阅读本书 "+ minute + "分钟");
        tv_book_days.setText("已加入圈子 "+ days + "天");
        tv_person_count.setText(String.valueOf(personCount) + "人");

        if (RecentContactsFragment.isTop){
            sb_top_off.setChecked(true);
        }else{
            sb_top_off.setChecked(false);
        }

        //个人头像
        if (RxLoginTool.isLogin()) {
            AccountDetailBean accountDetailBean = RxTool.getAccountBean();
            if(null != accountDetailBean){
                RxImageTool.loadLogoImage(ActivityCircleSetting.this, accountDetailBean.getData().getAvatar(),iv_user_logo);
            }
        }
        //个人头像背景
        if(userType >= Constant.Role.ADMIN_ROLE && userType < Constant.Role.MANAGE2_ROLE){
            iv_user_logo.setBorderWidth(0);
            iv_head_bg.setVisibility(View.VISIBLE);
            iv_head_bg.setImageResource(R.mipmap.circle_gold);
        }else if(userType >= Constant.Role.MANAGE2_ROLE && userType < Constant.Role.FANS_ROLE){
            iv_user_logo.setBorderWidth(0);
            iv_head_bg.setVisibility(View.VISIBLE);
            iv_head_bg.setImageResource(R.mipmap.circle_yin);
        }else {
            iv_head_bg.setVisibility(View.INVISIBLE);
            iv_user_logo.setBorderWidth(6);
        }



        //是否开启消息免打扰
        notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(accid);
        if(notice){
            sb_msg_off.setChecked(true);
        }else {
            sb_msg_off.setChecked(false);
        }

        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_WHAT_IS_CIRCLE,true)){
            //里面不显示
            tv_whats_circle.setVisibility(View.GONE);
        }else {
            //里面显示
            tv_whats_circle.setVisibility(View.VISIBLE);
        }

        if(platformType==Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || platformType > Constant.UserIdentity.CAN_ATTENTION_NUMBER){
            tv_exit_circle.setVisibility(View.GONE);
            tv_book_read.setVisibility(View.GONE);
            tv_book_days.setVisibility(View.GONE);
            rl_person.setVisibility(View.GONE);
        }else {
            tv_exit_circle.setVisibility(View.VISIBLE);
            tv_book_read.setVisibility(View.VISIBLE);
            tv_book_days.setVisibility(View.VISIBLE);
            rl_person.setVisibility(View.VISIBLE);
        }
        if(userType == Constant.Role.ADMIN_ROLE){
            tv_exit_circle.setVisibility(View.GONE);
        }
        if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.ADMIN_ROLE){
            tv_invite.setText("管理圈子成员");
            tv_shenhe_report.setVisibility(View.VISIBLE);
            tv_report.setVisibility(View.GONE);
        }else {
            tv_invite.setText("圈子成员");
            tv_shenhe_report.setVisibility(View.GONE);
            tv_report.setVisibility(View.VISIBLE);
        }

        if(mayApplyRole == Constant.Role.MANAGE_ROLE){
            tv_shenqing.setVisibility(View.VISIBLE);
            tv_shenqing.setText("申请代圈主");
        }else if(mayApplyRole == Constant.Role.MANAGE2_ROLE){
            tv_shenqing.setVisibility(View.VISIBLE);
            tv_shenqing.setText("申请管理员");
        }else {
            tv_shenqing.setVisibility(View.GONE);
        }


        if(isSigned){
            tv_copyright.setVisibility(View.VISIBLE);
            tv_copyright.setOnClickListener(
                    v -> {
                        ActivityWebView.startActivity(ActivityCircleSetting.this,signUrl);
                    }
            );
        }else {
            tv_copyright.setVisibility(View.GONE);
        }


        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        //圈子分享样式
        rxDialogWebShare = new RxDialogWebShare(this, R.style.BottomDialogStyle);
        rxDialogWebShare.getTv_share_refresh().setText("连载口令");
        rxDialogWebShare.getTv_share_refresh().setCompoundDrawables(null,RxImageTool.getDrawable(R.mipmap.icon_lianzaikouling),null,null);
        rxDialogWebShare.getTv_share_ftf().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_copy().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);

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
        rxDialogWebShare.getTv_share_refresh().setOnClickListener(
                v -> {
                    //连载口令
                    ActivityShareBitmapCircle.startActivity(ActivityCircleSetting.this,circleId);
                    rxDialogWebShare.dismiss();
                }
        );

    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }

    @OnClick(R.id.iv_cancel)void iv_cancelClick(){
        //直接关闭
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnCheckedChanged(R.id.sb_top_off)void sb_topChanged(boolean isChecked){
        if (isChecked){
            RecentContactsFragment.isTop=true;
            RxEventBusTool.sendEvents(String.valueOf(circleId),Constant.EventTag.CONTACT_TOP_TAG);
        }else{
            RecentContactsFragment.isTop=false;
            RxEventBusTool.sendEvents(String.valueOf(circleId),Constant.EventTag.CONTACT_CALCLE_TOP_TAG);
        }
    }

    @OnCheckedChanged(R.id.sb_msg_off)void sb_msg_offChanged(boolean isChecked){
        NIMClient.getService(FriendService.class).setMessageNotify(accid, isChecked).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                RxLogTool.e("RequestCallback---");
//                RxToast.custom("操作成功").show();
                //会话列表刷新
                RxEventBusTool.sendEvents(Constant.EventTag.MESSAGE_REFRESH);
            }
            @Override
            public void onFailed(int i) {
                RxToast.custom("操作失败").show();
            }
            @Override
            public void onException(Throwable throwable) {
            }
        });
    }


    @OnClick(R.id.rl_person)void rl_personClick(){
        if(RxLoginTool.isLogin()){
            ActivityCirclePersonList.startActivity(ActivityCircleSetting.this,circleId,userType);
        }else {
            ActivityLoginNew.startActivity(ActivityCircleSetting.this);
        }
    }

    @OnClick(R.id.tv_shenhe_report)void tv_shenhe_reportClick(){
        if(RxLoginTool.isLogin()){
        //审核举报内容
        ActivityCircleReportAuditList.startActivity(ActivityCircleSetting.this,circleId,userType);
        }else {
            ActivityLoginNew.startActivity(ActivityCircleSetting.this);
        }
    }

    @OnClick(R.id.tv_report)void tv_reportClick(){
        if(RxLoginTool.isLogin()){
            //举报H5
            ActivityWebView.startActivity(ActivityCircleSetting.this,Constant.H5_BASE_URL+"/author/#/report?source=1&targetId="+circleId,1);
        }else {
            ActivityLoginNew.startActivity(ActivityCircleSetting.this);
        }
    }

    @OnClick(R.id.tv_shenqing)void tv_shenqingClick(){
        if(RxLoginTool.isLogin()){
        //申请管理员H5
        if(mayApplyRole == Constant.Role.MANAGE_ROLE){
            ActivityWebView.startActivity(ActivityCircleSetting.this,Constant.H5_BASE_URL+"/circle-role/#/detail/surrogate?circleId="+circleId,1);
        }else if(mayApplyRole == Constant.Role.MANAGE2_ROLE){
            ActivityWebView.startActivity(ActivityCircleSetting.this,Constant.H5_BASE_URL+"/circle-role/#/detail/administrators?circleId="+circleId,1);
        }
        }else {
            ActivityLoginNew.startActivity(ActivityCircleSetting.this);
        }
    }



    @OnClick(R.id.tv_whats_circle)
    void tv_whats_circleClick(){
        if(Constant.appMode == Constant.AppMode.RELEASE){
            ActivityWebView.startActivity(ActivityCircleSetting.this,"https://fx.bendixing.net/taskCenter/#/circle");
        }else {
            ActivityWebView.startActivity(ActivityCircleSetting.this,"https://beta.fx.bendixing.net/taskCenter/#/circle");
        }
    }


    @OnClick(R.id.tv_share)void tv_shareClick(){
        //分享弹窗
        rxDialogWebShare.show();
    }


    @OnClick(R.id.tv_exit_circle)void tv_exit_circleClick(){
        //退出圈子
        exit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //退出圈子流程
    private void exit(){
        if(null == rxDialogSureCancel){
            rxDialogSureCancel=new RxDialogCheckBox(this,R.style.OptionDialogStyle);
        }
        rxDialogSureCancel.getTitleView().setText("不再考虑下吗？");
        if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.ADMIN_ROLE){
            rxDialogSureCancel.getContentView().setText("退出该圈子，您的圈子角色也会被同时被取消，确认退出吗？");
        }else {
            rxDialogSureCancel.getContentView().setText("退出圈子，你将失去和书友互动的机会哦～");
        }

        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
        rxDialogSureCancel.getSureView().setText("确定退出");
        rxDialogSureCancel.getCancelView().setText("取消");
//        if(platformType == Constant.UserIdentity.BOOK_PUBLIC_NUMBER || platformType == Constant.UserIdentity.BOOK_OUTSIED_NUMBER){
//            rxDialogSureCancel.getCheckbox_ly().setVisibility(View.VISIBLE);
//            rxDialogSureCancel.getCb_nomore_tip().setChecked(false);
//            rxDialogSureCancel.getTv_checkbox().setText("同时将书籍移出书架");
//        }else {
            rxDialogSureCancel.getCheckbox_ly().setVisibility(View.GONE);
//        }

        //红色按钮
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
        rxDialogSureCancel.getSureView().setOnClickListener(
                v -> {
                    exitCircle(rxDialogSureCancel.getCb_nomore_tip().isChecked());
                    rxDialogSureCancel.dismiss();
                }
        );
        rxDialogSureCancel.show();
    }

    /**
     * 退出圈子
     *
     */
    public void exitCircle(boolean check) {
        showDialog();
        Map<String, String> map = new HashMap<>();
        map.put("isUnConcern", String.valueOf(check));
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+circleId+"/exist",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
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
                        RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_CIRCLE_LIST);
                        RxEventBusTool.sendEvents(String.valueOf(circleId),Constant.EventTag.REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG);
                        if(check){
                            //告诉阅读页面重新请求书籍信息
                            RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_BOOK_INFO);
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_REQUEST);
                        }
                        finish();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int shareMode){

        Map<String,String> map=new HashMap<>();
        map.put("code", "1022");
        map.put("shareMode", String.valueOf(shareMode));
        map.put("objId", circleId);

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
                                RxShareUtils.QQShareUrl(ActivityCircleSetting.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityCircleSetting.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityCircleSetting.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(ActivityCircleSetting.this,baseBean.getData().getOneUrlVal());
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
