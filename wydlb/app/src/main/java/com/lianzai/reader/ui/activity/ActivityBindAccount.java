package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BindAccountListResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.UnBindWxBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.BindAccountListContract;
import com.lianzai.reader.ui.contract.UnbindAccountContract;
import com.lianzai.reader.ui.presenter.BindAccountListPresenter;
import com.lianzai.reader.ui.presenter.UnbindAccountPresenter;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogConfirm;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 账号与绑定设置
 */

public class ActivityBindAccount extends BaseActivity implements UnbindAccountContract.View ,BindAccountListContract.View {

    RxDialogConfirm rxDialogConfirm;
    RxDialogConfirm rxDialogConfirm2;

    @Bind(R.id.tv_bind_phone_status)
    TextView tv_bind_phone_status;

    @Bind(R.id.tv_bind_wx_status)
    TextView tv_bind_wx_status;

    @Inject
    UnbindAccountPresenter unbindAccountPresenter;

    AccountDetailBean accountDetailBean;

    @Inject
    BindAccountListPresenter bindAccountListPresenter;

    BindAccountListResponse bindAccountListResponse;


    @Bind(R.id.tv_commom_title)
    TextView tv_common_title;

    //单个绑定数据
    private BindAccountListResponse.DataBean phoneBind;
    private BindAccountListResponse.DataBean wxBind;

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_bind_account;
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
        tv_common_title.setText(getResources().getString(R.string.bind_account));

        accountDetailBean= RxTool.getAccountBean();
        if (null==accountDetailBean)
            return;

        unbindAccountPresenter.attachView(this);
        bindAccountListPresenter.attachView(this);

        initClearCacheDialog();

        //假如手机号不为空，先显示手机号
        if(!TextUtils.isEmpty(accountDetailBean.getData().getMobile()))
        tv_bind_phone_status.setText(RxDataTool.hideMobilePhone4(accountDetailBean.getData().getMobile()));

        bindAccountListPresenter.getBindAccountList();
    }

    @Override
    public void gc() {

    }

    private void initClearCacheDialog(){
        rxDialogConfirm=new RxDialogConfirm(this,R.style.BottomDialogStyle);
        rxDialogConfirm.getTv_title().setText("确定要解除与微信的绑定吗?");
        rxDialogConfirm.getTv_sure().setTextColor(getResources().getColor(R.color.blue_color));
        rxDialogConfirm.getTv_sure().setText("确认解绑");
        rxDialogConfirm.getTv_sure().setOnClickListener(
                v->{
                    ArrayMap<String,Object> map=new ArrayMap<String,Object>();
                    map.put("thirdType","1");
                    unbindAccountPresenter.unBindAccount(map);
                    rxDialogConfirm.dismiss();
                }
        );

        rxDialogConfirm2=new RxDialogConfirm(this,R.style.BottomDialogStyle);
        rxDialogConfirm2.getTv_title().setText("确定要换绑手机吗?");
        rxDialogConfirm2.getTv_sure().setTextColor(getResources().getColor(R.color.blue_color));
        rxDialogConfirm2.getTv_sure().setText("确认换绑");
        rxDialogConfirm2.getTv_sure().setOnClickListener(
                v->{
                    bindPhone();
                    rxDialogConfirm2.dismiss();
                }
        );
    }
    @Override
    public void initToolBar() {

    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @OnClick(R.id.tv_bind_wx)void bindWxClick(){
        if (null!=wxBind){
            if (wxBind.isBind()){
                rxDialogConfirm.show();
            }else{
                bindWx();
            }
        }
    }

    @OnClick(R.id.tv_bind_phone_status)void bindPhoneClick(){
        if (null!=phoneBind){
            if (phoneBind.isBind()){
                rxDialogConfirm2.show();
            }else{
                bindPhone();
            }
        }
    }

    private void bindPhone() {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
        RxActivityTool.skipActivity(this, ActivityBindPhone.class, bundle);
    }

    private void bindWx(){
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        //向微信发送请求
        BuglyApplicationLike.api.sendReq(req);
    }


    @Override
    public void unBindAccountSuccess(UnBindWxBean bean) {
        //置换token操作，慎重
        AccountTokenBean temp = RxLoginTool.getLoginAccountToken();
        temp.getData().setToken(bean.getData());
        RxLoginTool.saveToken(temp);

        tv_bind_wx_status.setText("点击绑定");
        tv_bind_wx_status.setTextColor(getResources().getColor(R.color.blue_color));
        bindAccountListPresenter.getBindAccountList();
    }
    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }

        RxToast.custom(message).show();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }


    @Override
    public void bindAccountSuccess(BaseBean bean) {
        dismissDialog();
        if (bean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
            tv_bind_wx_status.setText("已绑定");
            tv_bind_wx_status.setTextColor(getResources().getColor(R.color.third_text_color));
            if(wxBind != null){
                wxBind.setBind(true);
            }
            bindAccountListPresenter.getBindAccountList();
        }else if (bean.getCode()==Constant.ResponseCodeStatus.BIND_PHONE){
            RxToast.custom("该微信号无法绑定,请更换其他微信绑定",Constant.ToastType.TOAST_ERROR).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataSynEvent event) {
        RxLogTool.e("ActivityBindAccount onEvent....");
        if(event.getTag()== Constant.EventTag.WX_LOGIN&&null!=event.getObj()){//微信授权登录
            String code=event.getObj().toString();
            RxLogTool.e("-----wx code:"+code);
            ArrayMap<String,Object> params=new ArrayMap<>();
            params.put("code",code);
            params.put("thirdType","1");
            showDialog();
            bindAccountListPresenter.bindAccount(params);
        } if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void getBindAccountList(BindAccountListResponse bean) {
        bindAccountListResponse=bean;
        if(null == bindAccountListResponse) return;
        if(null == bindAccountListResponse.getData()) return;
        if(bindAccountListResponse.getCode() != Constant.ResponseCodeStatus.SUCCESS_CODE) return;
        //遍历绑定列表
        for(BindAccountListResponse.DataBean item : bindAccountListResponse.getData()){
            if(item.getType() == 0){
                phoneBind = item;
            }
            if(item.getType() == 1){
                wxBind = item;
            }
        }

        if(null != phoneBind){
            if(phoneBind.isBind()) {
                tv_bind_phone_status.setText(RxDataTool.hideMobilePhone4(phoneBind.getMobile()));
            }else {
                tv_bind_phone_status.setText("点击绑定");
                tv_bind_phone_status.setTextColor(getResources().getColor(R.color.third_text_color));
            }
        }else {
            tv_bind_phone_status.setText("点击绑定");
            tv_bind_phone_status.setTextColor(getResources().getColor(R.color.third_text_color));
        }

        if(null != wxBind){
            if(wxBind.isBind()) {
                tv_bind_wx_status.setText("已绑定");
                tv_bind_wx_status.setTextColor(getResources().getColor(R.color.third_text_color));
            }else {
                tv_bind_wx_status.setText("点击绑定");
                tv_bind_wx_status.setTextColor(getResources().getColor(R.color.blue_color));
            }
        }else {
            tv_bind_wx_status.setText("点击绑定");
            tv_bind_wx_status.setTextColor(getResources().getColor(R.color.blue_color));
        }
    }


}
