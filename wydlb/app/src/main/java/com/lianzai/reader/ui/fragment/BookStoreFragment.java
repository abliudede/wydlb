package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.TabEntity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityBookRecordHistoryNew;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.dialog.RxDialogBookStoreChoose;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;
import com.lianzai.reader.view.tab.listener.CustomTabEntity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的书架---包括我的书籍-我的书单
 */

public class BookStoreFragment extends BaseFragment {

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    TabFragmentAdapter tabFragmentAdapter;

    List<Fragment> fragments = new ArrayList<>();

    MyBookFragment myBookFragment;

    BookStoreBookListFragment bookStoreBookListFragment;

    @Bind(R.id.iv_search)
    ImageView iv_search;//搜索

    @Bind(R.id.tv_my_book)
    TextView tv_my_book;

    @Bind(R.id.tv_my_book_list)
    TextView tv_my_book_list;

    @Bind(R.id.view_slide)
    View view_slide;

    @Bind(R.id.tv_history_time)
    TextView tv_history_time;


    @Bind(R.id.view_action_bar)
            RelativeLayout view_action_bar;

    int pageIndex=0;

    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗
    RxDialogBookStoreChoose rxDialogBookStoreChoose;//书籍类型筛选弹框

    @Override
    public void onResume() {
        super.onResume();
        //每次需判断主页是否需要你去书籍tab
        if(activity instanceof  MainActivity){
            if(((MainActivity)activity).isBookStoreTab1()){
                clickTabByIndex(0);
                ((MainActivity)activity).setBookStoreTab1(false);
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.fragment_main_book_store;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {


    }

    @Override
    public void configViews(Bundle savedInstanceState) {

//        int statusBarHeight= ScreenUtil.getStatusBarHeight(getActivity());
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.action_bar_height));
//        layoutParams.setMargins(0,statusBarHeight,0,0);
//        view_action_bar.setLayoutParams(layoutParams);


        fragments.clear();
        myBookFragment=new MyBookFragment();
        bookStoreBookListFragment=new BookStoreBookListFragment();

        fragments.add(myBookFragment);
        fragments.add(bookStoreBookListFragment);

        rxDialogGoLogin = new RxDialogGoLogin(getActivity(), R.style.OptionDialogStyle);
        rxDialogBookStoreChoose = new RxDialogBookStoreChoose(getActivity());
        rxDialogBookStoreChoose.setChoose(1);
        rxDialogBookStoreChoose.getTv1().setOnClickListener(
                v -> {
                    if(null != myBookFragment){
                        myBookFragment.setChoose(1);
                    }
                    rxDialogBookStoreChoose.setChoose(1);
                    rxDialogBookStoreChoose.dismiss();
                }
        );
        rxDialogBookStoreChoose.getTv2().setOnClickListener(
                v -> {
                    if(null != myBookFragment){
                        myBookFragment.setChoose(2);
                    }
                    rxDialogBookStoreChoose.setChoose(2);
                    rxDialogBookStoreChoose.dismiss();
                }
        );
        rxDialogBookStoreChoose.getTv3().setOnClickListener(
                v -> {
                    if(null != myBookFragment){
                        myBookFragment.setChoose(3);
                    }
                    rxDialogBookStoreChoose.setChoose(3);
                    rxDialogBookStoreChoose.dismiss();
                }
        );


        String [] mTitles={"我的书籍","我的书单"};
        ArrayList<CustomTabEntity>mTabEntities=new ArrayList<>();

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],0,0));
        }
        tabFragmentAdapter=new TabFragmentAdapter(getChildFragmentManager());
        tabFragmentAdapter.setTitles(mTitles);
        tabFragmentAdapter.setFragments(fragments);
        viewPager.setAdapter(tabFragmentAdapter);

        tv_my_book.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //此处回调之后，里面对象可能为空

                    tv_my_book.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(null == tv_my_book) return;
                                if(null == view_slide) return;
                                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(tv_my_book.getMeasuredWidth(), RxImageTool.dip2px(1.5f));
                                layoutParams.setMargins(tv_my_book.getLeft() + RxImageTool.dp2px(16),tv_my_book.getTop(),0,0);
                                view_slide.setLayoutParams(layoutParams);
                            }catch (Exception e){

                            }
                        }
                    });
                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv_my_book.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }catch (Exception e){

                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                clickTabByIndex(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_my_book_list.setOnClickListener(
                v->{
                    clickTabByIndex(1);
                }
        );

        tv_my_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageIndex == 0) {
                    //已经在我的书籍tab时，弹出选项框
                    rxDialogBookStoreChoose.show();
                }
                clickTabByIndex(0);
            }
        });

        //请求阅读时长
        refreshReadTime();

    }

    private void refreshReadTime(){
        RxLogTool.e("refreshReadTime:"+RxLoginTool.isLogin());
        if (RxLoginTool.isLogin()){
            tv_history_time.setVisibility(View.VISIBLE);
            //阅读时长请求
            requestReadTotalTime();
        }else{
            tv_history_time.setVisibility(View.GONE);
        }
    }

    /**
     * 导航栏切换
     * @param index
     */
    private void clickTabByIndex(int index){
        if (pageIndex==index)return;

        pageIndex=index;

        int translateX=0;

        if (pageIndex==0){
            tv_my_book.setTextColor(getResources().getColor(R.color.normal_text_color));
            tv_my_book_list.setTextColor(getResources().getColor(R.color.color_black_ff999999));
        }else if(pageIndex==1){
            tv_my_book.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_my_book_list.setTextColor(getResources().getColor(R.color.normal_text_color));
            translateX=tv_my_book.getMeasuredWidth()+ RxImageTool.dip2px(30f);
        }

        viewPager.setCurrentItem(pageIndex);

        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view_slide, "translationX", translateX);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(translateAnim);

        animatorSet.setDuration(300);
        animatorSet.start();
    }


    @Override
    public void updateViews() {
        super.updateViews();
        tabFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.iv_search)void searchClick(){
        ActivitySearchFirst.startActivity(getActivity());
    }

    @OnClick(R.id.iv_read)void readClick(){
        if (RxLoginTool.isLogin()){
            ActivityBookRecordHistoryNew.startActivity(getContext());
        }else{
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        ActivityLoginNew.startActivity(getActivity());
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    @Override
    public void fetchData() {

    }

    @OnClick(R.id.tv_history_time)void readHistoryClick(){
        if (RxLoginTool.isLogin()){
            ActivityBookRecordHistoryNew.startActivity(getContext());
        }else{
            //弹登录提示
            rxDialogGoLogin.getTv_sure().setOnClickListener(
                    view -> {
                        rxDialogGoLogin.dismiss();
                        //直接跳登录页，不关闭之前页面
                        ActivityLoginNew.startActivity(getActivity());
                    }
            );
            rxDialogGoLogin.show();
        }
    }

    public void requestReadTotalTime(){

        if (RxLoginTool.isLogin()){
            OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/read/history/total", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {

                }

                @Override
                public void onResponse(String response) {
                    RxLogTool.e("BookStoreFragment requestReadTotalTime...."+RxLoginTool.isLogin());
                    try{
                        org.json.JSONObject jsonObject=new org.json.JSONObject(response);
                        if (jsonObject.getInt("code")==Constant.ResponseCodeStatus.SUCCESS_CODE){
                            int totalReadTime=jsonObject.getInt("data");
                            RxSharedPreferencesUtil.getInstance().putInt("readTotalTime",totalReadTime);
                            tv_history_time.setText(totalReadTime+"分钟");
                        }
                    }catch (Exception e){
                        RxLogTool.e("requestReadTotalTime:"+e.getMessage());
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if(event.getTag()==Constant.EventTag.LOGIN_REFRESH_TAG){//登录刷新
            if (RxLoginTool.isLogin()){
                refreshReadTime();//阅读时长刷新
            }
        }else if(event.getTag()==Constant.EventTag.READTIME_REFRESH_TAG){//主动动作刷新
            if (RxLoginTool.isLogin()){
                refreshReadTime();//阅读时长刷新
            }
        }else if(event.getTag()==Constant.EventTag.LOGIN_OUT_REFRESH_TAG){//退出登录成功
            tv_history_time.setVisibility(View.GONE);//隐藏阅读时长
        }
    }

}
