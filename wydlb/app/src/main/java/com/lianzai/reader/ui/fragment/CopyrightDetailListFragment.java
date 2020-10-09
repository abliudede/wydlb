package com.lianzai.reader.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.bean.WalletListSumResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightDetail;
import com.lianzai.reader.ui.adapter.WalletTransactionAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 版权资产，明细列表
 */
public class CopyrightDetailListFragment extends BaseFragment{

    @Bind(R.id.recyclerView)
    RecyclerView rv_list;

    CustomLoadMoreView customLoadMoreView;
    RxLinearLayoutManager manager;

    WalletTransactionAdapter walletTransactionAdapter;
    List<WalletDetailListResponse.DataBean.ListBean>dataBeanList=new ArrayList<>();

    int pageSize=10;

    String nextTime = "";

    //书籍id和列表类型
    private int bookId;
    private int type;
    private WalletListSumResponse mWalletListSumResponse;


    //类型 5书籍币 6书籍券
    public static final CopyrightDetailListFragment newInstance(int type, int bookId)
    {
        CopyrightDetailListFragment fragment = new CopyrightDetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putInt("bookId", bookId);
        fragment.setArguments(bundle);
        return fragment ;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_circle_dynamic;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        bookId=args.getInt("bookId");
        type=args.getInt("type");
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){

        walletTransactionAdapter=new WalletTransactionAdapter(dataBeanList,getContext());
        walletTransactionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                    circleDynamicRequest(false);
            }
        }, rv_list);
        customLoadMoreView=new CustomLoadMoreView();
        walletTransactionAdapter.setLoadMoreView(customLoadMoreView);

        manager=new RxLinearLayoutManager(getActivity());
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        rv_list.setAdapter(walletTransactionAdapter);

        walletTransactionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                if (!walletTransactionAdapter.getData().get(i).isShowMonth()){
//                    WalletDetailListResponse.DataBean.ListBean bean=walletTransactionAdapter.getData().get(i);
//
//                }
            }
        });

        walletTransactionAdapter.setEmptyView(R.layout.view_dynamic_empty,rv_list);

        requestListData();
    }


    @Override
    public void fetchData() {

    }

    public void requestListData(){
        Map<String, String> map = new HashMap<>();
        map.put("bookId", String.valueOf(bookId));
        map.put("balanceType", String.valueOf(type));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/bookUserBalance/getUserBookIncomeOutSum", map , new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                }catch (Exception ex){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    WalletListSumResponse walletListSumResponse = GsonUtil.getBean(response, WalletListSumResponse.class);
                    if (walletListSumResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        mWalletListSumResponse=walletListSumResponse;
                        //收入支出总和接口查询完后请求列表接口
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                circleDynamicRequest(true);
                            }
                        },getResources().getInteger(R.integer.refresh_delay));

                    }else {
                    }
                }catch (Exception e){
                }
            }
        });

    }


    public void circleDynamicRequest(boolean isRefresh){
        if(isRefresh){
            headerMaps.clear();
            nextTime = "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("bookId", String.valueOf(bookId));
        map.put("balanceType", String.valueOf(type));
        map.put("pageSize", String.valueOf(pageSize));
        if(!TextUtils.isEmpty(nextTime))
        map.put("nextTime", nextTime);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/getUserCopyrightBalanceInOrOutDetailResp", map , new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxLogTool.e(e.toString());
                    walletTransactionAdapter.loadMoreEnd();
                }catch (Exception ex){
//                    ex.printStackTrace();
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    WalletDetailListResponse walletDetailListResponse = GsonUtil.getBean(response, WalletDetailListResponse.class);
                    if (walletDetailListResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh) {
                            dataBeanList.clear();
                        }

                        List<WalletDetailListResponse.DataBean.ListBean> listBeans = walletDetailListResponse.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            dataBeanList.addAll(sectionListData(listBeans));
                            walletTransactionAdapter.replaceData(dataBeanList);
                            walletTransactionAdapter.setEnableLoadMore(true);
                            walletTransactionAdapter.loadMoreComplete();
                            //保存最后一条的时间
                            nextTime = String.valueOf(listBeans.get(listBeans.size() - 1).getNextTime());
                        } else {
                            walletTransactionAdapter.replaceData(dataBeanList);
                            walletTransactionAdapter.loadMoreEnd();
                        }

                    }else {
                        walletTransactionAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
                }
            }
        });
    }

    ArrayList<String> headerMaps=new ArrayList();
    private List<WalletDetailListResponse.DataBean.ListBean> sectionListData(List<WalletDetailListResponse.DataBean.ListBean> listBeans){
        List<WalletDetailListResponse.DataBean.ListBean> newListBeans=new ArrayList<>();
        for(int i=0;i<listBeans.size();i++){
            WalletDetailListResponse.DataBean.ListBean bean=listBeans.get(i);
            String header= RxTimeTool.milliseconds2YYMMString1(bean.getCreateTime());

            if (!headerMaps.contains(header)){//是否包含header
                headerMaps.add(header);
                WalletDetailListResponse.DataBean.ListBean headBean=new WalletDetailListResponse.DataBean.ListBean();
                if (null!=mWalletListSumResponse){
                    for(WalletListSumResponse.DataBean dataBean:mWalletListSumResponse.getData()){
                        String yearDate=header.replace("年","").replace("月","");
                        //类型 5书籍币 6书籍券
                        String str;
                        String strTemp = "书籍";
                        if(getActivity() instanceof ActivityCopyrightDetail){
                            strTemp  =  ((ActivityCopyrightDetail) getActivity()).getStr();
                        }
                        if(TextUtils.isEmpty(strTemp)){
                            strTemp = "书籍";
                        }
                        if(type == 5){
                            str =  strTemp + "点";
                        }else {
                            str =  strTemp + "券";
                        }

                        if (dataBean.getYearMonth().equals(yearDate)){
                            headBean.setDescription("支出 "+ RxDataTool.format2Decimals(String.valueOf(dataBean.getOutAmt()))+str +"  收入 "+RxDataTool.format2Decimals(String.valueOf(dataBean.getInAmt()))+str);
                            break;
                        }else{
                            headBean.setDescription("支出 0"+str+"  收入 0"+str);
                        }
                    }
                }
                headBean.setShowMonth(true);
                headBean.setShowDate(header);
                newListBeans.add(headBean);
            }
            bean.setDescription("");
            bean.setShowMonth(false);
            newListBeans.add(bean);
        }
        return newListBeans;

    }

}
