package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.bean.AuthStatusResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.AuthInfoContract;
import com.lianzai.reader.ui.presenter.AuthInfoPresenter;
import com.lianzai.reader.view.RxToast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 安全等级
 */

public class ActivitySafety extends BaseActivity implements AuthInfoContract.View{

    @Bind(R.id.iv_primary_status)
    ImageView iv_primary_status;

//    @Bind(R.id.iv_advanced_status)
//    ImageView iv_advanced_status;

    @Bind(R.id.tv_description_title)
    TextView tv_description_title;

//    @Bind(R.id.tv_advanced_status)
//    TextView tv_advanced_status;


    @Bind(R.id.rl_primary_auth)
    RelativeLayout rl_primary_auth;

//    @Bind(R.id.rl_advanced_auth)
//    RelativeLayout rl_advanced_auth;

    @Bind(R.id.ll_verity_success)
    LinearLayout ll_verity_success;

    @Bind(R.id.tv_description_title1)
    TextView tv_description_title1;

    @Inject
    AuthInfoPresenter authInfoPresenter;

    AuthStatusResponse authStatusResponse;


    @Override
    public int getLayoutId() {
        return R.layout.activity_safety_class;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        authInfoPresenter.getAuthInfo();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        authInfoPresenter.attachView(this);
    }


    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void initToolBar() {

    }
    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @Override
    public void getAuthInfoSuccess(AuthStatusResponse bean) {
        authStatusResponse=bean;

        if (null==authStatusResponse.getData()){//未认证过
            ll_verity_success.setVisibility(View.GONE);
            tv_description_title.setText("您当前的安全等级为 "+0+" 级");

            rl_primary_auth.setVisibility(View.VISIBLE);
//            rl_advanced_auth.setVisibility(View.VISIBLE);

            iv_primary_status.setImageResource(R.mipmap.v2_icon_not_auth);
//            iv_advanced_status.setImageResource(R.mipmap.v2_icon_auth_locked);

            rl_primary_auth.setOnClickListener(
                    v->RxActivityTool.skipActivity(ActivitySafety.this,ActivityRealNameAuthFirst.class)
            );

        }else{
//            if (authStatusResponse.getData().getType()==2){//初级认证
//                tv_description_title.setText("您当前的安全等级为 "+1+" 级");
//                ll_verity_success.setVisibility(View.GONE);
//
//                rl_primary_auth.setVisibility(View.GONE);
//                rl_advanced_auth.setVisibility(View.VISIBLE);
//
//                iv_advanced_status.setImageResource(R.mipmap.v2_icon_not_auth);
//
//
//                rl_advanced_auth.setOnClickListener(
//                        v->RxActivityTool.skipActivity(ActivitySafety.this,ActivityRealNameAuthSecond.class)
//                );
//            }else{
//                if (authStatusResponse.getData().getStatus()==3){
//                    ll_verity_success.setVisibility(View.GONE);
//                    tv_description_title.setText("您当前的安全等级为 "+1+" 级");
//
//                    rl_primary_auth.setVisibility(View.GONE);
//                    rl_advanced_auth.setVisibility(View.VISIBLE);
//                    iv_advanced_status.setVisibility(View.GONE);
//
//
//                    tv_advanced_status.setVisibility(View.VISIBLE);
//                    tv_advanced_status.setText("审核失败,请重新提交");
//                    iv_advanced_status.setVisibility(View.GONE);
//
//                    rl_advanced_auth.setOnClickListener(
//                            v-> RxActivityTool.skipActivity(ActivitySafety.this,ActivityRealNameAuthSecond.class)
//                    );
//                }else if (authStatusResponse.getData().getStatus()==1){//2级认证审核中
//                    tv_description_title.setText("您当前的安全等级为 "+1+" 级");
//                    ll_verity_success.setVisibility(View.GONE);
//
//                    rl_primary_auth.setVisibility(View.GONE);
//                    rl_advanced_auth.setVisibility(View.VISIBLE);
//                    iv_advanced_status.setVisibility(View.VISIBLE);
//                    iv_advanced_status.setImageResource(R.mipmap.v2_icon_in_review_status);
//
//
//                    tv_advanced_status.setVisibility(View.GONE);
//                    tv_advanced_status.setText("审核中");
//
//                    rl_advanced_auth.setOnClickListener(
//                            v->RxToast.custom("认证正在审核中,请耐心等待", Constant.ToastType.TOAST_NORMAL).show()
//                    );
//
//
//                }else if(authStatusResponse.getData().getStatus()==2){//高级认证 审核通过
//                    ll_verity_success.setVisibility(View.VISIBLE);
//                    tv_description_title.setVisibility(View.GONE);
//
//                    rl_primary_auth.setVisibility(View.GONE);
//                    rl_advanced_auth.setVisibility(View.GONE);
//
//                    tv_description_title1.setText(Html.fromHtml("当前的安全等级为 <font color='#424242'>1</font> 级,已解锁全部权益"));
//                }
//            }

            //只有一级认证，通过则解锁全部权益
            ll_verity_success.setVisibility(View.VISIBLE);
            tv_description_title.setVisibility(View.GONE);
            rl_primary_auth.setVisibility(View.GONE);
//                rl_advanced_auth.setVisibility(View.GONE);
            tv_description_title1.setText(Html.fromHtml("当前的安全等级为 <font color='#424242'>1</font> 级,已解锁全部权益"));

        }

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }
}
