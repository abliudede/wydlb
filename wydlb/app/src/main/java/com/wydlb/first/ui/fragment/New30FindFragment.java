package com.wydlb.first.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseFragment;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.DataSynEvent;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.model.bean.BannerBean;
import com.wydlb.first.ui.adapter.NewFindAdapter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxAppTool;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.view.RxToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 3.0新版发现页
 */
public class New30FindFragment extends BaseFragment {

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    List<BannerBean.DataBean> bannerBeanList = new ArrayList<>();
    NewFindAdapter newFindAdapter;
    RxLinearLayoutManager manager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.fragment_new30_find;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxEventBusTool.unRegisterEventBus(this);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        newFindAdapter = new NewFindAdapter(bannerBeanList,getActivity());
        manager=new RxLinearLayoutManager(getActivity());
        rv_data.setLayoutManager(manager);
//        rv_data.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        rv_data.setAdapter(newFindAdapter);

        newFindAdapter.setOnItemClickListener(
                (adapter, view, position) -> {
                    if(null != bannerBeanList && !bannerBeanList.isEmpty()) {
                        BannerBean.DataBean bannerBean = bannerBeanList.get(position);
                        RxAppTool.adSkip(bannerBean,getActivity());
                    }
                }
        );


        findRequest();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void fetchData() {

    }

    private String boxUrl = Constant.H5_BASE_URL + "/treasureBox/#/";


    /**
     * 发现页展示内容接口
     * 投放位置 1首页Banner 2任务中心Banner 3金钻领取弹出层 4发现页banner 默认为1
     */
    private void findRequest() {
        Map<String, String> map=new HashMap<>();
        map.put("putPosition",String.valueOf(4));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner",map, new CallBackUtil.CallBackString() {
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
                    RxLogTool.e("gameSwitchRequest:" + response);
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(null != bannerBean.getData() && !bannerBean.getData().isEmpty()){
                            bannerBeanList.clear();
                            bannerBeanList.addAll(bannerBean.getData());
                            newFindAdapter.replaceData(bannerBeanList);
                        }
                    } else {
                        RxToast.custom(bannerBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.REFRESH_NEW_FIND) {
            findRequest();
        }
    }


}
