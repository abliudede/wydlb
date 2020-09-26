package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.widget.EditText;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.AuthRealNameContract;
import com.lianzai.reader.ui.presenter.AuthRealNamePresenter;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxRegTool;
import com.lianzai.reader.view.RxToast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 实名认证-初级认证
 */

public class ActivityRealNameAuthFirst extends BaseActivity implements AuthRealNameContract.View{


    @Bind(R.id.ed_real_name)
    EditText ed_real_name;

    @Bind(R.id.ed_id_card_number)
    EditText ed_id_card_number;


    @Inject
    AuthRealNamePresenter authRealNamePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_safety_real_name_auth_first;
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
        authRealNamePresenter.attachView(this);
    }


    @Override
    public void gc() {
        RxToast.gc();
    }


    /**
     * 认证提交第一步
     */
    @OnClick(R.id.btn_submit)void submitAuthClick(){
        if (RxDataTool.isEmpty(ed_real_name.getText().toString())){
            RxToast.custom(getResources().getString(R.string.real_name_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (RxDataTool.isEmpty(ed_id_card_number.getText().toString())){
            RxToast.custom(getResources().getString(R.string.id_card_hint),Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        if (!RxRegTool.isIDCard(ed_id_card_number.getText().toString())){
            RxToast.custom(getResources().getString(R.string.id_card_hint1),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        ArrayMap<String,Object> maps=new ArrayMap();
        maps.put("cardCode",ed_id_card_number.getText().toString().trim());
        maps.put("name",ed_real_name.getText().toString());
        maps.put("cardType",Constant.AuthType.IDCARD_TYPE);
        maps.put("type","2");
        authRealNamePresenter.primaryCertification(maps);
        showDialog();

    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }

        RxToast.custom(message,Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void complete(String message) {
        showSeverErrorDialog(message);
    }

    @Override
    public void primaryCertificationSuccess(BaseBean bean) {
        RxToast.custom("恭喜您，安全等级1认证成功",Constant.ToastType.TOAST_SUCCESS).show();
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_ACCOUNT);
        finish();

    }

    @Override
    public void advancedCertificationSuccess(BaseBean bean) {

    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

}
