package com.lianzai.reader.ui.activity.taskCenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.GetTaskIsCompleteByUserIdBean;
import com.lianzai.reader.bean.ReceiveBean;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.bean.UserRelaListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.contract.TaskCenterContract;
import com.lianzai.reader.ui.presenter.TaskCenterPresenter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.CircleImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/24.
 * 新人红包到账页面
 */

public class ActivityGetRedEnvelope extends BaseActivityForTranslucent implements TaskCenterContract.View {

    @Bind(R.id.iv_logo)
    CircleImageView iv_logo;
    @Bind(R.id.nick_tv)
    TextView nick_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;

    @Bind(R.id.ly_1)
    LinearLayout ly_1;
    @Bind(R.id.iv_1)
    ImageView iv_1;
    @Bind(R.id.amount_tv1)
    TextView amount_tv1;
    @Bind(R.id.tv_1)
    TextView tv_1;

    @Bind(R.id.ly_2)
    LinearLayout ly_2;
    @Bind(R.id.iv_2)
    ImageView iv_2;
    @Bind(R.id.amount_tv2)
    TextView amount_tv2;
    @Bind(R.id.tv_2)
    TextView tv_2;

    @Bind(R.id.ly_3)
    LinearLayout ly_3;
    @Bind(R.id.iv_3)
    ImageView iv_3;
    @Bind(R.id.amount_tv3)
    TextView amount_tv3;
    @Bind(R.id.tv_3)
    TextView tv_3;


    @Inject
    TaskCenterPresenter taskCenterPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_get_red_envelope;
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
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        taskCenterPresenter.attachView(this);
        taskCenterPresenter.getTaskIsCompleteByUserId();
    }

    @Override
    public void gc() {

    }

    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.bg_click)
    void bg_clickClick() {
        finish();
        overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_bottom);
    }

    @OnClick(R.id.tv_know_it)
    void tv_know_itClick() {
        ActivityTaskCenter.startActivity(this,0);
        finish();
    }


    @Override
    public void getUserAwakenRelaListSuccess(UserRelaListBean bean) {

    }

    @Override
    public void getUserRelaListSuccess(UserRelaListBean bean) {

    }

    @Override
    public void receiveSuccess(ReceiveBean bean) {

    }

    @Override
    public void dailyCheckSuccess(BaseBean bean) {

    }

    @Override
    public void todayTaskListSuccess(TaskCenterBean bean) {

    }

    @Override
    public void getTaskIsCompleteByUserIdSuccess(GetTaskIsCompleteByUserIdBean bean) {
        //显示红包内容
        RxImageTool.loadLogoImage(this, bean.getData().getHeadImg(), iv_logo);
        nick_tv.setText(bean.getData().getNickName());

        ly_1.setVisibility(View.GONE);
        ly_2.setVisibility(View.GONE);
        ly_3.setVisibility(View.GONE);

        int count = 0;
        if (bean.getData().getGoldDrill() > 0) {
            count++;
            switch (count) {
                case 1:
                    ly_1.setVisibility(View.VISIBLE);
                    iv_1.setImageResource(R.mipmap.jinzuan_get);
                    tv_1.setText("金钻");
                    amount_tv1.setText(String.valueOf((int)bean.getData().getGoldDrill()));
                    break;
                case 2:
                    ly_2.setVisibility(View.VISIBLE);
                    iv_2.setImageResource(R.mipmap.jinzuan_get);
                    tv_2.setText("金钻");
                    amount_tv2.setText(String.valueOf((int)bean.getData().getGoldDrill()));
                    break;
                case 3:
                    ly_3.setVisibility(View.VISIBLE);
                    iv_3.setImageResource(R.mipmap.jinzuan_get);
                    tv_3.setText("金钻");
                    amount_tv3.setText(String.valueOf((int)bean.getData().getGoldDrill()));
                    break;
            }
        }

        if (bean.getData().getGoldCoin() > 0) {
            count++;
            switch (count) {
                case 1:
                    ly_1.setVisibility(View.VISIBLE);
                    iv_1.setImageResource(R.mipmap.jinbi_get);
                    tv_1.setText("金币");
                    amount_tv1.setText(String.valueOf((int)bean.getData().getGoldCoin()));
                    break;
                case 2:
                    ly_2.setVisibility(View.VISIBLE);
                    iv_2.setImageResource(R.mipmap.jinbi_get);
                    tv_2.setText("金币");
                    amount_tv2.setText(String.valueOf((int)bean.getData().getGoldCoin()));
                    break;
                case 3:
                    ly_3.setVisibility(View.VISIBLE);
                    iv_3.setImageResource(R.mipmap.jinbi_get);
                    tv_3.setText("金币");
                    amount_tv3.setText(String.valueOf((int)bean.getData().getGoldCoin()));
                    break;
                default:
                    break;
            }
        }

        if (bean.getData().getVoucher() > 0) {
            count++;
            switch (count) {
                case 1:
                    ly_1.setVisibility(View.VISIBLE);
                    iv_1.setImageResource(R.mipmap.yuejuan_get);
                    tv_1.setText("阅券");
                    amount_tv1.setText(String.valueOf((int)bean.getData().getVoucher()));
                    break;
                case 2:
                    ly_2.setVisibility(View.VISIBLE);
                    iv_2.setImageResource(R.mipmap.yuejuan_get);
                    tv_2.setText("阅券");
                    amount_tv2.setText(String.valueOf((int)bean.getData().getVoucher()));
                    break;
                case 3:
                    ly_3.setVisibility(View.VISIBLE);
                    iv_3.setImageResource(R.mipmap.yuejuan_get);
                    tv_3.setText("阅券");
                    amount_tv3.setText(String.valueOf((int)bean.getData().getVoucher()));
                    break;
                default:
                    break;
            }
        }

        if (bean.getData().getReads() > 0) {
            count++;
            switch (count) {
                case 1:
                    ly_1.setVisibility(View.VISIBLE);
                    iv_1.setImageResource(R.mipmap.yuedian_get);
                    tv_1.setText("阅点");
                    amount_tv1.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                case 2:
                    ly_2.setVisibility(View.VISIBLE);
                    iv_2.setImageResource(R.mipmap.yuedian_get);
                    tv_2.setText("阅点");
                    amount_tv2.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                case 3:
                    ly_3.setVisibility(View.VISIBLE);
                    iv_3.setImageResource(R.mipmap.yuedian_get);
                    tv_3.setText("阅点");
                    amount_tv3.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                default:
                    break;
            }
        }

        if (bean.getData().getDailyTicket() > 0) {
            count++;
            switch (count) {
                case 1:
                    ly_1.setVisibility(View.VISIBLE);
                    iv_1.setImageResource(R.mipmap.tuijianpiao_get);
                    tv_1.setText("推荐票");
                    amount_tv1.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                case 2:
                    ly_2.setVisibility(View.VISIBLE);
                    iv_2.setImageResource(R.mipmap.tuijianpiao_get);
                    tv_2.setText("推荐票");
                    amount_tv2.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                case 3:
                    ly_3.setVisibility(View.VISIBLE);
                    iv_3.setImageResource(R.mipmap.tuijianpiao_get);
                    tv_3.setText("推荐票");
                    amount_tv3.setText(String.valueOf((int)bean.getData().getReads()));
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }
}
