package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.adapter.BookListListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 书单列表
 */
public class ActivityBookList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    BookListListAdapter bookListListAdapter;

    List<BookListResponse.DataBean.ListBean> dataSource=new ArrayList<>();

    CustomLoadMoreView customLoadMoreView;


    @Bind(R.id.tv_commom_title)
    TextView tv_common_title;

    TextView tv_recommend_book_list;

    int type= Constant.BookListType.RECOMMEND_TYPE;


    View headerView;

    TextView tv_more;

    int pageNum=1;
    int pageSize=10;

    BookListResponse.DataBean updateLatestBean;

    String tagsId;

    public static void startActivity(Activity activity,int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        RxActivityTool.skipActivity(activity, ActivityBookList.class, bundle);
    }

    public static void startActivity(Activity activity,int type,String tagsId) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("tagsId", tagsId);
        RxActivityTool.skipActivity(activity, ActivityBookList.class, bundle);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_list;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        type=getIntent().getExtras().getInt("type");

        tagsId=getIntent().getExtras().getString("tagsId");
        if (type==Constant.BookListType.RECOMMEND_TYPE){
            tv_common_title.setText(getResources().getString(R.string.recommend_book_list_title));
        }else if(type==Constant.BookListType.UPDATE_TYPE){
            tv_common_title.setText(getResources().getString(R.string.update_book_list_title));
        }else if(type==Constant.BookListType.RECOMMEND_FOR_YOU_TYPE){
            tv_common_title.setText(getResources().getString(R.string.recommend_foryou_title));
        }else if(type==Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE){
            tv_common_title.setText("为您推荐的书单");

            //为您推荐收单头部
            headerView=getLayoutInflater().inflate(R.layout.header_recommend_book_list_title,null);
            tv_more=headerView.findViewById(R.id.tv_more);

            tv_recommend_book_list=headerView.findViewById(R.id.tv_recommend_book_list);


            tv_more.setOnClickListener(
                    v->{
                        RxEventBusTool.sendEvents(Constant.EventTag.HOBBY_SETTING_CLOSE_TAG);//关闭阅读喜好界面
                        finish();
                    }
            );
        }


        bookListListAdapter=new BookListListAdapter(dataSource,this);

        if (null!=headerView&&type==Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE){
            bookListListAdapter.addHeaderView(headerView);
        }else{
            customLoadMoreView = new CustomLoadMoreView();
            bookListListAdapter.setLoadMoreView(customLoadMoreView);
            bookListListAdapter.setOnLoadMoreListener(this,recyclerView);
        }

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(this);
        recyclerView.setLayoutManager(rxLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(bookListListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        bookListListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityBookListDetail.startActivity(ActivityBookList.this,String.valueOf(bookListListAdapter.getData().get(position).getId()));

                if (type==Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE){
                    RxEventBusTool.sendEvents(Constant.EventTag.HOBBY_SETTING_CLOSE_TAG);//关闭阅读喜好界面
                    finish();//关闭当前页再跳转到其他页面
                }
            }
        });


        onRefresh();
    }

    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }



    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @Override
    public void onRefresh() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showSwipeRefresh(true);
                pageNum=1;
                bookListRequest();

                bookListListAdapter.setEnableLoadMore(true);//可以加载更多
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        bookListRequest();
    }

    private void showSwipeRefresh(boolean isShow){
        try{
            if (null!=mSwipeRefreshLayout){
                mSwipeRefreshLayout.setRefreshing(isShow);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bookListRequest(){
        Map<String,String> map=new HashMap<>();
        map.put("pageNum",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));
        if (!TextUtils.isEmpty(tagsId)){
            map.put("tag",tagsId);
        }
        String urlstr = "/booklist/list";
        switch (type){
            case Constant.BookListType.RECOMMEND_TYPE:
                urlstr = "/booklist/list/recommend";
                break;
            case Constant.BookListType.UPDATE_TYPE:
                urlstr = "/booklist/list";
                break;
            case Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE:
                urlstr = "/booklist/list";
                break;
            case Constant.BookListType.RECOMMEND_FOR_YOU_TYPE:
                urlstr = "/booklist/list/like";
                break;
                default:
                    urlstr = "/booklist/list";
                    break;
        }

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + urlstr, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败bookListRequest").show();
                    bookListListAdapter.loadMoreFail();
                    showSwipeRefresh(false);
                } catch (Exception ee) {
                }

            }

            @Override
            public void onResponse(String response) {
                try{
                    showSwipeRefresh(false);
                    BookListResponse bookListResponse= GsonUtil.getBean(response,BookListResponse.class);
                    if (bookListResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        updateLatestBean=bookListResponse.getData();

                        if (type==Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE){//首次推荐
                            String nickName= RxLoginTool.isLogin()?RxTool.getAccountBean().getData().getNickName():"大兄弟";

                            tv_recommend_book_list.setText(String.format(getResources().getString(R.string.first_recommend_book_list),nickName,updateLatestBean.getSize()));
                        }

                        //填充数据
                        if (pageNum>1){
                            bookListListAdapter.addData(updateLatestBean.getList());

                        }else{
                            dataSource.clear();
                            dataSource.addAll(updateLatestBean.getList());
                            bookListListAdapter.replaceData(dataSource);
                        }

                        //是否允许加载更多
                        if (bookListResponse.getData().getSize()<bookListResponse.getData().getPageSize()){//最后一页
                            bookListListAdapter.loadMoreEnd();
                        }else{
                            bookListListAdapter.loadMoreComplete();
                        }

                        bookListListAdapter.setEmptyView(R.layout.view_book_list_empty);

                    }else{//加载失败
                        RxToast.custom(bookListResponse.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}