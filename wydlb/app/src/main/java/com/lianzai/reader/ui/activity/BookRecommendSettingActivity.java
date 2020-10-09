package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.HobbySettingResponse;
import com.lianzai.reader.bean.TagsResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.BookCategoryGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Copyright (C), 2018
 * FileName: SplashActivity
 * Author: lrz
 * Date: 2018/9/28 22:08
 * Description: 首次进来喜好设置
 */
public class BookRecommendSettingActivity extends BaseActivity {


    @Bind(R.id.iv_boy_selected)
    ImageView iv_boy_selected;

    @Bind(R.id.iv_girl_selected)
    ImageView iv_girl_selected;

    @Bind(R.id.tv_boy)
    TextView tv_boy;

    @Bind(R.id.tv_girl)
    TextView tv_girl;

    @Bind(R.id.bt_next)
    Button bt_next;

    @Bind(R.id.iv_back_step)
    ImageView iv_back_step;

    @Bind(R.id.iv_boy)
    ImageView iv_boy;

    @Bind(R.id.iv_girl)
    ImageView iv_girl;


    @Bind(R.id.rl_boy)
    RelativeLayout rl_boy;

    @Bind(R.id.ll_male_choice)
    LinearLayout ll_male_choice;

    @Bind(R.id.rl_girl)
    RelativeLayout rl_girl;

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    BookCategoryGridAdapter bookCategoryGridAdapter;

    List<TagsResponse.DataBean> categoryList=new ArrayList<>();

    @Bind(R.id.ll_category)
    LinearLayout ll_category;

    @Bind(R.id.ll_male)
    LinearLayout ll_male;

    @Bind(R.id.btn_finish)//选好了，开启阅读之旅
            Button btn_finish;

    int step=0;


    boolean isBoy=false;

    String[] hobbys;

    HobbySettingResponse hobbySettingResponse;

    boolean firstShow=false;

    public static void startActivity(boolean firstShow, Activity activity){
        Intent intent=new Intent(activity,BookRecommendSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle=new Bundle();
        bundle.putBoolean("firstShow",firstShow);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_book_recommend_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (null!=getIntent().getExtras()){
            firstShow=getIntent().getExtras().getBoolean("firstShow");
        }

        bookCategoryGridAdapter=new BookCategoryGridAdapter(categoryList,this);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        rv_data.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(10), RxImageTool.dip2px(10),0));
        rv_data.setLayoutManager(gridLayoutManager);

        rv_data.setAdapter(bookCategoryGridAdapter);

        bookCategoryGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bookCategoryGridAdapter.getMap().put(position,bookCategoryGridAdapter.getMap().get(position)?false:true);
                bookCategoryGridAdapter.notifyItemChanged(position);

                if (bookCategoryGridAdapter.getSelectCount()>0){
                    btn_finish.setEnabled(true);
                    btn_finish.setBackgroundResource(isBoy?R.drawable.v2_blue_30corner_bg:R.drawable.v2_pink_30corner_bg);
                }else{
                    btn_finish.setEnabled(false);
                    btn_finish.setBackgroundResource(R.drawable.v2_gray_30corner_bg);
                }
            }
        });


        rl_boy.post(new Runnable() {
            @Override
            public void run() {
                if(null != rl_boy) {
                    ObjectAnimator translateAnim = ObjectAnimator.ofFloat(rl_boy, "translationX", -rl_boy.getMeasuredWidth(), 0);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(translateAnim);

                    animatorSet.setDuration(500);
                    animatorSet.setStartDelay(0);
                    animatorSet.start();
                }
            }
        });


        rl_girl.post(new Runnable() {
            @Override
            public void run() {
                if(null != rl_girl) {
                    ObjectAnimator translateAnim1 = ObjectAnimator.ofFloat(rl_girl, "translationX", ScreenUtil.getDisplayWidth(), 0);
                    AnimatorSet animatorSet1 = new AnimatorSet();
                    animatorSet1.play(translateAnim1);
                    translateAnim1.setStartDelay(0);

                    animatorSet1.setDuration(500);
                    animatorSet1.start();
                }
            }
        });

        if (!firstShow){//从其他页面进来的
            btn_finish.setText("保存设置");
        }

        //喜好设置请求
        hobbyDetailRequest();

    }



    @OnClick(R.id.tv_exit)void exitClick(){
        backClick();
    }

    @OnClick(R.id.rl_boy)void rlBoyClick(){
        isBoy=true;

        iv_boy_selected.setVisibility(View.VISIBLE);
        iv_girl_selected.setVisibility(View.GONE);

        startScaleAnimation(iv_boy);
        startScaleAnimation(iv_boy_selected);

        tv_boy.setBackgroundResource(R.drawable.sex_corner_bg_selected);
        tv_girl.setBackgroundResource(R.drawable.sex_corner_bg);

        tv_boy.setTextColor(getResources().getColor(R.color.white));
        tv_girl.setTextColor(getResources().getColor(R.color.color_black_ff666666));


        bt_next.setEnabled(true);
        bt_next.setBackgroundResource(R.drawable.v2_blue_30corner_bg);

        //标签请求刷新
        bookListTagsRequest();

        bookCategoryGridAdapter.setBoy(true);
        bookCategoryGridAdapter.notifyDataSetChanged();

    }

    private void startScaleAnimation(View view ){
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.8f,1f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.8f,1f);

        AnimatorSet animatorSet1=new AnimatorSet();
        animatorSet1.play(scaleXAnim).with(scaleYAnim);

        animatorSet1.setDuration(500);
        animatorSet1.start();
    }

    @OnClick(R.id.rl_girl)void rlGirlClick(){
        isBoy=false;
        iv_boy_selected.setVisibility(View.GONE);
        iv_girl_selected.setVisibility(View.VISIBLE);


        startScaleAnimation(iv_girl);
        startScaleAnimation(iv_girl_selected);


        tv_girl.setBackgroundResource(R.drawable.girl_corner_bg);
        tv_boy.setBackgroundResource(R.drawable.sex_corner_bg);

        tv_girl.setTextColor(getResources().getColor(R.color.white));
        tv_boy.setTextColor(getResources().getColor(R.color.color_black_ff666666));

        bt_next.setEnabled(true);

        bt_next.setBackgroundResource(R.drawable.v2_pink_30corner_bg);

        //标签请求刷新
        bookListTagsRequest();

        bookCategoryGridAdapter.setBoy(false);
        bookCategoryGridAdapter.notifyDataSetChanged();


    }

    private void showFinishBtn(){
        if (bookCategoryGridAdapter.getSelectCount()>0){
            btn_finish.setEnabled(true);
            btn_finish.setBackgroundResource(isBoy?R.drawable.v2_blue_30corner_bg:R.drawable.v2_pink_30corner_bg);
        }else{
            btn_finish.setEnabled(false);
            btn_finish.setBackgroundResource(R.drawable.v2_gray_30corner_bg);
        }
    }

    @OnClick(R.id.bt_next)void nextStepClick(){
        iv_back_step.setVisibility(View.VISIBLE);

        step=1;


        rv_data.setVisibility(View.VISIBLE);
        ll_category.setVisibility(View.VISIBLE);
        btn_finish.setVisibility(View.VISIBLE);

        showFinishBtn();

        for(int i=0;i<3;i++){
            View view=null;
            int duration=500;

            if (i==0){
                view=rv_data;
            }else if(i==1){
                view=ll_category;
                duration=800;
            }else if(i==2){
                view=btn_finish;
                duration=700;
            }

            ObjectAnimator translateAnimIn = ObjectAnimator.ofFloat(view, "translationX", ScreenUtil.getDisplayWidth(),0);
            AnimatorSet animatorSetIn=new AnimatorSet();
            animatorSetIn.play(translateAnimIn);

            animatorSetIn.setDuration(duration);
            animatorSetIn.start();
        }



        for(int i=0;i<3;i++){
            View view=null;
            int duration=500;
            if (i==0){
                view=ll_male_choice;
            }else if(i==1){
                view=ll_male;
                duration=800;
            }else if(i==2){
                view=bt_next;
                duration=700;
            }

            ObjectAnimator translateAnimOut = ObjectAnimator.ofFloat(view, "translationX", -ll_male_choice.getMeasuredWidth());
            AnimatorSet animatorSetOut=new AnimatorSet();
            animatorSetOut.play(translateAnimOut);

            animatorSetOut.setDuration(duration);
            animatorSetOut.start();
        }

    }

    @OnClick(R.id.iv_back_step)void backStepClick(){
        iv_back_step.setVisibility(View.GONE);
        step=0;

        for(int i=0;i<3;i++){
            View view=null;
            int duration=500;
            if (i==0){
                view=rv_data;
            }else if(i==1){
                view=ll_category;
                duration=800;
            }else if(i==2){
                view=btn_finish;
                duration=700;
            }

            ObjectAnimator translateAnimIn = ObjectAnimator.ofFloat(view, "translationX", 0,ScreenUtil.getDisplayWidth());
            AnimatorSet animatorSetIn=new AnimatorSet();
            animatorSetIn.play(translateAnimIn);

            animatorSetIn.setDuration(duration);
            animatorSetIn.start();

        }



        for(int i=0;i<3;i++){

            View view=null;
            int duration=500;
            if (i==0){
                view=ll_male_choice;
            }else if(i==1){
                view=ll_male;
                duration=800;
            }else if(i==2){
                view=bt_next;
                duration=700;
            }

            ObjectAnimator translateAnimOut = ObjectAnimator.ofFloat(view, "translationX", 0);
            AnimatorSet animatorSetOut=new AnimatorSet();
            animatorSetOut.play(translateAnimOut);

            animatorSetOut.setDuration(duration);
            animatorSetOut.start();
        }
    }

    @OnClick(R.id.btn_finish)void finishClick(){//选好了-开始阅读之旅-跳转到为您推荐
        if (TextUtils.isEmpty(bookCategoryGridAdapter.getSelectIds())){
            RxToast.custom("至少得选择一个标签哦").show();
            return;
        }
        hobbySaveRequest(bookCategoryGridAdapter.getSelectIds());//保存喜好设置

        if (firstShow){
            ActivityBookList.startActivity(this, Constant.BookListType.RECOMMEND_FOR_TOURIST_TYPE,bookCategoryGridAdapter.getSelectIds());
        }else{
            finish();
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            if (step!=0){
                backStepClick();
                return true;
            }else{
                //禁止后退
                if (firstShow){
                    return  false;
                }else{
                    super.onKeyDown(keyCode,event);
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void bookListTagsRequest(){
        Map<String,String>map=new HashMap<>();
        map.put("sex",isBoy?"1":"2");//1男 2女
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/tag", map,new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try{
                    TagsResponse tagsResponse= GsonUtil.getBean(response,TagsResponse.class);

                    if (tagsResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        categoryList.clear();
                        categoryList.addAll(tagsResponse.getData());

                        bookCategoryGridAdapter.replaceData(categoryList);
                        bookCategoryGridAdapter.initMap();

                        if (null!=hobbys){//选中之前的标签
                            for(int i=0;i<bookCategoryGridAdapter.getData().size();i++){
                                for(String id:hobbys){
                                    if (id.equals(String.valueOf(bookCategoryGridAdapter.getData().get(i).getId()))){
                                        RxLogTool.e("initMap pos:"+i);
                                        bookCategoryGridAdapter.getMap().put(i,true);
                                        bookCategoryGridAdapter.notifyItemChanged(i);
                                    }
                                }
                            }
                        }

                    }else{
                        RxToast.custom(tagsResponse.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        RxLogTool.e("eventBusNotification.......");
        if (event.getTag() == Constant.EventTag.HOBBY_SETTING_CLOSE_TAG) {
            finish();
        }
    }

    /**
     * 阅读喜好设置获取
     */
    private void hobbyDetailRequest(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/tag/like", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try{
                    hobbySettingResponse=GsonUtil.getBean(response,HobbySettingResponse.class);
                    if (hobbySettingResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        String data=hobbySettingResponse.getData();
                        if (!TextUtils.isEmpty(data)){
                            if (data.contains(";")){
                                String[] strs=data.split(";");
                                if (strs[1].contains(",")){
                                    hobbys=strs[1].split(",");
                                }else{
                                    hobbys=new String[]{strs[1]};
                                }

                                if (strs[0].equals("1")){//性别
                                    rlBoyClick();
                                }else{
                                    rlGirlClick();
                                }

                            }else{
                                if (data.contains(",")){
                                    hobbys=data.split(",");
                                }else{
                                    hobbys=new String[]{data};
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 喜好保存设置
     * @param tag
     */
    private void hobbySaveRequest(String tag){
        Map<String,String>map=new HashMap<>();
        map.put("tag",(isBoy?"1;":"2;")+tag);

        RxLogTool.e("hobbySaveRequest:"+map.toString());

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/booklist/tag/like/save", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {

                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("保存成功",Constant.ToastType.TOAST_SUCCESS).show();
                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
