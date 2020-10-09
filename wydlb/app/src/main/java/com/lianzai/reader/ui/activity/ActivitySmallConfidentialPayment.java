package com.lianzai.reader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.UnpasswordDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 小额免密支付
 */

public class ActivitySmallConfidentialPayment extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;


    @Bind(R.id.sb_msg_off)
    CheckBox sb_msg_off;

    @Bind(R.id.tv_des)
    TextView tv_des;


    @Bind(R.id.tv_amount1)
    TextView tv_amount1;
    @Bind(R.id.tv_amount2)
    TextView tv_amount2;
    @Bind(R.id.tv_amount3)
    TextView tv_amount3;
    @Bind(R.id.tv_amount4)
    TextView tv_amount4;

    @Bind(R.id.rl1)
    RelativeLayout rl1;
    @Bind(R.id.rl2)
    RelativeLayout rl2;
    @Bind(R.id.rl3)
    RelativeLayout rl3;
    @Bind(R.id.rl4)
    RelativeLayout rl4;
    private boolean requestCheck = false;

    private AccountDetailBean accountDetailBean;

    RxDialogPayment rxDialogPayment;//支付密码框
    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号
    //设置支付密码弹框
    RxDialogSureCancel rxDialogSureCancel;

    private UnpasswordDetailBean baseBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_small_confidential_payment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {
        accountDetailBean = RxTool.getAccountBean();
        if (null == accountDetailBean) {
            RxToast.custom("未登录").show();
            finish();
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("小额免密支付");
        requestDetail();
        rxDialogPayment = new RxDialogPayment(this, R.style.OptionDialogStyle);
        rxDialogSureCancel = new RxDialogSureCancel(this, R.style.OptionDialogStyle);
        rxDialogSureCancel.setContent("您还未设置支付密码，请先设置支付密码。");
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.setCancel("再看看");
        rxDialogSureCancel.setSure("立即设置");
        rxDialogSureCancel.setCancelable(false);

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", Constant.RegisterOrPassword.PayPassword);
                RxActivityTool.skipActivity(ActivitySmallConfidentialPayment.this, ActivityEditPasswordShowPhone.class, bundle);
            }
        });
    }

    /**
     * 获取详情,type 1金币 2无 3阅点 4阅券 5书籍币 6书籍券
     */
    public void requestDetail() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/unpassword/detail", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e(e.toString());
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    baseBean = GsonUtil.getBean(response, UnpasswordDetailBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        if (baseBean.getData().isIsOpen()) {
                            if(sb_msg_off.isChecked()) {
                                requestCheck = false;
                            }else {
                                requestCheck = true;
                            }
                            sb_msg_off.setChecked(true);
                            tv_des.setVisibility(View.GONE);
                            rl1.setVisibility(View.VISIBLE);
                            rl2.setVisibility(View.VISIBLE);
                            rl3.setVisibility(View.VISIBLE);
                            rl4.setVisibility(View.VISIBLE);
                        } else {
                            if(!sb_msg_off.isChecked()) {
                                requestCheck = false;
                            }else {
                                requestCheck = true;
                            }
                            sb_msg_off.setChecked(false);
                            tv_des.setVisibility(View.VISIBLE);
                            rl1.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            rl3.setVisibility(View.GONE);
                            rl4.setVisibility(View.GONE);
                        }

                        if (null == baseBean.getData() || null == baseBean.getData().getList() || baseBean.getData().getList().isEmpty()) {
                            tv_amount1.setText("0");
                            tv_amount2.setText("0");
                            tv_amount3.setText("0");
                            tv_amount4.setText("0");
                            rl1.setClickable(false);
                            rl2.setClickable(false);
                            rl3.setClickable(false);
                            rl4.setClickable(false);
                        } else {
                            for (UnpasswordDetailBean.DataBean.ListBean item : baseBean.getData().getList()) {
                                if (item.getType() == 3) {
                                    tv_amount1.setText(String.valueOf(item.getDefaultAmount()));
                                    if (!TextUtils.isEmpty(item.getAmountConfig())) {
                                        rl1.setClickable(true);
                                    } else {
                                        rl1.setClickable(false);
                                    }
                                } else if (item.getType() == 4) {
                                    tv_amount2.setText(String.valueOf(item.getDefaultAmount()));
                                    if (!TextUtils.isEmpty(item.getAmountConfig())) {
                                        rl2.setClickable(true);
                                    } else {
                                        rl2.setClickable(false);
                                    }
                                } else  if (item.getType() == 1){
                                    tv_amount3.setText(String.valueOf(item.getDefaultAmount()));
                                    if (!TextUtils.isEmpty(item.getAmountConfig())) {
                                        rl3.setClickable(true);
                                    } else {
                                        rl3.setClickable(false);
                                    }
                                }else  if (item.getType() == 5){
                                    tv_amount4.setText(String.valueOf(item.getDefaultAmount()));
                                    if (!TextUtils.isEmpty(item.getAmountConfig())) {
                                        rl4.setClickable(true);
                                    } else {
                                        rl4.setClickable(false);
                                    }
                                }
                            }
                        }
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打开
     */
    public void unpasswordOpen(String password) {
        //支付密码输入完后执行下面操作
        HashMap map = new HashMap();
        map.put("password", password);

        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("password", password);
        String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
        RxLogTool.e("sortUrl:" + sortUrl);
        map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

        rxDialogPayment.getEd_pay_password().setText("");
        showDialog();
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/unpassword/open", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                    if(sb_msg_off.isChecked()) {
                        requestCheck = false;
                    }else {
                        requestCheck = true;
                    }
                    sb_msg_off.setChecked(false);
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        requestDetail();
                    } else {
                        if(!sb_msg_off.isChecked()) {
                            requestCheck = false;
                        }else {
                            requestCheck = true;
                        }
                        sb_msg_off.setChecked(false);
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关闭
     */
    public void unpasswordClose() {
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/unpassword/close", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        requestDetail();
                    } else {
                        if(sb_msg_off.isChecked()) {
                            requestCheck = false;
                        }else {
                            requestCheck = true;
                        }
                        sb_msg_off.setChecked(true);
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //免密支付开关
    @OnCheckedChanged(R.id.sb_msg_off)
    void sb_msg_offChanged(boolean isChecked) {
        if(requestCheck){
            //此处不能这样操作
            requestCheck = false;
        }else {
        if (isChecked) {

            if (TextUtils.isEmpty(accountDetailBean.getData().getMobile())) {
                //绑定手机号弹窗
                if (null == rxDialogBindPhone) {
                    rxDialogBindPhone = new RxDialogBindPhone(ActivitySmallConfidentialPayment.this, R.style.OptionDialogStyle);
                    rxDialogBindPhone.setContent("您暂未绑定手机号，请先绑定手机号再使用相关功能。");
                    rxDialogBindPhone.setTitle("绑定手机号提示");
                    rxDialogBindPhone.getCancelView().setVisibility(View.VISIBLE);
                    rxDialogBindPhone.setButtonText("立即绑定", "取消");
                    rxDialogBindPhone.setSureListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            //跳转到绑定手机号页面
                            rxDialogBindPhone.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
                            RxActivityTool.skipActivity(ActivitySmallConfidentialPayment.this, ActivityBindPhone.class, bundle);
                        }
                    });

                    rxDialogBindPhone.setCancelListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            rxDialogBindPhone.dismiss();
                        }
                    });
                }
                rxDialogBindPhone.show();
                if(!sb_msg_off.isChecked()) {
                    requestCheck = false;
                }else {
                    requestCheck = true;
                }
                sb_msg_off.setChecked(false);
            } else if (!accountDetailBean.getData().isHasPayPwd()) {////先判断是否设置了支付密码
                rxDialogSureCancel.show();
                if(!sb_msg_off.isChecked()) {
                    requestCheck = false;
                }else {
                    requestCheck = true;
                }
                sb_msg_off.setChecked(false);
            } else {
                //支付密码输入弹框
                rxDialogPayment.getTv_sure().setOnClickListener(
                        view -> {
                            if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())) {
                                RxToast.custom("请输入您的支付密码").show();
                                if(sb_msg_off.isChecked()) {
                                    requestCheck = false;
                                }else {
                                    requestCheck = true;
                                }
                                sb_msg_off.setChecked(false);
                                return;
                            }
                            rxDialogPayment.dismiss();
                            unpasswordOpen(rxDialogPayment.getEd_pay_password().getText().toString());
                        }
                );
                rxDialogPayment.getTv_cancel().setOnClickListener(
                        v -> {
                            if(sb_msg_off.isChecked()) {
                                requestCheck = false;
                            }else {
                                requestCheck = true;
                            }
                            sb_msg_off.setChecked(false);
                            rxDialogPayment.dismiss();
                        }
                );
                rxDialogPayment.show();
            }

        } else {
            unpasswordClose();
        }
        }
    }


    @OnClick(R.id.rl1)
    void rl1Click() {
        for (UnpasswordDetailBean.DataBean.ListBean item : baseBean.getData().getList()) {
            if (item.getType() == 3) {
                ActivitySmallConfidentialPaymentAmount.startActivity(ActivitySmallConfidentialPayment.this, item.getAmountConfig(), String.valueOf(item.getDefaultAmount()), item.getType());
            }
        }
    }

    @OnClick(R.id.rl2)
    void rl2Click() {
        for (UnpasswordDetailBean.DataBean.ListBean item : baseBean.getData().getList()) {
            if (item.getType() == 4) {
                ActivitySmallConfidentialPaymentAmount.startActivity(ActivitySmallConfidentialPayment.this, item.getAmountConfig(), String.valueOf(item.getDefaultAmount()), item.getType());
            }
        }
    }

    @OnClick(R.id.rl3)
    void rl3Click() {
        for (UnpasswordDetailBean.DataBean.ListBean item : baseBean.getData().getList()) {
            if (item.getType() == 1) {
                ActivitySmallConfidentialPaymentAmount.startActivity(ActivitySmallConfidentialPayment.this, item.getAmountConfig(), String.valueOf(item.getDefaultAmount()), item.getType());
            }
        }
    }

    @OnClick(R.id.rl4)
    void rl4Click() {
        for (UnpasswordDetailBean.DataBean.ListBean item : baseBean.getData().getList()) {
            if (item.getType() == 5) {
                ActivitySmallConfidentialPaymentAmount.startActivity(ActivitySmallConfidentialPayment.this, item.getAmountConfig(), String.valueOf(item.getDefaultAmount()), item.getType());
            }
        }
    }


    @Override
    public void gc() {

    }

    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)
    void closeClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && null != data.getExtras()) {
            Bundle bundle = data.getExtras();
            String amount = bundle.getString("amount");
            if (requestCode == 3) {//type
                tv_amount1.setText(amount);
            } else if (requestCode == 4) {//type
                tv_amount2.setText(amount);
            } else if (requestCode == 1) {//type
                tv_amount3.setText(amount);
            } else if (requestCode == 5) {//type
                tv_amount4.setText(amount);
            }
            requestDetail();
        }
    }

}
