package com.lianzai.reader.ui.activity.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.GetPlatformInfoResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityBookShare;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.ExpandableTextView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 连载号详情
 */
public class PublicNumberDetailActivity extends BaseActivity {

    @Bind(R.id.iv_more)
    ImageView iv_more;

    @Bind(R.id.iv_blur_img)
    ImageView iv_blur_img;

    Bitmap blurBitmap;//头部高斯模糊背景


    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    @Bind(R.id.tv_book_name)
            TextView tv_book_name;

//    @Bind(R.id.rl_catalog)
//    RelativeLayout rl_catalog;

    @Bind(R.id.tv_is_open_chat_room)
    TextView tv_is_open_chat_room;

    @Bind(R.id.rl_chat_team)
    RelativeLayout rl_chat_team;

    @Bind(R.id.tv_is_open_chat_team)
    TextView tv_is_open_chat_team;

    @Bind(R.id.tv_description)
    ExpandableTextView tv_description;


    @Bind(R.id.bottom_view)
    LinearLayout bottom_view;

    @Bind(R.id.tv_attention)
    TextView tv_attention;

    @Bind(R.id.tv_open_book)
    TextView tv_open_book;

    @Bind(R.id.tv_book_author)
    TextView tv_book_author;

    @Bind(R.id.tv_book_status_and_category)
    TextView tv_book_status_and_category;


//    @Bind(R.id.tv_book_directory_update_time)
//    TextView tv_book_directory_update_time;


    @Bind(R.id.rl_book_last_chapter)
    RelativeLayout rl_book_last_chapter;

    @Bind(R.id.tv_book_last_chapter)
    TextView tv_book_last_chapter;

    @Bind(R.id.sb_top)
    CheckBox sb_top;

    @Bind(R.id.sb_msg_off)
    CheckBox sb_msg_off;

//    @Bind(R.id.ll_normal_public_number)
//    LinearLayout ll_normal_public_number;

    Drawable drawableMining;

//    RxDialogPublicNumberOptions rxDialogPublicNumberOptions;
    //替换成两个标志位用来在下个页面决定按钮显示情况
    private boolean showCancle = false;

    GetPlatformInfoResponse.DataBean dataBean;

    int objectId;

    @Bind(R.id.ll_top_set)
    LinearLayout ll_top_set;

    @Bind(R.id.rl_chat_room)
            RelativeLayout rl_chat_room;

    private String bqtKey;
    private String uid;
    private AccountDetailBean accountDetailBean;

    public static void startActivity(Context context,int mObjectId) {
        Intent intent=new Intent(context, PublicNumberDetailActivity.class);
        intent.putExtra("objectId",mObjectId);
//        intent.addFlags(Intent.FLAG_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_public_number_detail;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
        requestPlatform();
    }

    private void processIntentData(Bundle outState,Intent intent){
        if (null!=outState){
            objectId=outState.getInt("objectId",0);
        }else{
            objectId=intent.getIntExtra("objectId",0);
        }

        RxLogTool.e("processIntentData objectId:"+objectId);

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //任务-沉浸式状态栏
        SystemBarUtils.fullScreen(this);

        accountDetailBean = RxTool.getAccountBean();
        if(null != accountDetailBean) {
            uid = accountDetailBean.getData().getUid();
            bqtKey = uid + Constant.BQT_KEY;
        }


        processIntentData(savedInstanceState,getIntent());

        iv_more.setOnClickListener(
                v->{
                    if (null==dataBean)return;
                    ActivityBookShare.startActivity(PublicNumberDetailActivity.this,dataBean.getPlatformCover()
                            ,dataBean.getPlatformName(),dataBean.getPenName(),dataBean.getPlatformIntroduce(),String.valueOf(objectId),dataBean.getPlatformType()
                    ,showCancle);
                }
        );

        int size= RxImageTool.dp2px(18);
        drawableMining= getResources().getDrawable(R.mipmap.icon_v2_mining);
        drawableMining.setBounds(0,0, size,size);

        if (RecentContactsFragment.isTop){
            sb_top.setChecked(true);
        }else{
            sb_top.setChecked(false);
        }


    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @OnCheckedChanged(R.id.sb_top)void sb_topChanged(boolean isChecked){
        if (isChecked){
            RecentContactsFragment.isTop=true;
            RxEventBusTool.sendEvents(String.valueOf(objectId),Constant.EventTag.CONTACT_TOP_TAG);
        }else{
            RecentContactsFragment.isTop=false;
            RxEventBusTool.sendEvents(String.valueOf(objectId),Constant.EventTag.CONTACT_CALCLE_TOP_TAG);
        }
    }

    @OnCheckedChanged(R.id.sb_msg_off)void sb_msg_offChanged(boolean isChecked){
        RxLogTool.e("checkState:"+isChecked);
        //false静音 true 需要提醒
        if(null == dataBean) return;

        NIMClient.getService(FriendService.class).setMessageNotify(dataBean.getAccid(), !isChecked).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                RxLogTool.e("RequestCallback---");
            }
            @Override
            public void onFailed(int i) {
            }
            @Override
            public void onException(Throwable throwable) {
            }
        });
    }


    @Override
    public void gc() {

    }

    private void refreshData(){
        if (null==dataBean)return;

        //是否开启消息免打扰
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(dataBean.getAccid());
        //消息需要消息提醒 false静音 true需要消息提醒
        sb_msg_off.setChecked(!notice);

        //加载头部通用视图
        int errorImgId=RxImageTool.getNoCoverImgByRandom();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.placeholder(errorImgId);
        requestOptions.error(errorImgId);
        requestOptions.skipMemoryCache(false);
        requestOptions.dontAnimate();
        Glide.with(this).load(dataBean.getPlatformCover()).apply(requestOptions).listener(new RequestListener<Drawable>() {
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

        tv_book_name.setText(dataBean.getPlatformName());
        tv_description.getmButton().setGravity(Gravity.LEFT);
        tv_description.setText("简介：" + dataBean.getPlatformIntroduce(), new SparseBooleanArray(),0);
        //加载头部通用视图完毕


        //官方号1
        if (dataBean.getPlatformType()==Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || dataBean.getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER){
            //官方号没有关注取关的概念。不管是否关注，都显示此视图。
            ll_top_set.setVisibility(View.VISIBLE);
            tv_attention.setText("进入连载号");
            tv_attention.setOnClickListener(
                    v->{
                        if(null == dataBean){
                            return;
                        }
                        if (TextUtils.isEmpty(dataBean.getAccid())){
                            RxToast.custom("该连载号不可用",Constant.ToastType.TOAST_ERROR).show();
                            return;
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(null == dataBean){
                                    return;
                                }
                                //关注之后会话不能立即获得，所以延迟一会再打开连载号详情
                                NimUIKit.startP2PSession(PublicNumberDetailActivity.this, dataBean.getAccid());
                            }
                        },300);
                    }
            );
            showCancle = false;

            //底部视图
            bottom_view.setVisibility(View.VISIBLE);
            tv_attention.setVisibility(View.VISIBLE);
            tv_open_book.setVisibility(View.GONE);
            rl_book_last_chapter.setVisibility(View.GONE);

            iv_more.setVisibility(View.INVISIBLE);
            tv_book_author.setVisibility(View.VISIBLE);
            tv_book_status_and_category.setVisibility(View.GONE);

            tv_book_author.setText("连载官方");

        }else if( dataBean.getPlatformType() == Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
            checkIsConcern();
            //其它可关注取关的官方号
            bottom_view.setVisibility(View.VISIBLE);
            tv_attention.setVisibility(View.VISIBLE);
            tv_open_book.setVisibility(View.GONE);
            rl_book_last_chapter.setVisibility(View.GONE);

            iv_more.setVisibility(View.VISIBLE);
            tv_book_author.setVisibility(View.VISIBLE);
            tv_book_status_and_category.setVisibility(View.GONE);

            tv_book_author.setText("连载官方");

        }else {
            checkIsConcern();
            //书籍类型
            bottom_view.setVisibility(View.VISIBLE);
            tv_attention.setVisibility(View.VISIBLE);
            tv_open_book.setVisibility(View.VISIBLE);
            rl_book_last_chapter.setVisibility(View.VISIBLE);


            iv_more.setVisibility(View.VISIBLE);
            tv_book_author.setVisibility(View.VISIBLE);
            tv_book_status_and_category.setVisibility(View.VISIBLE);



            tv_book_author.setText(dataBean.getPenName() + " 著");
            tv_book_status_and_category.setText(dataBean.getTypeText()+" | "+dataBean.getCategoryName());
            tv_book_last_chapter.setText(dataBean.getLastestChapterTitle());
        }









        //聊天室模块是否开通
        if (dataBean.getRoomId()>0){
            rl_chat_room.setVisibility(View.VISIBLE);
            tv_is_open_chat_room.setText("以文会友");
        }else{
            rl_chat_room.setVisibility(View.GONE);
            tv_is_open_chat_room.setText("聊天室还未开通");
        }

        //群聊模块是否显示
        if(dataBean.isInTeam() && dataBean.getTeamId2() != 0){
            rl_chat_team.setVisibility(View.VISIBLE);
            tv_is_open_chat_team.setText("畅聊交友");
        }else if(!dataBean.isInTeam() && dataBean.getTeamId2() != 0){
            rl_chat_team.setVisibility(View.VISIBLE);
            tv_is_open_chat_team.setText("申请入群");
        }else {
            rl_chat_team.setVisibility(View.GONE);
        }

        //新增是否禁止阅读
        if(dataBean.isHidden()) {
            rl_book_last_chapter.setVisibility(View.GONE);
            tv_open_book.setVisibility(View.GONE);
        }
    }


    private void checkIsConcern(){
        if (dataBean.isIsConcern()){//已关注
            ll_top_set.setVisibility(View.VISIBLE);
            tv_attention.setText("进入连载号");
            tv_attention.setOnClickListener(
                    v->{
                        if(null == dataBean){
                            return;
                        }
                        if (TextUtils.isEmpty(dataBean.getAccid())){
                            RxToast.custom("该连载号不可用",Constant.ToastType.TOAST_ERROR).show();
                            return;
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(null == dataBean){
                                    return;
                                }
                                //关注之后会话不能立即获得，所以延迟一会再打开连载号详情
                                NimUIKit.startP2PSession(PublicNumberDetailActivity.this, dataBean.getAccid());
                            }
                        },300);
                    }
            );

            if (dataBean.getUserType()==Constant.Role.ADMIN_ROLE||dataBean.getUserType()==Constant.Role.MANAGE_ROLE){//超级管理员-本书作者  管理员
                showCancle = false;
            }else{
                showCancle = true;
            }
        }else{
            ll_top_set.setVisibility(View.GONE);
            tv_attention.setText("添加关注");
            tv_attention.setOnClickListener(
                    v->{
                        requestConcernPlatform();
                    }
            );
            showCancle = false;
        }
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float radius = 40;//模糊程度

        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

    /**
     * 数据请求，请求连载号详细信息
     */
    private void requestPlatform(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/" + objectId, new CallBackUtil.CallBackString() {
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
                    RxLogTool.e("requestPlatform:"+response);
                    GetPlatformInfoResponse getPlatformInfoResponse= GsonUtil.getBean(response,GetPlatformInfoResponse.class);
                    if (getPlatformInfoResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        dataBean=getPlatformInfoResponse.getData();
                        refreshData();
                    }else{
                        RxToast.custom(getPlatformInfoResponse.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    RxLogTool.e("requestPlatform e:"+e.getMessage());
                }


            }
        });
    }

    /**
     * 关注
     */
    private void requestConcernPlatform(){
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+objectId+"/join", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
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
                        requestPlatform();
                        //去掉自动打开会话页的功能
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //关注之后会话不能立即获得，所以延迟一会再打开,连载号详情
//                                NimUIKit.startP2PSession(PublicNumberDetailActivity.this, baseBean.getData());
//                            }
//                        },300);
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //历史消息
    @OnClick(R.id.tv_history)void historyClick(){
        if (null==dataBean)return;

        //除了书要显示分类跟笔名，其他的都不要显示
        if (dataBean.getPlatformType()==Constant.UserIdentity.BOOK_OUTSIED_NUMBER||dataBean.getPlatformType()==Constant.UserIdentity.BOOK_PUBLIC_NUMBER){
            PublicNumberHistoryActivity.startActivity(this,String.valueOf(objectId),dataBean.getPenName(),dataBean.getTypeText()+" | "+dataBean.getCategoryName());
        }else{
            PublicNumberHistoryActivity.startActivity(this,String.valueOf(objectId),dataBean.getPenName(),null);
        }

    }




//    /**
//     * 分享详情框
//     */
//    private void showShareDialog(GetPlatformInfoResponse.DataBean bean){
//        if (null==bean)
//            return;
//        rxDialogShare.getTv_share_wx_circle().setOnClickListener(
//                v->{
//                    File file=new File(RxImageTool.getImgPathFromCache(bean.getPlatformCover(),getApplicationContext()));
//
//                    RxShareUtils.shareWebUrl(file,bean.getShareUrl(),bean.getPlatformName(),bean.getPlatformIntroduce(), BuglyApplicationLike.getsInstance().api,false);
//                    rxDialogShare.dismiss();
//                }
//        );
//        rxDialogShare.getTv_share_wx().setOnClickListener(
//                v->{
//                    File file=new File(RxImageTool.getImgPathFromCache(bean.getPlatformCover(),getApplicationContext()));
//                    RxShareUtils.shareWebUrl(file,bean.getShareUrl(),bean.getPlatformName(),bean.getPlatformIntroduce(), BuglyApplicationLike.getsInstance().api,true);
//                    rxDialogShare.dismiss();
//                }
//        );
//
//        rxDialogShare.getTv_share_copy().setOnClickListener(
//                v->{
//                    RxClipboardTool.copyText(this,bean.getShareUrl());
//                    RxToast.custom("复制成功",Constant.ToastType.TOAST_SUCCESS).show();
//                    rxDialogShare.dismiss();
//                }
//        );
//
//        rxDialogShare.show();
//    }

//    @OnClick(R.id.rl_catalog)void bookCatalogClick(){
//        if (dataBean==null)return;
//        BookCatalogActivity.startActivity(this,String.valueOf(dataBean.getBookId()),String.valueOf(dataBean.getId()),dataBean.getSource());
//    }

    //打开阅读
    @OnClick(R.id.tv_open_book)void openBookClick(){
        if (dataBean==null)return;
        SkipReadUtil.normalRead(this,String.valueOf(dataBean.getBookId()),"",false);
    }

    /**
     * 打开最新章节
     */
    @OnClick(R.id.tv_book_last_chapter)void openLastChapterClick(){
        if (dataBean==null)return;
        UrlReadActivity.startActivityOutsideRead(this,String.valueOf(dataBean.getBookId()),"",false,"",dataBean.getLastestChapterTitle(),0,false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentData(null,getIntent());

        requestPlatform();//刷新请求

        RxLogTool.e("PublicNumberDetailActivity onNewIntent----objectId:"+objectId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("objectId",objectId);
    }

//    /**
//     * 连载号二维码
//     */
//    @OnClick(R.id.rl_qrcode)void qrcodeClick(){
//        ChatNumberQrCodeActivity.startActivity(this);
//    }

    /**
     * 进入聊天室
     */
    @OnClick(R.id.rl_chat_room)void chatRoomClick(){
//        if (null==dataBean)return;
//        if (dataBean.getRoomId()>0)
//            ChatRoomActivity.startActivity(this,String.valueOf(dataBean.getRoomId()));
    }

    /**
     * 进入群聊
     */
    @OnClick(R.id.rl_chat_team)void rl_chat_teamClick(){
        if(null == dataBean){
            RxToast.custom("暂未获取到页面信息").show();
            return;
        }
        if(dataBean.isInTeam()) {
            TeamMessageActivity.startActivity(PublicNumberDetailActivity.this, String.valueOf(dataBean.getTeamId2()));
        }else {
            joinTeamChat(String.valueOf(dataBean.getTeamId2()));
        }
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
                        TeamMessageActivity.startActivity(PublicNumberDetailActivity.this,teamId);
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
