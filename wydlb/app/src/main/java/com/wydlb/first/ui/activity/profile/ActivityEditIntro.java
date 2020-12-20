package com.wydlb.first.ui.activity.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.AccountDetailBean;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.view.RxToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * 修改简介
 */

public class ActivityEditIntro extends BaseActivity {


    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;


    @Bind(R.id.ed_nickname)
    EditText ed_nickname;

    @Bind(R.id.count_tv)
    TextView count_tv;
    private AccountDetailBean accountDetailBean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_intro_edit;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("修改个人简介");
        tv_options.setText("保存");
        if (null!=accountDetailBean && null != accountDetailBean.getData().getIntroduce()){
            ed_nickname.setText(accountDetailBean.getData().getIntroduce().replace("\n",""));
        }

        tv_options.setOnClickListener(
                v->{
                    if (!TextUtils.isEmpty(ed_nickname.getText().toString().replace("\n","").trim())){
                        showDialog();
                        editProfileRequest(ed_nickname.getText().toString().replace("\n",""));
                    }else{
                        RxToast.custom("个人简介不允许为空",Constant.ToastType.TOAST_ERROR).show();
                    }
                }
        );
        ed_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (TextUtils.isEmpty(editable.toString())){
//                    iv_clear.setVisibility(View.GONE);
//                }else{
//                    iv_clear.setVisibility(View.VISIBLE);
//                }
                if(null != editable){
                    int num = editable.length();
                    count_tv.setText(String.valueOf(num) + "/50");
                }else {
                    count_tv.setText("0/50");
                }
            }
        });

//        iv_clear.setOnClickListener(
//                v->ed_nickname.setText("")
//        );

    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }


    private void editProfileRequest(String introduce){
        Map map=new HashMap();
        map.put("introduce",introduce);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/updateMemberInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }

            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("个人简介修改成功",Constant.ToastType.TOAST_SUCCESS).show();
                        finish();
                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){

                }
                RxLogTool.e("editProfileRequest response:"+response);
            }
        });
    }
}
