package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.adapter.RelatedLinksAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogReadFeedback2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2019/05/14
 * 相关链接列表
 */

public class ActivityRelatedLinks extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    RelatedLinksAdapter relatedLinksAdapter;
    List<RelatedLinksBean.DataBean.SourcesBean> arrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private String bookId;
    private String bookDes;
    private ReadSettingsResponse readSettingsResponse;

    RxDialogReadFeedback2 rxDialogReadFeedback;//阅读反馈弹框


    @Override
    public int getLayoutId() {
        return R.layout.activity_related_links;
    }

    public static void startActivity(Context context,String bookId,String bookDes){
        RxActivityTool.removeActivityRelatedLinks();
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("bookDes", bookDes);
        RxActivityTool.skipActivity(context,ActivityRelatedLinks.class,bundle);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
        showOrCloseRefresh(true);
        onRefresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        try {
            bookId = getIntent().getStringExtra("bookId");
            bookDes = getIntent().getStringExtra("bookDes");
        }catch (Exception e){
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        relatedLinksAdapter= new RelatedLinksAdapter(arrayList);

        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityRelatedLinks.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(0,0 , RxImageTool.dip2px(10),0));
        recycler_view.setAdapter(relatedLinksAdapter);

        relatedLinksAdapter.setContentClickListener(new RelatedLinksAdapter.ContentClickListener() {
            @Override
            public void muluClick(View v, int pos) {
                SkipReadUtil.linkRead(ActivityRelatedLinks.this,bookId,arrayList.get(pos).getSource(),true,arrayList.get(pos).getCatalog_url());
            }

            @Override
            public void readClick(View v, int pos) {
                //优先用第一章的链接
                if(!TextUtils.isEmpty(arrayList.get(pos).getFirst_chapter_url())) {
                    SkipReadUtil.linkRead(ActivityRelatedLinks.this, bookId, arrayList.get(pos).getSource(), false, arrayList.get(pos).getFirst_chapter_url());
                }else {
                    SkipReadUtil.linkRead(ActivityRelatedLinks.this, bookId, arrayList.get(pos).getSource(), false, arrayList.get(pos).getCatalog_url());
                }
            }
        });

//        //源配置刷新
//        loadSettingsRequest();
    }


    private  void showOrCloseRefresh(boolean isShow){
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null!=mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                }catch (Exception e){
                    RxLogTool.e("showOrCloseRefresh:"+e.getMessage());
                }
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    @OnClick(R.id.iv_more)
    void iv_moreClick() {
        if(null == rxDialogReadFeedback){
            rxDialogReadFeedback=new RxDialogReadFeedback2(ActivityRelatedLinks.this);
            rxDialogReadFeedback.getTv_title().setText("提交链接");
            ViewGroup.LayoutParams pramas = rxDialogReadFeedback.getEd_feed_content().getLayoutParams();
            pramas.height = RxImageTool.dip2px(65);
            rxDialogReadFeedback.getEd_feed_content().setLayoutParams(pramas);
            rxDialogReadFeedback.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    //提交请求
                    try{
                        postCompleteUrl(rxDialogReadFeedback.getEd_feed_content().getText().toString());
                    }catch (Exception e){
//                        e.printStackTrace();
                    }
                    rxDialogReadFeedback.dismiss();
                }
            });
        }
        rxDialogReadFeedback.show();
    }

    /**
     * 提交补充网址
     */
    private void postCompleteUrl(String content){
        HashMap map=new HashMap();
        map.put("bookId",bookId);
        map.put("link",content);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/circles/supplementBookLink",map, new CallBackUtil.CallBackString() {
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
                        if (null!=rxDialogReadFeedback){
                            rxDialogReadFeedback.getEd_feed_content().setText("");
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

    @Override
    public void gc() {
        RxToast.gc();
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        myNoticeRequest();
    }

    public void myNoticeRequest(){
        Map<String, String> map=new HashMap<>();
        map.put("bookId",bookId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/bookRelatedLinks", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    showOrCloseRefresh(false);
                }catch (Exception ex){
//                    ex.printStackTrace();
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    showOrCloseRefresh(false);
                    RelatedLinksBean relatedLinksBean = GsonUtil.getBean(response, RelatedLinksBean.class);
                    if (relatedLinksBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                         arrayList.clear();
                        relatedLinksAdapter.setAuthor_name(relatedLinksBean.getData().getAuthor_name());
                        relatedLinksAdapter.setBook_name(relatedLinksBean.getData().getBook_name());
                        relatedLinksAdapter.setBookId(bookId);
                        relatedLinksAdapter.setBookDes(bookDes);
                        String source = relatedLinksBean.getData().getMySource();
//                        String source = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source2");


                        RelatedLinksBean.DataBean.SourcesBean sourcesBeanTemp = null;
                        //避免重复读取遍历
                        if(null == readSettingsResponse) {
                            readSettingsResponse = RxSharedPreferencesUtil.getInstance().getObject(Constant.READ_SETTINGS_KEY, ReadSettingsResponse.class);
                        }
                        List<RelatedLinksBean.DataBean.SourcesBean> listBeans = relatedLinksBean.getData().getSources();

                        if (null!=relatedLinksAdapter&&null!=listBeans){
                            int i=0;
                            for(;i<listBeans.size();i++){
                                RelatedLinksBean.DataBean.SourcesBean sourcesBean=listBeans.get(i);
                                //首先设置随机码
                                int random = new Random().nextInt(5);
                                sourcesBean.setRandomInt(random);

                                if (null!=readSettingsResponse){
                                    for(ReadSettingsResponse.ReadSettingsBean readSettingsBean:readSettingsResponse.getReadSettings()){
                                        if (sourcesBean.getSource().equals(readSettingsBean.getSource())){
                                            sourcesBean.setSourceName(readSettingsBean.getSourceName());
                                        }
                                    }
                                }
                                if (!TextUtils.isEmpty(source)&&source.equals(sourcesBean.getSource())){//选中
                                    sourcesBean.setChoose(true);
                                    sourcesBeanTemp = sourcesBean;
                                }
                            }

                            //已匹配到
                            if(null != sourcesBeanTemp){
                                listBeans.remove(sourcesBeanTemp);
                                listBeans.add(0,sourcesBeanTemp);
                            }
                        }


                        if (null != listBeans && !listBeans.isEmpty()) {
                            arrayList.addAll(listBeans);
                            relatedLinksAdapter.notifyDataSetChanged();
                        } else {
                            relatedLinksAdapter.notifyDataSetChanged();
                        }
                    }else{//加载失败
                        RxToast.custom(relatedLinksBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

//    /**
//     * 转码配置读取
//     */
//    private void loadSettingsRequest() {
//        OKHttpUtil.okHttpGet(Constant.BOOK_SOURCE_SETTINGS_URL, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                ReadSettingsResponse readSettingsResponse = GsonUtil.getBean(response, ReadSettingsResponse.class);
//                RxSharedPreferencesUtil.getInstance().putObject(Constant.READ_SETTINGS_KEY, readSettingsResponse);
//            }
//        });
//    }


}
