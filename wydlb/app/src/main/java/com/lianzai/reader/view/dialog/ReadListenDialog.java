package com.lianzai.reader.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.ui.activity.listenPay.ActivityListenPay;
import com.lianzai.reader.utils.TimeFormatUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReadListenDialog extends Dialog {


    Context mContext;

    @Bind(R.id.read_setting_ll_menu)
    LinearLayout read_setting_ll_menu;

    @Bind(R.id.read_setting_rg_voice_style)
    RadioGroup read_setting_rg_voice_style;

    @Bind(R.id.read_setting_rg_voice_timer)
    RadioGroup read_setting_rg_voice_timer;

    @Bind(R.id.tv_exit_listen_book)
    public TextView tv_exit_listen_book;

    @Bind(R.id.read_setting_sb_voice_speed)
    SeekBar read_setting_sb_voice_speed;


    @Bind(R.id.view_line)
            View view_line;

    String[] mCloudVoicerValue;//发音人

    ReadSettingManager readSettingManager;

    String voicer;
    int voiceSpeed;

    @Bind(R.id.tv_slow)
    TextView tv_slow;

    @Bind(R.id.tv_quick)
    TextView tv_quick;


    @Bind(R.id.read_setting_rb_voice_normal_girl)
    RadioButton read_setting_rb_voice_normal_girl;

    @Bind(R.id.read_setting_rb_voice_normal_boy)
    RadioButton read_setting_rb_voice_normal_boy;

    @Bind(R.id.read_setting_rb_voice_emotion_girl)
    RadioButton read_setting_rb_voice_emotion_girl;

    @Bind(R.id.read_setting_rb_voice_emotion_boy)
    RadioButton read_setting_rb_voice_emotion_boy;

    @Bind(R.id.read_setting_rb_timer_none)
    RadioButton read_setting_rb_timer_none;

    @Bind(R.id.read_setting_rb_timer_fifteen_min)
    RadioButton read_setting_rb_timer_fifteen_min;

    @Bind(R.id.read_setting_rb_timer_thirty_min)
    RadioButton read_setting_rb_timer_thirty_min;

    @Bind(R.id.read_setting_rb_timer_sixty_min)
    RadioButton read_setting_rb_timer_sixty_min;


    @Bind(R.id.iv_listen_book_support)
    ImageView iv_listen_book_support;

    @Bind(R.id.tv_more)
    TextView tv_more;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.rl_listen_time)
    RelativeLayout rl_listen_time;




    ListenModeChangeCallback listenModeChangeCallback;
    private long listenSecond;

    public ReadListenDialog(Context context,ListenModeChangeCallback mListenModeChangeCallback) {
        super(context, R.style.ReadSettingDialog);
        mContext=context;
        this.listenModeChangeCallback=mListenModeChangeCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_listen_book);
        ButterKnife.bind(this);
        setUpWindow();

        readSettingManager=ReadSettingManager.getInstance();
        mCloudVoicerValue= mContext.getResources().getStringArray(R.array.voicer_cloud_values);

        //数据初始化
        initData();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }


    /**
     * 切换夜间模式
     * @param isNightMode
     */
    public void toggleDayNightMode(boolean isNightMode,int listenMin){
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
                iv_listen_book_support.setImageResource(R.mipmap.icon_listen_book_support_night);
            }else{
                iv_listen_book_support.setImageResource(R.mipmap.icon_listen_book_support);

                read_setting_ll_menu.setBackgroundColor(bgColor);

                view_line.setBackgroundColor(mContext.getResources().getColor(R.color.colorf5f5f5));

                tv_slow.setTextColor(mContext.getResources().getColor(R.color.color_black_ff333333));
                tv_quick.setTextColor(mContext.getResources().getColor(R.color.color_black_ff333333));

                alpha=1f;
            }
            read_setting_sb_voice_speed.setAlpha(alpha);

            read_setting_rb_voice_normal_girl.setAlpha(alpha);
            read_setting_rb_voice_emotion_boy.setAlpha(alpha);
            read_setting_rb_voice_emotion_girl.setAlpha(alpha);
            read_setting_rb_voice_normal_boy.setAlpha(alpha);

            read_setting_rb_timer_none.setAlpha(alpha);
            read_setting_rb_timer_fifteen_min.setAlpha(alpha);
            read_setting_rb_timer_thirty_min.setAlpha(alpha);
            read_setting_rb_timer_sixty_min.setAlpha(alpha);

            read_setting_rb_voice_normal_girl.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_voice_normal_boy.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_voice_emotion_girl.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_voice_emotion_boy.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));

            read_setting_rb_timer_none.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_timer_fifteen_min.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_timer_thirty_min.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_timer_sixty_min.setBackground(isNightMode?mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mContext.getResources().getDrawable(R.drawable.selector_btn_read_setting));

            if (listenMin==0){
                //默认60分钟
                read_setting_rg_voice_timer.check(R.id.read_setting_rb_timer_none);
            }else if(listenMin==15){
                read_setting_rg_voice_timer.check(R.id.read_setting_rb_timer_fifteen_min);
            }else if(listenMin==30){
                read_setting_rg_voice_timer.check(R.id.read_setting_rb_timer_thirty_min);
            }else if(listenMin==60){
                read_setting_rg_voice_timer.check(R.id.read_setting_rb_timer_sixty_min);
            }
        }
    }

    public void setListenTime(long second,boolean show){
        listenSecond = second;
        //用于刷新
        if(null != tv_des){
            //改变样式
            String listenTime = TimeFormatUtil.getFormatTimeDHM(listenSecond);
            SpannableString mSpannableString = new SpannableString(listenTime);
            mSpannableString.setSpan(new TextAppearanceSpan(mContext, R.style.YellowText), 7, listenTime.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_des.setText(mSpannableString);
        }
        if(listenSecond > 0 && show){
            if(null != rl_listen_time){
                rl_listen_time.setVisibility(View.VISIBLE);
            }
        }else {
            if(null != rl_listen_time){
                rl_listen_time.setVisibility(View.GONE);
            }
        }
    }

    private void initData(){
        tv_more.setOnClickListener(
                v -> {
                    ActivityListenPay.startActivity(mContext);
                    dismiss();
                }
        );
        //改变样式
//        String listenTime = TimeFormatUtil.getFormatTimeDHM(listenSecond);
//        SpannableString mSpannableString = new SpannableString(listenTime);
//        mSpannableString.setSpan(new TextAppearanceSpan(mContext, R.style.YellowText), 7, listenTime.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv_des.setText(mSpannableString);

        voicer=readSettingManager.getVoicer();
        voiceSpeed=readSettingManager.getVoiceSpeed();
        //语速
        read_setting_sb_voice_speed.setProgress(voiceSpeed);
        read_setting_sb_voice_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int speed=seekBar.getProgress();

                if (null!=listenModeChangeCallback){
                    listenModeChangeCallback.changeVoiceSpeed(speed);
                }

//                TtsHelp.setVoiceSpeed(speed);//设置语速，并恢复播放
//                readSettingManager.setVoiceSpeed(speed);
            }
        });

        for(int i=0;i<mCloudVoicerValue.length;i++){
            if (mCloudVoicerValue[i].equals(voicer)){
                if (i==0){
                    read_setting_rg_voice_style.check(R.id.read_setting_rb_voice_normal_girl);
                }else if(i==1){
                    read_setting_rg_voice_style.check(R.id.read_setting_rb_voice_normal_boy);
                }else if(i==2){
                    read_setting_rg_voice_style.check(R.id.read_setting_rb_voice_emotion_girl);
                }else if(i==3){
                    read_setting_rg_voice_style.check(R.id.read_setting_rb_voice_emotion_boy);
                }
            }
        }

        //发音人设置
        read_setting_rg_voice_style.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_voice_normal_girl){

                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.changeVoicer(mCloudVoicerValue[0]);
                    }
                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_voice_normal_boy){
                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.changeVoicer(mCloudVoicerValue[1]);
                    }

                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_voice_emotion_girl){

                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.changeVoicer(mCloudVoicerValue[2]);
                    }
                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_voice_emotion_boy){
                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.changeVoicer(mCloudVoicerValue[3]);
                    }
                }
            }
        });

        //听书定时
        read_setting_rg_voice_timer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_timer_none){

                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.listenTimer(0);
                    }
                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_timer_fifteen_min){
                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.listenTimer(15);
                    }

                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_timer_thirty_min){

                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.listenTimer(30);
                    }
                }else if (radioGroup.getCheckedRadioButtonId()==R.id.read_setting_rb_timer_sixty_min){

                    if (null!=listenModeChangeCallback){
                        listenModeChangeCallback.listenTimer(60);
                    }
                }
            }
        });
    }

    /**
     * 改变听书的语速，声音回调
     */
    public interface ListenModeChangeCallback{
        void changeVoiceSpeed(int speed);
        void changeVoicer(String voicer);
        void listenTimer(int min);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//    }


}
