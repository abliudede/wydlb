package com.lianzai.reader.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxAnimationTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2017/10/14.
 * 实名认证成功
 */

public class ActivityRealNameAuthSuccess extends BaseActivity {

    @Bind(R.id.iv_status)
    ImageView iv_status;

    @Bind(R.id.tv_certificates_type)
    TextView tv_certificates_type;

    @Bind(R.id.tv_real_name)
    TextView tv_real_name;

    @Bind(R.id.tv_id_card)
    TextView tv_id_card;

    @Bind(R.id.tv_certificates_date)
    TextView tv_certificates_date;


    @Bind(R.id.iv_user_head)
    CircleImageView iv_user_head;
    private AccountDetailBean accountDetailBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_real_name_auth_success;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        accountDetailBean= RxTool.getAccountBean();
        if (null==accountDetailBean){
            return;
        }
        if (!RxSharedPreferencesUtil.getInstance().getBoolean(Constant.CURRENT_LANGUAGE_KEY,true)){//英文版
            iv_status.setImageResource(R.mipmap.verified_success_bg_en);
        }else{
            iv_status.setImageResource(R.mipmap.verified_success_bg);
        }
        RxImageTool.loadLogoImage(this,accountDetailBean.getData().getAvatar(),iv_user_head);
        //此处应该显示真实姓名
//        tv_real_name.setText(accountDetailBean.getData().getRealName());
//        tv_id_card.setText(accountDetailBean.getData().get());
//        tv_certificates_date.setText(accountDetailBean.getData().getAuthTime());
//
//        if (accountDetailBean.getData().getType().equals(Constant.AuthType.IDCARD_TYPE)){//身份证
//            tv_certificates_type.setText(getResources().getString(R.string.id_card));
//        }else{
//            tv_certificates_type.setText(getResources().getString(R.string.passport));
//        }

        RxAnimationTool.zoomOut(iv_status,0f,800);
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
}
