package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.FeedBackTypeResponse;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.bean.UnBindWxBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityCircleSetting;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.ui.activity.read.ActivityTeamChooseForLianzaihao;
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
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogFeedbackType;
import com.lianzai.reader.view.dialog.RxDialogReadFeedback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/24.
 * 旧阅读模式弹出页面
 */

public class ActivityBookShare2 extends BaseActivityForTranslucent {

    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    @Bind(R.id.tv_book_name)
    TextView tv_book_name;

    @Bind(R.id.tv_book_author)
    TextView tv_book_author;

    @Bind(R.id.tv_book_des)
    TextView tv_book_des;

    @Bind(R.id.tv_share_follow1)
    TextView tv_share_follow1;

    @Bind(R.id.tv_share_team)
    TextView tv_share_team;

    @Bind(R.id.tv_share_open_web)
    TextView tv_share_open_web;

    @Bind(R.id.tv_share_copyright)
    TextView tv_share_copyright;





    private String img;
    private String bookname;
    private String athour;
    private String bookdes;
    private int lianzaihaoid;

    private boolean isSigned;
    private String signUrl;

    private WbShareHandler shareHandler;

    //补充网址弹窗
    RxDialogReadFeedback rxDialogReadFeedback2;
    private String url;
    private String bookId;

    private boolean isConcern;
    private String bqtKey;
    private String uid;
    private AccountDetailBean accountDetailBean;
    private Drawable iconConcern;
    private Drawable iconSetLike;
    private Drawable iconConcernCancle;

    //反馈类型列表弹框
    RxDialogFeedbackType rxDialogFeedbackType;
    RxDialogReadFeedback rxDialogReadFeedback;//阅读反馈弹框
    private String chapterId;
    private String chaptername;
    private int platformType;

    //设为偏好专用字段
    private String localSource;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_share2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    public static void startActivity(Activity context, String img,String bookname,String athour,String bookdes,int lianzaihaoid,String url
            ,String bookId,boolean isConcern
            ,String chapterId,String chaptername,int platformType ,boolean isSigned , String signUrl){
        Bundle intent=new Bundle();
        intent.putString("img",img);
        intent.putString("bookname",bookname);
        intent.putString("athour",athour);
        intent.putString("bookdes",bookdes);
        intent.putInt("lianzaihaoid",lianzaihaoid);
        intent.putString("url",url);
        intent.putString("bookId",bookId);
        intent.putBoolean("isConcern",isConcern);
        intent.putString("chapterId",chapterId);
        intent.putString("chaptername",chaptername);
        intent.putInt("platformType",platformType);
        intent.putBoolean("isSigned",isSigned);
        intent.putString("signUrl",signUrl);
        RxActivityTool.skipActivity(context,ActivityBookShare2.class,intent);
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
//        hideSystemBar();
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        try {
            img = intent.getStringExtra("img");
            bookname = intent.getStringExtra("bookname");
            athour = intent.getStringExtra("athour");
            bookdes = intent.getStringExtra("bookdes");
            lianzaihaoid = intent.getIntExtra("lianzaihaoid", 0);
            url = intent.getStringExtra("url");
            bookId = intent.getStringExtra("bookId");
            isConcern = intent.getBooleanExtra("isConcern",false);
            chapterId= intent.getStringExtra("chapterId");
            chaptername= intent.getStringExtra("chaptername");
            platformType = intent.getIntExtra("platformType", 0);
            isSigned =  intent.getBooleanExtra("isSigned",false);
            signUrl = intent.getStringExtra("signUrl");
        }catch (Exception e){

        }

        if(isSigned){
            tv_share_copyright.setVisibility(View.VISIBLE);
            tv_share_copyright.setOnClickListener(
                    v -> {
                        ActivityWebView.startActivity(ActivityBookShare2.this,signUrl);
                    }
            );
        }else {
            tv_share_copyright.setVisibility(View.GONE);
        }

        if(platformType == Constant.UserIdentity.BOOK_OUTSIED_NUMBER){
            tv_share_open_web.setVisibility(View.VISIBLE);
        }else {
            tv_share_open_web.setVisibility(View.GONE);
        }


        iconConcern= RxTool.getContext().getResources().getDrawable(R.mipmap.share_follow);
        iconConcern.setBounds(0, 0,iconConcern.getIntrinsicWidth(), iconConcern.getIntrinsicHeight());
        iconSetLike= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_share_setlike);
        iconSetLike.setBounds(0, 0,iconSetLike.getIntrinsicWidth(), iconSetLike.getIntrinsicHeight());
        iconConcernCancle= RxTool.getContext().getResources().getDrawable(R.mipmap.share_follow_cancle);
        iconConcernCancle.setBounds(0, 0,iconConcernCancle.getIntrinsicWidth(), iconConcernCancle.getIntrinsicHeight());



        showConcern();

        accountDetailBean = RxTool.getAccountBean();
        if(null != accountDetailBean) {
            uid = accountDetailBean.getData().getUid();
            bqtKey = uid + Constant.BQT_KEY;
        }else {
            tv_share_follow1.setVisibility(View.GONE);
            tv_share_team.setVisibility(View.GONE);
        }



        RxImageTool.loadImage(ActivityBookShare2.this,img,iv_book_cover);
        tv_book_name.setText(bookname);
        tv_book_author.setText(athour);
        tv_book_des.setText(bookdes);


        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        rxDialogReadFeedback2=new RxDialogReadFeedback(ActivityBookShare2.this);
        rxDialogReadFeedback2.getTv_title().setText("补充网址");
        rxDialogReadFeedback2.getTv_des().setText("如该小说章节有缺失，您可帮助我们提交可正常阅读的第三方网站网址，查实有效并采用后，您将有可能获得我们的特殊奖励哦！");
        rxDialogReadFeedback2.getTv_des().setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams pramas = rxDialogReadFeedback2.getEd_feed_content().getLayoutParams();
        pramas.height = RxImageTool.dip2px(65);
        rxDialogReadFeedback2.getEd_feed_content().setLayoutParams(pramas);
        rxDialogReadFeedback2.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                //提交请求
                try{
                    postCompleteUrl("99",bookId,chapterId,rxDialogReadFeedback2.getEd_feed_content().getText().toString());
                }catch (Exception e){
//                    e.printStackTrace();
                }
                rxDialogReadFeedback2.dismiss();
            }
        });

        //反馈类型请求
        requestFeedTypeList();
    }

    private void showConcern(){
        String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
        localSource  = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source2");
        //复杂的按钮展示逻辑
        if("lianzai.com".equals(nowSource)){
            //内站书无偏好功能
            if (isConcern) {
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcernCancle, null, null);
                tv_share_follow1.setText("移出书架");
            } else{
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcern, null, null);
                tv_share_follow1.setText("加入书架");
            }
        }else {
            if (isConcern && TextUtils.isEmpty(nowSource)) {
                //关注且没有传入的source时，单纯的移出书架
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcernCancle, null, null);
                tv_share_follow1.setText("移出书架");
            } else if (isConcern && !TextUtils.isEmpty(nowSource) && !TextUtils.isEmpty(localSource) && nowSource.equals(localSource)) {
                //关注且有传入source且和本地相同时，单纯的移出书架
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcernCancle, null, null);
                tv_share_follow1.setText("移出书架");
            } else if (isConcern && !TextUtils.isEmpty(nowSource)) {
                //关注且有传入source，不符合上面条件的，单纯的设为偏好
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconSetLike, null, null);
                tv_share_follow1.setText("设为偏好");
            } else if (!isConcern && !TextUtils.isEmpty(nowSource)) {
                //没关注且有传入source，那么加入书架并且设为偏好
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcern, null, null);
                tv_share_follow1.setText("加入书架");
            } else {
                //没关注且没有传入source，单纯的加入书架
                tv_share_follow1.setVisibility(View.VISIBLE);
                tv_share_follow1.setCompoundDrawables(null, iconConcern, null, null);
                tv_share_follow1.setText("加入书架");
            }
        }
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



    @OnClick(R.id.top_view)void top_viewClick(){
        ActivityCircleDetail.startActivity(ActivityBookShare2.this, String.valueOf(lianzaihaoid));
        finish();
    }

    @OnClick(R.id.tv_cancel)void tv_cancelClick(){
        //直接关闭
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }


    //功能按钮
    @OnClick(R.id.tv_share_team)void tv_share_teamClick(){
        if(RxLoginTool.isLogin()) {
            ActivityTeamChooseForLianzaihao.startActivity(ActivityBookShare2.this, bookname, bookdes, img, bookId, String.valueOf(lianzaihaoid));
            finish();
        }
    }

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

    @OnClick(R.id.tv_share_kouling)void tv_share_koulingClick(){
        //连载口令
        String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
        ActivityShareBitmapBook.startActivity(ActivityBookShare2.this,bookId,nowSource);
        finish();
    }

    @OnClick(R.id.tv_share_follow)void tv_share_followClick(){
        //转码声明
        ActivityWebView.startActivity(ActivityBookShare2.this,Constant.H5_HELP_BASE_URL + "/helper/show/27");
        finish();
    }

    @OnClick(R.id.tv_share_complete)void tv_share_completeClick(){
        //补充网址
        rxDialogReadFeedback2.show();
    }

    @OnClick(R.id.tv_share_copy)void tv_share_copyClick(){
        //复制链接
        RxClipboardTool.copyText(ActivityBookShare2.this,url);
        RxToast.custom("链接复制成功").show();
    }


    /*打开原网页*/
    @OnClick(R.id.tv_share_open_web)void tv_share_open_webClick(){
        //优先用书评传进来的source
        String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
        ActivityWebView.startActivityForReadNormal(ActivityBookShare2.this,url,bookId,nowSource,false);
        finish();
    }

    @OnClick(R.id.tv_share_follow1)void tv_share_follow1Click(){
        String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
        localSource  = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source2");

        if("lianzai.com".equals(nowSource)){
            //内站书无偏好功能
            if (isConcern) {
                //单纯的移出书架
                requestUnConcernPlatform(String.valueOf(lianzaihaoid));
            } else{
                //单纯的加入书架
                requestConcernPlatform(String.valueOf(lianzaihaoid),"");
            }
        }else {

            if (isConcern && TextUtils.isEmpty(nowSource)) {
                //关注且没有传入的source时，单纯的移出书架
                requestUnConcernPlatform(String.valueOf(lianzaihaoid));
            } else if (isConcern && !TextUtils.isEmpty(nowSource) && !TextUtils.isEmpty(localSource) && nowSource.equals(localSource)) {
                //关注且有传入source且和本地相同时，单纯的移出书架
                requestUnConcernPlatform(String.valueOf(lianzaihaoid));
            } else if (isConcern && !TextUtils.isEmpty(nowSource)) {
                //关注且有传入source，不符合上面条件的，单纯的设为偏好
//            RxToast.custom("已成功设为偏好", Constant.ToastType.TOAST_SUCCESS).show();
                requestPreference(String.valueOf(lianzaihaoid), nowSource);
            } else if (!isConcern && !TextUtils.isEmpty(nowSource)) {
                //没关注且有传入source，那么加入书架并且设为偏好
                requestConcernPlatform(String.valueOf(lianzaihaoid),nowSource);
//                requestPreference(String.valueOf(lianzaihaoid), );
            } else {
                //没关注且没有传入source，单纯的加入书架
                requestConcernPlatform(String.valueOf(lianzaihaoid),"");
            }

        }
    }

    /*提交反馈*/
    @OnClick(R.id.tv_share_problem)void tv_share_problemClick(){
        if (null != feedbackListData && feedbackListData.size() == 0) {
            return;
        }
        if (null == rxDialogFeedbackType) {
            rxDialogFeedbackType = new RxDialogFeedbackType(this, R.style.BottomDialogStyle);
            rxDialogFeedbackType.setFeedbackTypeCallback(new RxDialogFeedbackType.FeedbackTypeCallback() {
                @Override
                public void feedBackTypeClick(FeedBackTypeResponse.DataBean dataBean) {

                    if (dataBean.getRequired() == 1) {//需要输入内容
                        if (null == rxDialogReadFeedback) {
                            rxDialogReadFeedback = new RxDialogReadFeedback(ActivityBookShare2.this);
                        }
                        rxDialogReadFeedback.setSureListener(new OnRepeatClickListener() {
                            @Override
                            public void onRepeatClick(View v) {
                                //提交请求
                                try {
                                    postFeedback(String.valueOf(dataBean.getId()), bookId,chapterId, rxDialogReadFeedback.getEd_feed_content().getText().toString(), false, bookname + "::" + chaptername);

                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                                rxDialogReadFeedback.dismiss();
                            }
                        });
                        rxDialogReadFeedback.show();
                    } else {
                        //直接提交请求
                        try {
                            postFeedback(String.valueOf(dataBean.getId()), bookId, chapterId, null, false,bookname + "::" + chaptername);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                        rxDialogFeedbackType.dismiss();
                    }
                }
            });
        }

        //填充数据
        rxDialogFeedbackType.setData(feedbackListData);
        rxDialogFeedbackType.show();
    }



    /**
     * 设为偏好
     */
    private void requestPreference(String platformId , String source) {
        Map<String, String> map = new HashMap<>();
        if(!TextUtils.isEmpty(source)){
            map.put("source", source);
            RxSharedPreferencesUtil.getInstance().putString(bookId + "_source2",source);
            RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_LIKE_INFO);
            showConcern();
        }
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + platformId + "/preference",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("已成功设为偏好", Constant.ToastType.TOAST_SUCCESS).show();
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 关注
     */
    private void requestConcernPlatform(String platformId,String source) {
        Map<String, String> map = new HashMap<>();
        if(!TextUtils.isEmpty(source)){
            map.put("source", source);
            RxSharedPreferencesUtil.getInstance().putString(bookId + "_source2",source);
            RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_LIKE_INFO);
            showConcern();
        }
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + platformId + "/Concern",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore();
                        RxToast.custom("已成功添加至书架", Constant.ToastType.TOAST_SUCCESS).show();
                        isConcern = true;
                        showConcern();
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 取消关注
     */
    private void requestUnConcernPlatform(String objectId) {
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + objectId + "/UnConcern", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore();
                        RxToast.custom("已成功移除书架", Constant.ToastType.TOAST_SUCCESS).show();
                        isConcern = false;
                        showConcern();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("e:" + e.getMessage());
                }
            }
        });
    }


    //同步书架数据
    private void requestBookStore() {
        ArrayMap map = new ArrayMap();
        long timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
        if (timestamp > 0) {//
            map.put("timestamp", String.valueOf(timestamp));
        }
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/shelf/refresh", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    BookStoreResponse bookStoreResponse = GsonUtil.getBean(response, BookStoreResponse.class);
                    if (bookStoreResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<BookStoreBeanN> bookStoreBeanList = new ArrayList<>();
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getDelete_List() && bookStoreResponse.getData().getDelete_List().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getDelete_List()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,旧书模式
                                    BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(Integer.parseInt(uid), bookStoreBean.getPlatformId());
                                }
                            }
                        }
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,新url模式
                                    BookStoreRepository.getInstance().deleteAllByBookIdAndUid(Integer.parseInt(uid), bookStoreBean.getBookId());
                                }
                            }
                        }
                        //给每本书指定用户ID
                        int i = 0;
                        long time = System.currentTimeMillis();
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(uid));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //给每本书指定用户ID
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(uid));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //缓存这次的请求时间
                        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookStoreResponse.getData().getTimestamp());
                        //显示数据-缓存数据
                        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_TAG);
                        //书籍同步完之后关闭当前弹窗
                        finish();
                        //告诉阅读页面重新请求书籍信息
                        RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_BOOK_INFO);

                    } else {//加载失败
                    }

                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 提交反馈请求
     */
    private void postFeedback(String typeId, String bookId, String mChapterId, String content, boolean isAutoUpload, String chapterTitle) {
        HashMap map = new HashMap();
        map.put("typeId", typeId);
        map.put("bookId", bookId);
        map.put("chapterId", mChapterId);

        if (!TextUtils.isEmpty(chapterTitle)) {
            map.put("chapterTitle", chapterTitle);
        }
        if (!TextUtils.isEmpty(content)) {
            map.put("content", content);
        }

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/feedback/save", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    if (!isAutoUpload) {
                        BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                        if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            RxToast.custom("反馈成功").show();
                            //清空
                            if (null != rxDialogReadFeedback) {
                                rxDialogReadFeedback.getEd_feed_content().setText("");
                            }
                        } else {
                            RxToast.custom("反馈失败").show();
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * -------反馈------
     **/

    List<FeedBackTypeResponse.DataBean> feedbackListData = new ArrayList<>();//反馈类型

    private void requestFeedTypeList() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/feedback/queryType", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    FeedBackTypeResponse feedBackTypeResponse = GsonUtil.getBean(response, FeedBackTypeResponse.class);
                    if (feedBackTypeResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        feedbackListData = feedBackTypeResponse.getData();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 提交补充网址
     */
    private void postCompleteUrl(String typeId,String bookId,String mChapterId,String content){
        HashMap map=new HashMap();
        map.put("typeId",typeId);
        map.put("bookId",bookId);
        map.put("chapterId",mChapterId);
        if (!TextUtils.isEmpty(content)){
            map.put("content",content);
        }
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/feedback/save",map, new CallBackUtil.CallBackString() {
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
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("提交成功").show();
                        //清空
                        if (null!=rxDialogReadFeedback2){
                            rxDialogReadFeedback2.getEd_feed_content().setText("");
                        }
                    }else{
                        RxToast.custom("提交失败").show();
                    }
                }catch (Exception e){
//                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取连载号口令
     */
    private void requestGetCountersign(String bookId) {
        ArrayMap<String, String> map = new ArrayMap();
            //获取本地存储的source
            String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
            if(!TextUtils.isEmpty(nowSource)){
                map.put("source", nowSource);
            }

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/getCountersign/" + bookId ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    UnBindWxBean baseBean = GsonUtil.getBean(response, UnBindWxBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        String nowSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
                        ActivityShareBitmapBook.startActivity(ActivityBookShare2.this,bookId,nowSource);
                        RxClipboardTool.copyText(ActivityBookShare2.this,baseBean.getData());
                        RxToast.custom("口令已复制到剪切板").show();
                        finish();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
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
        map.put("objId", String.valueOf(lianzaihaoid));

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
                                RxShareUtils.QQShareUrl(ActivityBookShare2.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityBookShare2.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityBookShare2.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
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
