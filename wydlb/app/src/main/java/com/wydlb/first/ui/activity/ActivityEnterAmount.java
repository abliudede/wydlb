package com.wydlb.first.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.interfaces.OnRepeatClickListener;
import com.wydlb.first.ui.adapter.GirdRecyclerViewAdapter2;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogSureCancel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/24.
 * 输入打赏金额
 */

public class ActivityEnterAmount extends BaseActivity {

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.ed_content)
    EditText ed_content;

    @Bind(R.id.activity_creatquanzi_recycle)
    RecyclerView activity_creatquanzi_recycle;

    /**
     * 显示的数据
     */
    private List<String> mDatas;
    /**
     * RecyclerView的适配器
     */
    private GirdRecyclerViewAdapter2 adapter;


    RxDialogSureCancel authRealNameDialogSureCancel;
    @Override
    public int getLayoutId() {
        return R.layout.activity_enter_amount;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }
    @Override
    public void configViews(Bundle savedInstanceState) {

        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        initAuthRealNameDialog();

        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(null == s){
                    tv_options.setEnabled(false);
                    tv_options.setBackgroundResource(R.drawable.btn_disable_bg);
                }else if(TextUtils.isEmpty(s.toString())){
                    tv_options.setEnabled(false);
                    tv_options.setBackgroundResource(R.drawable.btn_disable_bg);
                }else{
                    tv_options.setEnabled(true);
                    tv_options.setBackgroundResource(R.drawable.sure_btn);
                }

            }
        });
        tv_options.setEnabled(false);
        tv_options.setBackgroundResource(R.drawable.btn_disable_bg);


        //防止发帖点击太快，发重复贴
        tv_options.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                if (TextUtils.isEmpty(ed_content.getText().toString().trim())){
                    RxToast.custom(getString(R.string.amountnotnull)).show();
                    return;
                }
                int i = 0;
                try{
                     i = Integer.parseInt(ed_content.getText().toString().trim());
                }catch (Exception e){
                    RxLogTool.e(e);
                    RxToast.custom(getString(R.string.amountnull)).show();
                    return;
                }

                if(i < 1){
                    RxToast.custom(getString(R.string.donotlessthanone)).show();
                    return;
                }

                Intent intent  = new Intent();
                intent.putExtra("amount",String.valueOf(i));
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        mDatas = new ArrayList<String>();
        mDatas.add("5金币");
        mDatas.add("50金币");
        mDatas.add("100金币");
        mDatas.add("500金币");
        mDatas.add("1000金币");
        mDatas.add("2000金币");
        //2.声名gridview布局方式  第二个参数代表是2列的网格视图 (垂直滑动的情况下, 如果是水平滑动就代表2行)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        //3.给GridLayoutManager设置滑动的方向
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        //4.为recyclerView设置布局管理器
        activity_creatquanzi_recycle.setLayoutManager(gridLayoutManager);

        adapter = new GirdRecyclerViewAdapter2(this, mDatas);
        //设置添加,移除item的动画,DefaultItemAnimator为默认的
        activity_creatquanzi_recycle.setItemAnimator(new DefaultItemAnimator());
        //4.设置适配器
        activity_creatquanzi_recycle.setAdapter(adapter);

        //添加点击事件
        adapter.setOnItemClickListener(new GirdRecyclerViewAdapter2.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setChoosingPosition(position);
                String str = mDatas.get(position);
                ed_content.setText(str.substring(0,str.length()-2));
                tv_options.setEnabled(true);
                tv_options.setBackgroundResource(R.drawable.sure_btn);
            }
        });


    }

    @Override
    public void gc() {

    }


    @OnClick(R.id.img_back)void closeClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    /**
     * 实名认证判断
     */
    private void initAuthRealNameDialog(){
        authRealNameDialogSureCancel=new RxDialogSureCancel(this,R.style.OptionDialogStyle);
        authRealNameDialogSureCancel.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SystemBarUtils.expandNavBar(ActivityEnterAmount.this);
                SystemBarUtils.hideStableNavBar(ActivityEnterAmount.this);
            }
        });
    }

}
