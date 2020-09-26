package com.lianzai.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CircleBookBean;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.adapter.AdForExampleAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.RxToast;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 腾讯百度穿山甲，集成list实例
 */
public class AdForExampleFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener,NativeExpressAD.NativeExpressADListener{

    @Bind(R.id.recyclerView)
    RecyclerView rv_list;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩

    CustomLoadMoreView customLoadMoreView;

    AdForExampleAdapter adForExampleAdapter;

    int pageNum=1;
    int pageSize=10;

    List<Object> mdatalist=new ArrayList<>();

    MoreOptionPopup moreOptionPopup;


    /*广点通腾讯广告联盟区域*/
    private NativeExpressAD mADManager;
    private List<NativeExpressADView> mAdViewList;
    private HashMap<NativeExpressADView, Integer> mAdViewIdMap = new HashMap<NativeExpressADView, Integer>();


    private List<BannerBean.DataBean> attrs;
    private List<BannerBean.DataBean> ownAttrs;
    private List<BannerBean.DataBean> sdkAttrs;

    private int adCount = 0;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview;
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
        // 使用完了每一个NativeExpressADView之后都要释放掉资源。
        if (mAdViewList != null) {
            for (NativeExpressADView view : mAdViewList) {
                view.destroy();
            }
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        adForExampleAdapter=new AdForExampleAdapter(mdatalist,getContext());
        adForExampleAdapter.setDynamicClickListener(dynamicClickListener);

        customLoadMoreView = new CustomLoadMoreView();
        adForExampleAdapter.setLoadMoreView(customLoadMoreView);
        adForExampleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                circleDynamicRequest(false);
            }
        }, rv_list);

        RxLinearLayoutManager manager = new RxLinearLayoutManager(getContext());
        rv_list.setLayoutManager(manager);
//        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0,0 ,RxImageTool.dip2px(10),0));
        rv_list.setAdapter(adForExampleAdapter);

        adForExampleAdapter.setEmptyView(R.layout.view_home_dynamic_empty,rv_list);
        getData();
    }


    @Override
    public void fetchData() {
    }

    @Override
    public void onRefresh() {
        try{
            if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).getHomePageSwitchFragment().findIntervalRewardRequest();
            }
        }catch (Exception e){

        }
        getData();
    }

    public void getData(){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circleDynamicRequest(true);
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }


    private void showSwipeRefresh(boolean isShow){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (null!=mSwipeRefreshLayout){
                        mSwipeRefreshLayout.setRefreshing(isShow);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }


    public void circleDynamicRequest(boolean isRefresh){
        if (isRefresh){
            adCount = 0;
            pageNum=1;
        }
        Map<String, String> map=new HashMap<>();
        map.put("pageNumber",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/userAllDynamicNew", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/userAllDynamicNew").show();
                    adForExampleAdapter.loadMoreFail();
                    showSwipeRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    showSwipeRefresh(false);
                    CircleDynamicBean circleDynamicBean = GsonUtil.getBean(response, CircleDynamicBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            mdatalist.clear();
                        }
                        if(null != circleDynamicBean.getData()) {
                            List<CircleDynamicBean.DataBean.DynamicRespBean> listBeans = circleDynamicBean.getData().getList();
                            if (null != listBeans && !listBeans.isEmpty()) {
                                mdatalist.addAll(listBeans);
                                adForExampleAdapter.replaceData(mdatalist);
                                adForExampleAdapter.setEnableLoadMore(true);
                                adForExampleAdapter.loadMoreComplete();
                            } else {
                                adForExampleAdapter.replaceData(mdatalist);
                                adForExampleAdapter.loadMoreEnd();
                            }
                            attrs = circleDynamicBean.getData().getAttrs();
                            if(null != attrs) {
                                //广告插入开始
                                initNativeExpressAD();
                            }
                        }else {
                            adForExampleAdapter.replaceData(mdatalist);
                            adForExampleAdapter.loadMoreEnd();
                        }
                    }else {
                        adForExampleAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    AdForExampleAdapter.DynamicClickListener dynamicClickListener=new AdForExampleAdapter.DynamicClickListener() {

        @Override
        public void circleClick(int position) {
            //跳到圈子首页
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityCircleDetail.startActivity(getActivity(),String.valueOf(listBean.getPlatformId()));
            }catch (Exception e){

            }
        }

        @Override
        public void contentDynamicClick(int position) {
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityPostDetail.startActivity(getActivity(),String.valueOf(listBean.getId()));
            }catch (Exception e){

            }
        }

        @Override
        public void headClick(int position) {
            //跳往个人主页
            try {
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                PerSonHomePageActivity.startActivity(getActivity(), String.valueOf(listBean.getUserId()));
            }catch (Exception e){

            }
        }

        @Override
        public void praiseClick(int position) {
            try {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(getActivity());
                    return;
                }
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                if(!listBean.isIsLike())
                praiseRequest(String.valueOf(listBean.getId()),position,String.valueOf(listBean.getPlatformId()));
            }catch (Exception e){

            }
        }

        @Override
        public void commentClick(int position) {
            //跳往动态详情，并且定位到评论区域
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(position);
                ActivityPostDetail.startActivity(getActivity(),String.valueOf(listBean.getId()),"comment");
            }catch (Exception e){

            }
        }

        @Override
        public void imageDynamicClick(int index,int pos) {

            //不同类型跳往不同页面
            //普通动态跳往图片预览，书籍跳往网页走url识别，周报跳往周报网页
            try{
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)mdatalist.get(pos);
                if( listBean.getPostType() == 3){
                    //书籍页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        //是否开启了自动转码
                        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
                            UrlReadActivity.startActivityOutsideRead(getActivity(), String.valueOf(circleBookBean.getBookId()),circleBookBean.getSource(),false,"",circleBookBean.getChapterName(),0,false);
                        }else {
                            ActivityWebView.startActivityForReadTitle(getActivity(),circleBookBean.getUrl(),circleBookBean.getBookId(),circleBookBean.getChapterName(),circleBookBean.getSource(),false,0);
                        }
                    }catch (Exception e){
                    }
                }else if( listBean.getPostType() == 2){
                    //内站书籍页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        UrlReadActivity.startActivityInsideRead(getActivity(), String.valueOf(circleBookBean.getBookId()),circleBookBean.getSource(),false,"",circleBookBean.getChapterName(),0,false);
                    }catch (Exception e){
                    }
                }else if(listBean.getPostType() == 4 || listBean.getPostType() == 5 || listBean.getPostType() == 6|| listBean.getPostType() == 7){
                    //周报等页面，跳url
                    try{
                        CircleBookBean circleBookBean = GsonUtil.getBean(listBean.getPicturesShow(),CircleBookBean.class);
                        ActivityWebView.startActivity(getActivity(),circleBookBean.getUrl(),listBean.getTitleShow(),listBean.getContentShow(),circleBookBean.getCover(),2);
                    }catch (Exception e){
                    }
                }else {
                    //普通动态
                    ArrayList<String> listtemp = new ArrayList<String>();
                    listtemp.addAll(listBean.getImages());
                    ActivityImagesPreview.startActivity(getActivity(),listtemp,index);
                }
            }catch (Exception e){
            }
        }

        @Override
        public void moreOptionClick(int position,View view) {

            if(!RxLoginTool.isLogin()){
                ActivityLoginNew.startActivity(getActivity());
                return;
            }

            if(null == moreOptionPopup) {
                moreOptionPopup = new MoreOptionPopup(getActivity());
            }
            moreOptionPopup.getTv_report().setOnClickListener(
                    v -> {
                        try {
                            //举报5：圈子动态 6：圈子评论 7：圈子回复
                            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                            againstCircleUser("5",String.valueOf(listBean.getId()));
                        }catch (Exception e){

                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.getTv_delete().setOnClickListener(
                    v -> {
                        try {
                            //删除
                            CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("contentType", 1);
                            jsonObject.addProperty("objectId", listBean.getId());
                            requestDelete(jsonObject.toString(),position);
                        }catch (Exception e){

                        }
                        moreOptionPopup.dismiss();
                    }
            );
            moreOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //隐藏蒙层
                    mask_view.setVisibility(View.GONE);
                }
            });
            //显示蒙层
            mask_view.setVisibility(View.VISIBLE);

            try {
                //区分是自己还是别人的动态
                CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean) mdatalist.get(position);
                AccountDetailBean account = RxTool.getAccountBean();
                if(null != account){
                    if(account.getData().getUid().equals(String.valueOf(listBean.getUserId()))){
                        moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        moreOptionPopup.getTv_report().setVisibility(View.GONE);
                    }else {
                        moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                        moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                    }
                }else {
                    moreOptionPopup.getTv_delete().setVisibility(View.GONE);
                    moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            moreOptionPopup.showPopupWindow(getActivity(),view);
        }

        @Override
        public void closeClick(int position) {
            if (adForExampleAdapter != null) {
                adForExampleAdapter.removeADView(position);
                adCount -- ;
            }
        }
    };

    /*动态点赞*/
    public void praiseRequest(String objectId,int postion,String circleId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("likeType",1);
        jsonObject.addProperty("objectId", objectId);
        jsonObject.addProperty("platformId", circleId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/like", jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            //此处展示金钻消耗toast
                            RxToast.showJinzuanCharge(circleDynamicBean);

                        //更新点赞状态
                        CircleDynamicBean.DataBean.DynamicRespBean listBean = (CircleDynamicBean.DataBean.DynamicRespBean)  mdatalist.get(postion);
                        int count = listBean.getLikeCount();
                        count++;
                        listBean.setIsLike(true);
                        listBean.setLikeCount(count);
                        mdatalist.set(postion,listBean);
                        adForExampleAdapter.replaceData(mdatalist);
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除动态
     */
    private void requestDelete(String json,int postion) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/delete",json, new CallBackUtil.CallBackString() {
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
                        adForExampleAdapter.remove(postion);
                        RxToast.custom("删除成功").show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }


    /*广点通广告开始*/

    public HashMap<NativeExpressADView, Integer> getmAdViewPositionMap() {
        return adForExampleAdapter.getmAdViewPositionMap();
    }

    public void setmAdViewPositionMap(HashMap<NativeExpressADView, Integer> mAdViewPositionMap) {
        adForExampleAdapter.setmAdViewPositionMap(mAdViewPositionMap);
    }

    /**
     * 如果选择支持视频的模版样式，请使用
     * {Constants#NativeExpressSupportVideoPosID}
     */
    private void initNativeExpressAD() {
        if(null == sdkAttrs){
            sdkAttrs = new ArrayList<>();
        }
        if(null == ownAttrs){
            ownAttrs = new ArrayList<>();
        }
        sdkAttrs.clear();
        ownAttrs.clear();

        for(BannerBean.DataBean item :attrs){
            if(!TextUtils.isEmpty(item.getSdkChannel())){
                sdkAttrs.add(item);
            }else {
                ownAttrs.add(item);
            }
        }

        int sdkSize = sdkAttrs.size();
        if(sdkSize > 0){
            ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
            mADManager = new NativeExpressAD(getActivity(), adSize, Constant.TENCENTAPPID,Constant.TENCENTPOSTID2, this);
            mADManager.loadAD(sdkSize);
        }else {
            try{
                for(BannerBean.DataBean item :ownAttrs){
                    int position = item.getPutPosition();
                    adForExampleAdapter.addADToPosition(position + adCount,item);
                    adCount ++;
                }
                ownAttrs.clear();
            }catch (Exception e){
            }
            adForExampleAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        mAdViewList = list;
        try{
            for (int i = 0; i < mAdViewList.size(); i++) {
                int position = sdkAttrs.get(i).getPutPosition();
                    NativeExpressADView view = mAdViewList.get(i);
                    int configId = sdkAttrs.get(i).getConfigId();
                    if (view.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        view.setMediaListener(mediaListener);
                    }
                    //存储广告视图和id的键值对
                    mAdViewIdMap.put(view,configId);
                if (position  + adCount >  mdatalist.size()) {
                    position  =  mdatalist.size() - adCount;
                }
                    getmAdViewPositionMap().put(view, position + adCount); // 把每个广告在列表中位置记录下来
                    adForExampleAdapter.addADViewToPosition(position + adCount, view);
                    adCount ++;
            }
        }catch (Exception  e){

        }

        try{
            for(BannerBean.DataBean item :ownAttrs){
                int position = item.getPutPosition();
                adForExampleAdapter.addADToPosition(position + adCount,item);
                adCount ++;
            }
            ownAttrs.clear();
        }catch (Exception e){

        }

        adForExampleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        /*此处需要调用接口，告诉后台*/
        if(null != mAdViewIdMap){
            int tempConfigId = mAdViewIdMap.get(nativeExpressADView);
            saveStartAdvertisementStatistics(String.valueOf(tempConfigId));
        }
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        if (adForExampleAdapter != null) {
            int removedPosition = getmAdViewPositionMap().get(nativeExpressADView);
            adForExampleAdapter.removeADView(removedPosition);
            adCount -- ;
        }
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {
        try{
            for(BannerBean.DataBean item :ownAttrs){
                int position = item.getPutPosition();
                adForExampleAdapter.addADToPosition(position + adCount,item);
                adCount ++;
            }
            ownAttrs.clear();
        }catch (Exception e){
        }
        adForExampleAdapter.notifyDataSetChanged();
    }
    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ")
                        .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }
    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition());
            return videoBuilder.toString();
        }
        return null;
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
        }
    };
    /*广点通广告结束*/



    /**
     * 请求统计点击接口
     * 类型 1浏览 2点击
     */
    private void saveStartAdvertisementStatistics(String configId) {
        HashMap map = new HashMap();
        map.put("configId", configId);
        map.put("type", "2");
        map.put("num", "1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/startup/saveStartAdvertisementStatistics", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }


    /**
     * 举报圈子用户
     */
    private void againstCircleUser(String source,String userId){
        HashMap map=new HashMap();
        map.put("source",source);
        map.put("targetId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/against/circleUser",map, new CallBackUtil.CallBackString() {
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
                        RxToast.custom("举报成功").show();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
