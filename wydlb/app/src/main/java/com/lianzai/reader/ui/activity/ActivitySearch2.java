package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.fragment.BookShortageFragment;
import com.lianzai.reader.ui.fragment.SearchBookFragment;
import com.lianzai.reader.ui.fragment.SearchBookListFragment;
import com.lianzai.reader.ui.fragment.SearchIntegrateFragment;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBookFeedback;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 搜索
 */

public class ActivitySearch2 extends BaseActivity {

    @Bind(R.id.ed_search_keyword)
    EditText ed_search_keyword;

    @Bind(R.id.iv_clear)
    ImageView iv_clear;

    @Bind(R.id.rg_tab)
    RadioGroup rg_tab;

    @Bind(R.id.rb_all)
    RadioButton rb_all;

    @Bind(R.id.rb_book)
    RadioButton rb_book;

    @Bind(R.id.rb_book_list)
    RadioButton rb_book_list;

//    @Bind(R.id.rb_book_web)
//    RadioButton rb_book_web;

    @Bind(R.id.rb_book_short)
    RadioButton rb_book_short;



    @Bind(R.id.frame_layout)
    FrameLayout frame_layout;



    SearchBookFragment searchBookFragment;

    SearchBookListFragment searchBookListFragment;

    SearchIntegrateFragment searchIntegrateFragment;

    BookShortageFragment bookShortageFragment;

    FragmentManager fm;


    @Bind(R.id.iv_book_feedback)
    ImageView iv_book_feedback;

    RxDialogBookFeedback rxDialogBookFeedback;


    @Bind(R.id.rl_guide)
    RelativeLayout rl_guide;//首次搜索引导图层
    private String search;

    private boolean first = true;
    private int index;
    private boolean bookshortage;

    public static void startActivity(Context context, String search) {
        Bundle bundle = new Bundle();
        bundle.putString("search", search);
        RxActivityTool.skipActivity(context, ActivitySearch2.class, bundle);
    }
    public static void startActivityToBookShortage(Context context, String search) {
        Bundle bundle = new Bundle();
        bundle.putString("search", search);
        bundle.putBoolean("bookshortage", true);
        RxActivityTool.skipActivity(context, ActivitySearch2.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_book2;
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


        try{
            search = getIntent().getExtras().getString("search");
            if(!TextUtils.isEmpty(search)) {
                ed_search_keyword.setText(search);
            }
        }catch (Exception e){
        }

        try{
            bookshortage = getIntent().getExtras().getBoolean("bookshortage",false);
        }catch (Exception e){
        }

        fm = getSupportFragmentManager();
        initViewPager();

        ed_search_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    iv_clear.setVisibility(View.GONE);
                }else{//模糊匹配
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }
        });

        ed_search_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String keyWord = getKeyword();
                    if(!TextUtils.isEmpty(keyWord)) {
                        //刷新数据
                        doSearchRequest(false);
                    }else {
                        RxToast.custom("没有搜索关键词").show();
                    }
                }
                return false;
            }
        });


        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id== R.id.rb_all){
                    setSelected(0);
                    rb_all.getPaint().setFakeBoldText(true);
                    rb_book.getPaint().setFakeBoldText(false);
                    rb_book_list.getPaint().setFakeBoldText(false);
//                    rb_book_web.getPaint().setFakeBoldText(false);
                    rb_book_short.getPaint().setFakeBoldText(false);

                }else if(id== R.id.rb_book){
                    setSelected(1);
                    rb_all.getPaint().setFakeBoldText(false);
                    rb_book.getPaint().setFakeBoldText(true);
                    rb_book_list.getPaint().setFakeBoldText(false);
//                    rb_book_web.getPaint().setFakeBoldText(false);
                    rb_book_short.getPaint().setFakeBoldText(false);
                }else if(id== R.id.rb_book_list){
                    setSelected(2);
                    rb_all.getPaint().setFakeBoldText(false);
                    rb_book.getPaint().setFakeBoldText(false);
                    rb_book_list.getPaint().setFakeBoldText(true);
//                    rb_book_web.getPaint().setFakeBoldText(false);
                    rb_book_short.getPaint().setFakeBoldText(false);
                }
//                else if(id== R.id.rb_book_web){
//                    setSelected(3);
//                    rb_all.getPaint().setFakeBoldText(false);
//                    rb_book.getPaint().setFakeBoldText(false);
//                    rb_book_list.getPaint().setFakeBoldText(false);
//                    rb_book_web.getPaint().setFakeBoldText(true);
//                    rb_book_short.getPaint().setFakeBoldText(false);
//                }
                else if(id== R.id.rb_book_short){
                    setSelected(3);
                    rb_all.getPaint().setFakeBoldText(false);
                    rb_book.getPaint().setFakeBoldText(false);
                    rb_book_list.getPaint().setFakeBoldText(false);
//                    rb_book_web.getPaint().setFakeBoldText(false);
                    rb_book_short.getPaint().setFakeBoldText(true);
                }
            }
        });

        //默认选中
        if(bookshortage){
            changeTab(3);
        }else {
            changeTab(0);
        }

    }

    private void doSearchRequest(boolean first){
        String keyword=getKeyword();

        if (!TextUtils.isEmpty(keyword)){
            if(null != searchIntegrateFragment) {
                searchIntegrateFragment.searchIntegrateRequest(keyword);
            }
            if(null != searchBookFragment) {
                searchBookFragment.bookListRequest(keyword, true);
            }
            if(null != searchBookListFragment) {
                searchBookListFragment.bookListRequest(keyword, true);
            }
            if(null != bookShortageFragment) {
                bookShortageFragment.bookListRequest(keyword);
            }

            //隐藏键盘
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(null != ed_search_keyword) {
                            RxKeyboardTool.hideKeyboard(ActivitySearch2.this, ed_search_keyword);
                        }
                    }catch (Exception e){

                    }
                }
            },200);
        }
    }

    public String getKeyword(){
        if (null==ed_search_keyword){
            ed_search_keyword=findViewById(R.id.ed_search_keyword);
        }
        return  ed_search_keyword.getText().toString().trim();
    }

    private void initViewPager(){
            //展示全部的情况
            //默认加粗
            rb_all.getPaint().setFakeBoldText(true);
            rb_all.setVisibility(View.VISIBLE);
            rb_book.setVisibility(View.VISIBLE);
            rb_book_list.setVisibility(View.VISIBLE);
    }




    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }



    @OnClick(R.id.tv_cancel)void cancelClick(){
        finish();
    }


    @OnClick(R.id.iv_clear)void clearContentClick(){
        ed_search_keyword.setText("");
    }

    @OnClick(R.id.iv_book_feedback)void bookFeedbackClick(){
        if (null==rxDialogBookFeedback){
            rxDialogBookFeedback=new RxDialogBookFeedback(this, R.style.OptionDialogStyle);
        }
        //搜索框的数据自动填充进去
        if (!TextUtils.isEmpty(getKeyword())){
            rxDialogBookFeedback.getEd_book_name().setText(getKeyword());
            rxDialogBookFeedback.getEd_book_name().setSelection(rxDialogBookFeedback.getEd_book_name().getText().toString().length());
        }

        rxDialogBookFeedback.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                searchDemandRequest(rxDialogBookFeedback.getEd_book_name().getText().toString(),rxDialogBookFeedback.getEd_author_name().getText().toString());

                rxDialogBookFeedback.getEd_book_name().setText("");
                rxDialogBookFeedback.getEd_author_name().setText("");
                rxDialogBookFeedback.dismiss();
            }
        });
        rxDialogBookFeedback.show();
    }


    /**
     * 提交反馈
     * @param name
     * @param author
     */
    private void searchDemandRequest(String name,String author){

        Map<String,String>map=new HashMap<>();
        if (!TextUtils.isEmpty(name)){
            map.put("name",name);
        }
        if (!TextUtils.isEmpty(author)){
            map.put("author",author);
        }

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/search/demand", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("您的反馈已经提交成功!", Constant.ToastType.TOAST_SUCCESS).show();
                    }else{
                        RxToast.custom(baseBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                    //隐藏键盘
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(null != ed_search_keyword) {
                                    RxKeyboardTool.hideKeyboard(ActivitySearch2.this, ed_search_keyword);
                                }
                            }catch (Exception e){

                            }
                        }
                    },200);
                }catch (Exception e){
//                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.iv_book_feedback_guide)void rlGuideClick(){
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.SHOW_SEARCH_BOOK_GUIDE,false);//不显示该图层
        rl_guide.setVisibility(View.GONE);

        //引导弹框
        bookFeedbackClick();
    }

    public void showGuideView(){
        try{
            if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SHOW_SEARCH_BOOK_GUIDE,true)){
                rl_guide.setVisibility(View.VISIBLE);
            }else{
                rl_guide.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }


    public void setSelected(int i) {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        hideTransaction(fragmentTransaction);//自定义一个方法，来隐藏所有的fragment

        String keyWord = getKeyword();

        switch (i) {
            case 0:
                if(null == searchIntegrateFragment) {
                    searchIntegrateFragment = new SearchIntegrateFragment();
                    fragmentTransaction.add(R.id.frame_layout, searchIntegrateFragment);
                    if(!TextUtils.isEmpty(keyWord))
                    searchIntegrateFragment.searchIntegrateRequest(keyWord);
                }
                fragmentTransaction.show(searchIntegrateFragment);
                break;
            case 1:
                if(null == searchBookFragment) {
                    searchBookFragment = new SearchBookFragment();
                    fragmentTransaction.add(R.id.frame_layout, searchBookFragment);
                    if(!TextUtils.isEmpty(keyWord))
                    searchBookFragment.bookListRequest(keyWord,true);
                }
                fragmentTransaction.show(searchBookFragment);
                break;
            case 2:
                if(null == searchBookListFragment) {
                    searchBookListFragment = new SearchBookListFragment();
                    fragmentTransaction.add(R.id.frame_layout, searchBookListFragment);
                    if(!TextUtils.isEmpty(keyWord))
                    searchBookListFragment.bookListRequest(keyWord,true);
                }
                fragmentTransaction.show(searchBookListFragment);
                break;
            case 3:
                if(null == bookShortageFragment) {
                    bookShortageFragment = new BookShortageFragment();
                    fragmentTransaction.add(R.id.frame_layout, bookShortageFragment);
                    bookShortageFragment.bookListRequest(getKeyword());
                }
                fragmentTransaction.show(bookShortageFragment);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();//最后千万别忘记提交事务
        index = i;
    }

    //隐藏fragment
    private void hideTransaction(FragmentTransaction ftr) {

        if (searchIntegrateFragment != null) {
            ftr.hide(searchIntegrateFragment);//隐藏该fragment
        }
        if (searchBookFragment != null) {
            ftr.hide(searchBookFragment);
        }
        if (searchBookListFragment != null) {
            ftr.hide(searchBookListFragment);
        }
        if (bookShortageFragment != null) {
            ftr.hide(bookShortageFragment);
        }
    }

    public void changeTab(int pos) {
        if (pos == 0) {
            rb_all.setChecked(true);
        } else if (pos == 1) {
            rb_book.setChecked(true);
        } else if (pos == 2) {
            rb_book_list.setChecked(true);
        } else if (pos == 3) {
            rb_book_short.setChecked(true);
        }
    }

    public void search(String keyWord){
        if(!TextUtils.isEmpty(keyWord)) {
            ed_search_keyword.setText(keyWord);
            //刷新数据
            doSearchRequest(false);
        }else {
            RxToast.custom("没有搜索关键词").show();
        }
    }

}
