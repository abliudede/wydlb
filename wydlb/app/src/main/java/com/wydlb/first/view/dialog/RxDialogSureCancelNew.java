package com.wydlb.first.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wydlb.first.R;


public class RxDialogSureCancelNew extends RxDialog {

    /*1、修改项目投入人员，2、解雇公司人员，3、招聘公司人员*/
    private int type = 0;

    private TextView mTvTitle;
    private ImageView mIvQuestion;
    private ImageView mIvClose;
    private ImageView mIvSure;

    //当前滑动到的数字
    private int currentNum;
    private TextView mTvCurrentNum;
    //最大可操作的数字
    private Long maxNum;
    private TextView mTvMaxNum;
    //滑动操作条
    private SeekBar seekbar_for_all;
    //滑动条操作描述
    private TextView mTvSeekDes1;
    private TextView mTvSeekDes2;

    //快速数量选择按钮（百分比）
    private TextView mTvQuickBtn1;
    private TextView mTvQuickBtn2;
    private TextView mTvQuickBtn3;
    private TextView mTvQuickBtn4;

    //预计收支展示框
    private RelativeLayout mRlShow;
    private TextView mTvShowDes1;
    private TextView mTvShowDes2;
    private TextView mTvShowDes3;
    private TextView mTvShowDes4;
    private TextView mTvShowNumCost;//具体花费数字
    private TextView mTvShowNumMoreOrLess;//预计节省数字或多支出数字


    //当前余额
    private TextView mTvGold;
    private Long currentGold;


    public RxDialogSureCancelNew(Activity context, int type, Long max, Long gold) {
        super(context, R.style.OptionDialogStyle);
        this.type = type;
        this.maxNum = max;
        this.currentGold = gold;
        initView();
    }

    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_cancel_new, null);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mIvQuestion = dialogView.findViewById(R.id.iv_wenhao);
        mIvClose = dialogView.findViewById(R.id.iv_close);
        mIvSure = dialogView.findViewById(R.id.iv_sure);
        mTvCurrentNum = dialogView.findViewById(R.id.tv_current_num);
        mTvMaxNum = dialogView.findViewById(R.id.tv_max_num);
        seekbar_for_all = dialogView.findViewById(R.id.seekbar_for_all);
        mTvSeekDes1 = dialogView.findViewById(R.id.tv_seek_des1);
        mTvSeekDes2 = dialogView.findViewById(R.id.tv_seek_des2);
        mTvQuickBtn1 = dialogView.findViewById(R.id.tv_quick_btn1);
        mTvQuickBtn2 = dialogView.findViewById(R.id.tv_quick_btn2);
        mTvQuickBtn3 = dialogView.findViewById(R.id.tv_quick_btn3);
        mTvQuickBtn4 = dialogView.findViewById(R.id.tv_quick_btn4);
        mRlShow = dialogView.findViewById(R.id.rl_shouzhi_show);
        mTvShowDes1 = dialogView.findViewById(R.id.tv_show_des1);
        mTvShowDes2 = dialogView.findViewById(R.id.tv_show_des2);
        mTvShowDes3 = dialogView.findViewById(R.id.tv_show_des3);
        mTvShowDes4 = dialogView.findViewById(R.id.tv_show_des4);
        mTvShowNumCost = dialogView.findViewById(R.id.tv_show_num1);
        mTvShowNumMoreOrLess = dialogView.findViewById(R.id.tv_show_num2);
        mTvGold = dialogView.findViewById(R.id.tv_gold);

        setParams();

        //关闭按钮
        mIvClose.setOnClickListener(
                v -> {
                    dismiss();
                }
        );

        //确认按钮
        mIvSure.setOnClickListener(
                v -> {
                    //保存
                    dismiss();
                }
        );

        //最大可操作数字显示
        mTvMaxNum.setText(String.valueOf(maxNum));

        seekbar_for_all.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置当前的数字
                currentNum = (int) ((progress * maxNum) / 100);
                mTvCurrentNum.setText(String.valueOf(currentNum));
                if (type == 2) {
                    mTvShowNumCost.setText(String.valueOf(currentNum * 5000));
                    mTvShowNumMoreOrLess.setText(String.valueOf(currentNum * 5000));
                } else if (type == 3) {
                    mTvShowNumCost.setText(String.valueOf(currentNum * 5000));
                    mTvShowNumMoreOrLess.setText(String.valueOf(currentNum * 5000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTvQuickBtn1.setOnClickListener(
                v -> {
                    seekbar_for_all.setProgress(25);
                }
        );
        mTvQuickBtn2.setOnClickListener(
                v -> {
                    seekbar_for_all.setProgress(50);
                }
        );
        mTvQuickBtn3.setOnClickListener(
                v -> {
                    seekbar_for_all.setProgress(75);
                }
        );
        mTvQuickBtn4.setOnClickListener(
                v -> {
                    seekbar_for_all.setProgress(100);
                }
        );


        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
    }

    public void reset(int type, Long max, Long gold) {
        this.type = type;
        this.maxNum = max;
        this.currentGold = gold;
    }
    public void setParams(){
        //根据不同情况显示不同的视图
        /*1、修改项目投入人员，2、解雇公司人员，3、招聘公司人员*/
        if (type == 1) {
            mTvTitle.setText("修改投入人员");
            mTvSeekDes1.setText("已投入人员数");
            mTvSeekDes2.setText("可用人员数");
            mRlShow.setVisibility(View.GONE);
            mTvGold.setVisibility(View.GONE);
        } else if (type == 2) {
            mTvTitle.setText("解雇");
            mTvSeekDes1.setText("本次解雇人数");
            mTvSeekDes2.setText("可解雇空闲人数");
            mRlShow.setVisibility(View.VISIBLE);
            mTvGold.setVisibility(View.VISIBLE);
            mTvShowDes2.setText("元解雇费");
            mTvShowDes3.setText("后续每月节省");
            mTvGold.setText("当前月：" + currentGold);
        } else if (type == 3) {
            mTvTitle.setText("招聘");
            mTvSeekDes1.setText("本次招聘人数");
            mTvSeekDes2.setText("可招聘人数");
            mRlShow.setVisibility(View.VISIBLE);
            mTvGold.setVisibility(View.VISIBLE);
            mTvShowDes2.setText("元招聘费");
            mTvShowDes3.setText("后续每月支出");
            mTvGold.setText("当前月：" + currentGold);
        }
    }

}
