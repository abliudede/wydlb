package com.wydlb.first.ui.activity.location;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.ui.adapter.LocationItemAdapter;
import com.wydlb.first.utils.DividerItemDecoration;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Copyright (C), 2019
 * FileName: ActivitySelectLocation
 * Author: lrz
 * Date: 2019/3/9 11:21
 * Description: ${DESCRIPTION}
 */
public class ActivitySelectLocation extends BaseActivity implements PoiSearch.OnPoiSearchListener{
    @Bind(R.id.tv_common_title)
    TextView tv_common_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    CustomLoadMoreView customLoadMoreView;

    @Bind(R.id.tv_not_found_location)
    TextView tv_not_found_location;

    @Bind(R.id.view_loading)
    View view_loading;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    AMapLocationClientOption option=new AMapLocationClientOption();

    PoiSearch.Query query;

    PoiSearch poiSearch;

    LocationItemAdapter locationItemAdapter;

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    List<PoiItem> dataList=new ArrayList<>();

    @Bind(R.id.ed_search_keyword)
    EditText ed_search_keyword;

    int pageNum=1;
    int pageSize=10;

    double longitude=0;
    double latitude=0;

    String keyWord="";
    String startKeyWord="";//其实位置
    String cityCode="";
    String cityName="";

    boolean isRefresh=true;

    View notSelectedLocationView;

    ImageView iv_select_status;

    String selectedAddress;

    public static void startActivity(Activity activity, String address){
        Intent intent=new Intent(activity,ActivitySelectLocation.class);
        Bundle bundle=new Bundle();
        bundle.putString("address",address);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,1001);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_location;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        tv_common_title.setText("选择地点");
        tv_options.setVisibility(View.GONE);

        selectedAddress=getIntent().getExtras().getString("address");

        notSelectedLocationView= LayoutInflater.from(this).inflate(R.layout.view_header_location,null);
        iv_select_status=notSelectedLocationView.findViewById(R.id.iv_select_status);


        locationItemAdapter=new LocationItemAdapter(dataList);
        customLoadMoreView = new CustomLoadMoreView();
        locationItemAdapter.setLoadMoreView(customLoadMoreView);
        locationItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                isRefresh=false;
                nearSearchByKeyword(keyWord);

            }
        }, rv_data);

        locationItemAdapter.addHeaderView(notSelectedLocationView);
        locationItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent=new Intent();
                PoiItem poiItem=dataList.get(i);
                String addr=poiItem.getCityName()+poiItem.getAdName()+(!TextUtils.isEmpty(poiItem.getSnippet())?poiItem.getSnippet():poiItem.getTitle());
                intent.putExtra("address",addr);
                intent.putExtra("latitude",poiItem.getLatLonPoint().getLatitude());
                intent.putExtra("longitude",poiItem.getLatLonPoint().getLongitude());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        notSelectedLocationView.setOnClickListener(
                v->{
                    Intent intent=new Intent();
                    intent.putExtra("address","");
                    setResult(RESULT_OK,intent);
                    finish();
                }
        );

        RxLinearLayoutManager manager = new RxLinearLayoutManager(this);
        rv_data.setLayoutManager(manager);
        rv_data.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        rv_data.setAdapter(locationItemAdapter);

        ed_search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isRefresh=true;

                if (!TextUtils.isEmpty(editable.toString())){
                    keyWord=editable.toString();
                    nearSearchByKeyword(keyWord);
                }else{
                    nearSearchByKeyword(startKeyWord);
                }
            }
        });


        initLocations();


    }


    /**
     * 初始化定位
     */
    private void initLocations() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        option.setInterval(1000);
        option.setNeedAddress(true);


        if(null != mLocationClient){
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        view_loading.setVisibility(View.VISIBLE);
    }

    private void nearSearchByKeyword(String searchKeyword){

        RxLogTool.e("nearSearchByKeyword keyWord:"+searchKeyword);
        query = new PoiSearch.Query(searchKeyword, "", cityCode);//120203 楼宇

        query.setPageSize(pageSize);
        query.setPageNum(pageNum);

        poiSearch=new PoiSearch(this,query);
        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,longitude),5000));
        poiSearch.searchPOIAsyn();
    }



    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    latitude=amapLocation.getLatitude();
                    longitude=amapLocation.getLongitude();
                    cityCode=amapLocation.getCityCode();
                    cityName=amapLocation.getCity();
                    keyWord=amapLocation.getAoiName();
                    startKeyWord=keyWord;

                    nearSearchByKeyword(keyWord);

                    //可在其中解析amapLocation获取相应内容。
                    RxLogTool.e("city:"+amapLocation.getCity());
                    RxLogTool.e("district:"+amapLocation.getDistrict());
                    RxLogTool.e("street:"+amapLocation.getStreet());
                    RxLogTool.e("street num:"+amapLocation.getStreetNum());

                    RxLogTool.e("bearing:"+amapLocation.getBearing());
                    RxLogTool.e("poiname:"+amapLocation.getPoiName());
                    RxLogTool.e("aoiname:"+amapLocation.getAoiName());

                    RxLogTool.e("buildid:"+amapLocation.getBuildingId());
                    RxLogTool.e("description:"+amapLocation.getDescription());
                    RxLogTool.e("address:"+amapLocation.getAddress());
                    RxLogTool.e("provider:"+amapLocation.getProvider());
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    RxLogTool.e("AmapError location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    public void onPoiSearched(PoiResult poiResult, int errorCode) {
        RxLogTool.e("onPoiSearched errorCode:"+errorCode);

        view_loading.setVisibility(View.GONE);

        if (errorCode==1000){
            tv_not_found_location.setVisibility(View.GONE);

            ArrayList<PoiItem> poiItems=poiResult.getPois();

            RxLogTool.e("onPoiSearched size:"+poiItems.size());
            if (!isRefresh){
                if (poiItems.size()<pageSize){
                    locationItemAdapter.loadMoreEnd();
                }else{
                    locationItemAdapter.loadMoreComplete();
                }
            }
            if (poiItems.size()>0){
                RxLogTool.e("poiResult size:"+poiItems.size());
                if (isRefresh){
                    dataList.clear();
                    dataList.addAll(poiItems);
                }else{
                    dataList.addAll(poiItems);
                }

                locationItemAdapter.notifyDataSetChanged();

                getCurrentPosition();
            }
            if (isRefresh&&poiItems.size()==0){//未搜索到相关位置
                dataList.clear();
                locationItemAdapter.notifyDataSetChanged();
                tv_not_found_location.setVisibility(View.VISIBLE);
            }
        }else{
            tv_not_found_location.setVisibility(View.VISIBLE);
            tv_not_found_location.setText("位置获取失败，请检查是否开启定位服务");
//            RxToast.custom("附近位置获取失败").show();
        }

    }

    private void getCurrentPosition(){
        if (TextUtils.isEmpty(selectedAddress)){//selectedaddress
            iv_select_status.setVisibility(View.VISIBLE);
            notSelectedLocationView.setBackgroundColor(Color.parseColor("#193865FE"));
        }else{
            iv_select_status.setVisibility(View.GONE);
            notSelectedLocationView.setBackgroundColor(Color.parseColor("#ffffff"));
            for(int i=0;i<dataList.size();i++){
                PoiItem poiItem=dataList.get(i);
                String itemAddr=poiItem.getCityName()+poiItem.getAdName()+(!TextUtils.isEmpty(poiItem.getSnippet())?poiItem.getSnippet():poiItem.getTitle());
                if (null!=itemAddr&&itemAddr.equals(selectedAddress)){
                    RxLogTool.e("itemAddr:"+itemAddr);
                    locationItemAdapter.setCurrentSelectPosition(i+locationItemAdapter.getHeaderLayoutCount());
                    break;
                }

            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int errorCode) {

        RxLogTool.e("onPoiItemSearched:"+errorCode);
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

}
