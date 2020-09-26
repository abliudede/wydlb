package com.lianzai.reader.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.model.local.ReadSettingManager;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReadAutoPageDialog extends Dialog {


    Context mContext;

    @Bind(R.id.read_setting_ll_menu)
    LinearLayout read_setting_ll_menu;

    @Bind(R.id.tv_exit_listen_book)
    public TextView tv_exit_listen_book;

    @Bind(R.id.read_setting_sb_voice_speed)
    SeekBar read_setting_sb_voice_speed;

    long autoPageTime;

    ListenModeChangeCallback listenModeChangeCallback;

    @Bind(R.id.view_line)
    View view_line;
    @Bind(R.id.tv_slow)
    TextView tv_slow;
    @Bind(R.id.tv_quick)
    TextView tv_quick;

    public ReadAutoPageDialog(Context context, ListenModeChangeCallback mListenModeChangeCallback, long autoPageTime) {
        super(context, R.style.ReadSettingDialog);
        mContext=context;
        this.listenModeChangeCallback=mListenModeChangeCallback;
        this.autoPageTime = autoPageTime;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_auto_page);
        ButterKnife.bind(this);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(attributes);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        //数据初始化
        initData();
    }


    /**
     * 切换夜间模式
     * @param isNightMode
     */
    public void toggleDayNightMode(boolean isNightMode){
        if (null!=read_setting_ll_menu){
            float alpha;
            int bgColor=mContext.getResources().getColor(R.color.white);
            if (isNightMode){
                bgColor=mContext.getResources().getColor(R.color.read_night_bg_color);
                read_setting_ll_menu.setBackgroundColor(bgColor);
                view_line.setBackgroundColor(mContext.getResources().getColor(R.color.colorf5f5f5_alpha1));
                tv_slow.setTextColor(mContext.getResources().getColor(R.color.color_white_5alpha));
                tv_quick.setTextColor(mContext.getResources().getColor(R.color.color_white_5alpha));
                alpha=0.5f;
            }else{
                read_setting_ll_menu.setBackgroundColor(bgColor);
                view_line.setBackgroundColor(mContext.getResources().getColor(R.color.colorf5f5f5));
                tv_slow.setTextColor(mContext.getResources().getColor(R.color.color_black_ff333333));
                tv_quick.setTextColor(mContext.getResources().getColor(R.color.color_black_ff333333));
                alpha=1f;
            }
            read_setting_sb_voice_speed.setAlpha(alpha);
        }



    }

    private void initData(){
        //语速
        int progress = (int) ((autoPageTime - 3000)*100/37000);
        read_setting_sb_voice_speed.setProgress(100 - progress);
        read_setting_sb_voice_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int speed = 100 - seekBar.getProgress();
                //设置自动翻页速度
                autoPageTime = speed*37000/100 + 3000;
                if (null!=listenModeChangeCallback){
                    listenModeChangeCallback.changePageSpeed(autoPageTime);
                }
            }
        });


    }

    /**
     * 改变翻页速度的回调
     */
    public interface ListenModeChangeCallback{
        void changePageSpeed(long speed);
    }

}
