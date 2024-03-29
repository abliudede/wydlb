package com.wydlb.first.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.ui.adapter.SmallConfidentialPaymentItemAdapter;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxEncryptTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 小额免密支付额度
 */

public class ActivitySmallConfidentialPaymentAmount extends BaseActivity {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    private String amountConfig;
    private String chooseStr = "";
    private int type;


    private SmallConfidentialPaymentItemAdapter sortItemAdapter;
    List<String> sortList = new ArrayList<>();

    RxDialogPayment rxDialogPayment;//支付密码框

    //type 1金币 2无 3阅点 4阅券 5书籍币 6书籍券
    public static void startActivity(Activity context, String amountConfig, String chooseStr , int type) {
        Bundle bundle = new Bundle();
        bundle.putString("amountConfig", amountConfig);
        bundle.putString("chooseStr", chooseStr);
        bundle.putInt("type", type);
        RxActivityTool.skipActivityForResult(context, ActivitySmallConfidentialPaymentAmount.class, bundle,type);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_small_confidential_payment_amount;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        try {
            amountConfig = getIntent().getStringExtra("amountConfig");
            chooseStr = getIntent().getStringExtra("chooseStr");
            type = getIntent().getIntExtra("type", 0);
        } catch (Exception e) {
        }

        if(type == 3){
            tv_commom_title.setText("阅点免密额度");
        }else if(type == 4){
            tv_commom_title.setText("阅券免密额度");
        }else if(type == 1){
            tv_commom_title.setText("金币免密额度");
        }else if(type == 5){
            tv_commom_title.setText("版权积分免密额度");
        }

        String[] listTemp = amountConfig.split(",");
        if(listTemp.length > 1){
            for(String item : listTemp){
                sortList.add(item);
            }
        }
        rxDialogPayment=new RxDialogPayment(ActivitySmallConfidentialPaymentAmount.this,R.style.OptionDialogStyle);

        sortItemAdapter = new SmallConfidentialPaymentItemAdapter(sortList,this);
        sortItemAdapter.setChooseStr(chooseStr);
        sortItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                chooseStr = sortList.get(i);
                sortItemAdapter.setChooseStr(chooseStr);
                sortItemAdapter.notifyDataSetChanged();
                //上传
                rxDialogPayment.getTv_sure().setOnClickListener(
                        view1-> {
                            if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())) {
                                RxToast.custom("请输入您的支付密码").show();
                                return;
                            }
                            rxDialogPayment.dismiss();
                            unpasswordUpdate(chooseStr,rxDialogPayment.getEd_pay_password().getText().toString());
                        });
                rxDialogPayment.show();
            }
        });
        RxLinearLayoutManager manager=new RxLinearLayoutManager(this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(sortItemAdapter);



    }



    @Override
    public void gc() {

    }

    @OnClick(R.id.img_back)void closeClick(){
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 打开
     */
    public void unpasswordUpdate (String amount,String password) {
        //支付密码输入完后执行下面操作
        HashMap map = new HashMap();
        map.put("amount", amount);
        map.put("type", String.valueOf(type));
        map.put("password", password);


        //签名生成
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
//        parameters.put("amount",amount);
        parameters.put("type",String.valueOf(type));
        parameters.put("password",password);
        String sortUrl= RxTool.sortMap(parameters,Constant.SIGN_KEY);
        map.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
        RxLogTool.e("/unpassword/v2/update:" + map.toString());

        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/unpassword/v2/update",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        Bundle bundle=new Bundle();
                        bundle.putString("amount",amount);
                        Intent intent=new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
