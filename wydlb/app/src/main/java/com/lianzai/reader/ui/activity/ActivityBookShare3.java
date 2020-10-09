package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.FeedBackTypeResponse;
import com.lianzai.reader.bean.UnBindWxBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.activity.UrlIdentification.ActivityTeamChoose;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogFeedbackType;
import com.lianzai.reader.view.dialog.RxDialogReadFeedback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/24.
 * url阅读界面，弹出书信息
 */

public class ActivityBookShare3 extends BaseActivityForTranslucent {

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

    @Bind(R.id.tv_share_team)
    TextView tv_share_team;

    private String img;
    private String bookname;
    private String athour;
    private String bookdes;


    //补充网址弹窗
    private String url;
    private String bookid;
    private boolean isConcern;

    private String bqtKey;
    private String uid;
    private AccountDetailBean accountDetailBean;
    private Drawable iconConcern;
    private Drawable iconConcernCancle;

    //反馈类型列表弹框
    RxDialogFeedbackType rxDialogFeedbackType;
    RxDialogReadFeedback rxDialogReadFeedback;//阅读反馈弹框
    private String chapterId;
    private String chaptername;
    private String lianzaihaoId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_share3;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }

    public static void startActivity(Activity context, String img,String bookname,String athour,String bookdes,String url,String bookid,boolean isConcern
            ,String chapterId,String chaptername,String lianzaihaoId){
        Bundle intent=new Bundle();
        intent.putString("img",img);
        intent.putString("bookname",bookname);
        intent.putString("athour",athour);
        intent.putString("bookdes",bookdes);
        intent.putString("url",url);
        intent.putString("bookid",bookid);
        intent.putBoolean("isConcern",isConcern);
        intent.putString("chapterId",chapterId);
        intent.putString("chaptername",chaptername);
        intent.putString("lianzaihaoId",lianzaihaoId);
        RxActivityTool.skipActivity(context,ActivityBookShare3.class,intent);
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
//        hideSystemBar();
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        try{
            img= intent.getStringExtra("img");
            bookname= intent.getStringExtra("bookname");
            athour= intent.getStringExtra("athour");
            bookdes= intent.getStringExtra("bookdes");
            url= intent.getStringExtra("url");
            bookid= intent.getStringExtra("bookid");
            isConcern = intent.getBooleanExtra("isConcern",false);
            chapterId= intent.getStringExtra("chapterId");
            chaptername= intent.getStringExtra("chaptername");
            lianzaihaoId= intent.getStringExtra("lianzaihaoId");
        }catch (Exception e){

        }


        iconConcern= RxTool.getContext().getResources().getDrawable(R.mipmap.share_follow);
        iconConcern.setBounds(0, 0,iconConcern.getIntrinsicWidth(), iconConcern.getIntrinsicHeight());
        iconConcernCancle= RxTool.getContext().getResources().getDrawable(R.mipmap.share_follow_cancle);
        iconConcernCancle.setBounds(0, 0,iconConcernCancle.getIntrinsicWidth(), iconConcernCancle.getIntrinsicHeight());

        if(isConcern) {
            tv_share_follow.setVisibility(View.VISIBLE);
            tv_share_follow.setCompoundDrawables(null,iconConcernCancle,null,null);
            tv_share_follow.setText("移出书架");
        }else {
            tv_share_follow.setVisibility(View.VISIBLE);
            tv_share_follow.setCompoundDrawables(null,iconConcern,null,null);
            tv_share_follow.setText("加入书架");
        }

        accountDetailBean = RxTool.getAccountBean();
        if(null != accountDetailBean) {
            uid = accountDetailBean.getData().getUid();
            bqtKey = uid + Constant.BQT_KEY;
        }else {
            tv_share_follow.setVisibility(View.GONE);
            tv_share_team.setVisibility(View.GONE);
        }

        RxImageTool.loadImage(ActivityBookShare3.this,img,iv_book_cover);
        tv_book_name.setText(bookname);
        tv_book_author.setText(athour);
        tv_book_des.setText(bookdes);

        //反馈类型请求
        requestFeedTypeList();
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
            ActivityCircleDetail.startActivity(ActivityBookShare3.this, lianzaihaoId);
        finish();
    }


    @OnClick(R.id.tv_cancel)void tv_cancelClick(){
        //直接关闭
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.tv_share_team)void tv_share_teamClick(){
        //分享到群聊
        if(RxLoginTool.isLogin()) {
            ActivityTeamChoose.startActivity(ActivityBookShare3.this, bookname, bookdes, img, Integer.parseInt(bookid),url);
            finish();
        }
    }

    @OnClick(R.id.tv_share_follow)void tv_share_followClick(){
        if(isConcern) {
            //取消关注
            requestUnConcernBookId(bookid);
        }else {
            //关注
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("bookCover", img);
            jsonObject.addProperty("bookName", bookname);
            jsonObject.addProperty("bookId", bookid);
            requestConcernUrlBook(jsonObject.toString());
        }

    }

    @OnClick(R.id.tv_share_kouling)void tv_share_koulingClick(){
        //连载口令
        ActivityShareBitmapBook.startActivity(ActivityBookShare3.this,bookid,"");
        finish();
    }



    @OnClick(R.id.tv_share_exchange)void tv_share_exchangeClick(){
        //转码声明
        ActivityWebView.startActivity(ActivityBookShare3.this,Constant.H5_HELP_BASE_URL + "/helper/show/27");
        finish();
    }

    @OnClick(R.id.tv_share_complete)void tv_share_completeClick(){
        //复制链接
        RxClipboardTool.copyText(ActivityBookShare3.this,url);
        RxToast.custom("链接复制成功").show();
    }

    /*打开原网页*/
    @OnClick(R.id.tv_share_open_web)void tv_share_open_webClick(){
        ActivityWebView.startActivityForReadNormal(ActivityBookShare3.this, url,bookid,"",false);
      finish();
    }

    @OnClick(R.id.tv_share_problem)void  tv_share_problemClick(){
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
                            rxDialogReadFeedback = new RxDialogReadFeedback(ActivityBookShare3.this);
                        }
                        rxDialogReadFeedback.setSureListener(new OnRepeatClickListener() {
                            @Override
                            public void onRepeatClick(View v) {
                                //提交请求
                                try {
                                    postFeedback(String.valueOf(dataBean.getId()), bookid, chapterId, rxDialogReadFeedback.getEd_feed_content().getText().toString(), false, bookname + "::" + chaptername);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                rxDialogReadFeedback.dismiss();
                            }
                        });
                        rxDialogReadFeedback.show();
                    } else {
                        //直接提交请求
                        try {
                            postFeedback(String.valueOf(dataBean.getId()), bookid,chapterId, null, false, bookname + "::" + chaptername);
                        } catch (Exception e) {
                            e.printStackTrace();
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
                    e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取连载号口令
     */
    private void requestGetCountersign(String bookId) {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/getCountersign/" + bookId , new CallBackUtil.CallBackString() {
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
                        ActivityShareBitmapBook.startActivity(ActivityBookShare3.this,bookId,"");
                        RxClipboardTool.copyText(ActivityBookShare3.this,baseBean.getData());
                        RxToast.custom("口令已复制到剪切板").show();
                        finish();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 关注
     */
    private void requestConcernUrlBook(String json) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/book/shelf/bookAttention", json, new CallBackUtil.CallBackString() {
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
                        tv_share_follow.setVisibility(View.VISIBLE);
                        tv_share_follow.setCompoundDrawables(null,iconConcernCancle,null,null);
                        tv_share_follow.setText("移出书架");
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 取消关注
     */
    private void requestUnConcernBookId(String bookId) {
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/shelf/bookCancelAttention/" + bookId , new CallBackUtil.CallBackString() {
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
                        tv_share_follow.setVisibility(View.VISIBLE);
                        tv_share_follow.setCompoundDrawables(null,iconConcern,null,null);
                        tv_share_follow.setText("加入书架");
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
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
                                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
                                    //移除该书,旧书模式
                                    BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(Integer.parseInt(uid), bookStoreBean.getPlatformId());
                                }
                            }
                        }
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
                                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
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
                        RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_BOOK_STORE_TAG);
                        //书籍同步完之后关闭当前弹窗
                        finish();
                        //告诉url页面重新请求书籍信息
                        RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_BOOK_INFO);
                    } else {//加载失败
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
