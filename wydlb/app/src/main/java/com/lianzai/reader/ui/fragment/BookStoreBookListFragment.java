package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.MyBookListResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.MyBookListBean;
import com.lianzai.reader.model.local.MyBookListRepository;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.adapter.MyBookListListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 书架-书单
 */
public class BookStoreBookListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    MyBookListListAdapter bookListListAdapter;

    List<MyBookListBean>dataSource=new ArrayList<>();


    MyBookListResponse.DataBean updateLatestBean;

//    View emptyView;

    AccountTokenBean accountTokenBean;

    long bookRequestTime = 0;

    String bqtKey;

    int uid=0;


    @Bind(R.id.view_not_login)
    View view_not_login;


    LinearLayout ll_go_to_read;

    MainActivity mainActivity;

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.book_store_book_list_fragment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity=(MainActivity)activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {


        ll_go_to_read=view_not_login.findViewById(R.id.ll_go_to_read);

//        emptyView=getLayoutInflater().inflate(R.layout.view_book_list_record_empty,null);

        bookListListAdapter=new MyBookListListAdapter(dataSource,getContext());

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rxLinearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(bookListListAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        bookListListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                requestClearHotPoint(bookListListAdapter.getItem(position), position);
                ActivityBookListDetail.startActivity(getContext(),String.valueOf(bookListListAdapter.getData().get(position).getId()));
            }
        });

        refreshLoginView(false);
    }

    private void refreshLoginView(boolean needSendEvent){
        if (RxLoginTool.isLogin()){
            mSwipeRefreshLayout.setEnabled(true);
            accountTokenBean = RxLoginTool.getLoginAccountToken();
            if(null != accountTokenBean && null != accountTokenBean.getData()) {
                uid = accountTokenBean.getData().getId();
                bqtKey = uid + "_WDSD";
                bookRequestTime = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
            }


            if (bookRequestTime > 0) {//之前已缓存过数据，从本地数据库读取
                //显示数据
                fetchLocalDataAndShow();
            } else {
                if (RxNetTool.isAvailable()) {//网络可用才能用
                    if(needSendEvent){
                        onRefresh();
                    }else {
                        onRefreshFalse();
                    }
                }
            }
        }else{
            mSwipeRefreshLayout.setEnabled(false);
            //游客状态
            view_not_login.setVisibility(View.VISIBLE);

        }


        ll_go_to_read.setOnClickListener(
                v->mainActivity.changeTab(0)
        );
    }


    //只有主动拉动刷新时才会发送事件刷新阅读时长
    @Override
    public void onRefresh() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                RxLogTool.e("onRefresh....");
//                pageNum=1;
                showSwipeRefresh(true);
                bookListUserRequest();
                RxEventBusTool.sendEvents(Constant.EventTag.READTIME_REFRESH_TAG);//阅读时长刷新
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }

    public void onRefreshFalse() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                RxLogTool.e("onRefresh....");
//                pageNum=1;
                showSwipeRefresh(true);
                bookListUserRequest();
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }

    @Override
    public void fetchData() {
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



    private void bookListUserRequest(){
        Map<String,String> map=new HashMap<>();
//        map.put("pageNum",String.valueOf(pageNum));
//        map.put("pageSize",String.valueOf(pageSize));
        long timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
        if (timestamp > 0) {//
            map.put("timestamp", String.valueOf(timestamp));
        }
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/list/user", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                showSwipeRefresh(false);
            }

            @Override
            public void onResponse(String response) {
                try{
                    showSwipeRefresh(false);
                    MyBookListResponse bookListResponse= GsonUtil.getBean(response,MyBookListResponse.class);
                    if (bookListResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                        updateLatestBean=bookListResponse.getData();

                        ArrayList<MyBookListBean>myBookListBeans=new ArrayList<>();

                        if (null != bookListResponse.getData().getDelete_List() && bookListResponse.getData().getDelete_List().size() > 0) {
                            for (MyBookListBean myBookListBean : bookListResponse.getData().getDelete_List()) {//本地移除该书
                                //移除该书单
                                deleteBookListOptions(String.valueOf(myBookListBean.getId()));
                            }
                        }

                        //给每个书单指定用户ID
                        for (MyBookListBean myBookListBean : bookListResponse.getData().getList()) {
                            myBookListBean.setUid(uid);
                            if (timestamp <= 0) {//更新的书单不修改排序时间
                                myBookListBean.setUpdateDate(System.currentTimeMillis());
                            }
                            myBookListBeans.add(myBookListBean);
                        }


                        //显示数据-缓存数据
                        MyBookListRepository.getInstance().saveMyBookList(myBookListBeans);

                        fetchLocalDataAndShow();

                        //缓存这次的请求时间
                        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookListResponse.getData().getTimestamp());

                    }else{//加载失败
                        RxToast.custom(bookListResponse.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.REFRESH_BOOK_STORE_BOOK_LIST_TAG) {
            onRefreshFalse();
        }
        else if(event.getTag()==Constant.EventTag.LOGIN_REFRESH_TAG){//登录刷新
            refreshLoginView(false);
        }else if(event.getTag()==Constant.EventTag.LOGIN_OUT_REFRESH_TAG){//退出登录成功
            refreshLoginView(false);
        }
    }


    private void deleteBookListOptions(String id) {
        //移除
        MyBookListRepository.getInstance().deleteByBookListIdAndUid(accountTokenBean.getData().getId(),id);
        RxLogTool.e("deleteBookListOptions bookListId:" + id);

        fetchLocalDataAndShow();
    }

    /**
     * 渲染数据-缓存数据等操作
     */
    private void fetchLocalDataAndShow() {

        dataSource = MyBookListRepository.getInstance().getBookListByUserId(uid);

        RxLogTool.e("fetchLocalDataAndShow size:" + dataSource.size());
        //填充数据
        bookListListAdapter.replaceData(dataSource);
        if(null == dataSource){
            view_not_login.setVisibility(View.VISIBLE);
        }else if(dataSource.isEmpty()){
            view_not_login.setVisibility(View.VISIBLE);
        }else {
            view_not_login.setVisibility(View.GONE);
        }
//        bookListListAdapter.setEmptyView(emptyView);
    }

    /**
     * 清除更新提示
     */
    private void requestClearHotPoint(MyBookListBean myBookListBean,int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("booklistId", String.valueOf(myBookListBean.getId()));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/booklist/clearUnread", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("requestClearHotPoint:" + response);
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //本地更新
                        myBookListBean.setIsUnread(false);
                        bookListListAdapter.setData(pos,myBookListBean);

                        MyBookListRepository.getInstance().updateBookListById(String.valueOf(myBookListBean.getId()),String.valueOf(uid));
                        fetchLocalDataAndShow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
