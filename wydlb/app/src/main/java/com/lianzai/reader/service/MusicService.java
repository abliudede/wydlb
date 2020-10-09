package com.lianzai.reader.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Pair;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.ui.activity.UrlIdentification.baiduUtils.InitConfig;
import com.lianzai.reader.ui.activity.UrlIdentification.baiduUtils.MessageListener;
import com.lianzai.reader.ui.activity.UrlIdentification.baiduUtils.MySyntherizer;
import com.lianzai.reader.ui.activity.UrlIdentification.baiduUtils.NonBlockSyntherizer;
import com.lianzai.reader.ui.activity.UrlIdentification.baiduUtils.OfflineResource;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.page.PageParagraphVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/5/22.
 * 后台播放
 *      基础版：四步走
 */

public class MusicService extends Service {


    // 百度语音主控制类，所有合成控制方法从这个类开始
    private MySyntherizer synthesizer;
    boolean isStopThread;//是否停止线程
    boolean hasShow = false;

    private final int notificationId = 1112;

    @Override
    public void onCreate() {
        super.onCreate();
//        RxLogTool.e("音频","onCreate()");
        try {
             initialTts(); // 初始化TTS引擎
        } catch (Exception e) {

        }
    }


    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        isStopThread=false;
        return new MyBinder();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        //【4】关闭线程并释放资源
        isStopThread=true;//避免线程仍在走

        //释放听书资源
        if (null != synthesizer) {
            synthesizer.release();
            synthesizer = null;
        }
        if (Constant.isListenBook) {
            Constant.isListenBook = false;
            RxToast.custom("已退出听书模式").show();
        }

        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder implements MyOperation {

        private boolean isStop = true;
        MusicService getService(){
            return MusicService.this;
        }

        @Override
        public void play(List<PageParagraphVo> pageParagraphVos) {
            try{
                //播放
                isStop = false;
                if(null != synthesizer) {
                    batchSpeak(pageParagraphVos);
                }
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(notificationId,notification);
                // 参数一：唯一的通知标识；参数二：通知消息。
                if(!hasShow) {
                    hasShow = true;
                    Notification notification = showshowNotification("连载听书正在播放");
                    startForeground(notificationId, notification);// 开始前台服务
                }
            }catch (Exception e){

            }
        }


        @Override
        public void moveon() {
            try{
                //继续播放
                if (null != synthesizer) {
                    isStop = false;
                    synthesizer.resume();
                }
            }catch (Exception e){

            }

        }

        @Override
        public void stop() {
            try{
                if (null != synthesizer) {
                    isStop = true;
                    synthesizer.pause();
                }
            }catch (Exception e){

            }
        }

        @Override
        public void setParams(int type) {
            try{
                //有网络的情况下，不用重新读取本地文件。
                if(type == 1){
                    //直接设置语速
                    if(null != synthesizer){
                        int speed = ReadSettingManager.getInstance().getVoiceSpeed();
                        int temp = 0;
                        if (speed < 10) {
                            temp = 0;
                        } else if (speed < 20) {
                            temp = 1;
                        } else if (speed < 30) {
                            temp = 2;
                        } else if (speed < 40) {
                            temp = 3;
                        } else if (speed < 50) {
                            temp = 4;
                        } else if (speed < 60) {
                            temp = 5;
                        } else if (speed < 70) {
                            temp = 6;
                        } else if (speed < 80) {
                            temp = 7;
                        } else if (speed < 90) {
                            temp = 8;
                        } else if (speed <= 100) {
                            temp = 9;
                        }
                        synthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, String.valueOf(temp));
                    }
                }else {
                    int voicer = Integer.parseInt(ReadSettingManager.getInstance().getVoicer());
                    synthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, String.valueOf(voicer));
                    loadModel(voicer);
                }
            }catch (Exception e){

            }
        }

        @Override
        public void changeVoice() {
            try{
                //停止听书
                if (null != synthesizer) {
                    isStop = true;
                    synthesizer.stop();
                }
            }catch (Exception e){

            }

        }

        //退出听书模式并且隐藏通知
        @Override
        public void dissmissNotification() {
            try{
                //停止听书
                if (null != synthesizer) {
                    isStop = true;
                    synthesizer.stop();
                }
                stopForeground(true);
            }catch (Exception e){

            }

        }

        public boolean isStop() {
            return isStop;
        }
    }

    /*** 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        MessageListener listener = new MessageListener();
        TtsMode ttsMode = TtsMode.MIX;
        int speed = ReadSettingManager.getInstance().getVoiceSpeed();
        int voicer = Integer.parseInt(ReadSettingManager.getInstance().getVoicer());
        int temp = 0;
        if (speed < 10) {
            temp = 0;
        } else if (speed < 20) {
            temp = 1;
        } else if (speed < 30) {
            temp = 2;
        } else if (speed < 40) {
            temp = 3;
        } else if (speed < 50) {
            temp = 4;
        } else if (speed < 60) {
            temp = 5;
        } else if (speed < 70) {
            temp = 6;
        } else if (speed < 80) {
            temp = 7;
        } else if (speed < 90) {
            temp = 8;
        } else if (speed <= 100) {
            temp = 9;
        }
        Map<String, String> params = getParams(temp, voicer);
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(Constant.BAIDUAPPID, Constant.BAIDUAPPKEY, Constant.BAIDUAPPSECRECT, ttsMode, params, listener);
        synthesizer = new NonBlockSyntherizer(this, initConfig); // 此处可以改为MySyntherizer 了解调用过程
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams(int speed, int voice) {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填

        if (voice != -1) {
            // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
            params.put(SpeechSynthesizer.PARAM_SPEAKER, String.valueOf(voice));
        }
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        if (speed != -1) {
            // 设置合成的语速，0-9 ，默认 5
            params.put(SpeechSynthesizer.PARAM_SPEED, String.valueOf(speed));
        }
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        if (voice != -1) {
            String offlineVoice = OfflineResource.VOICE_MALE;
            switch (voice) {
                case 0:
                    offlineVoice = OfflineResource.VOICE_FEMALE;
                    break;
                case 1:
                    offlineVoice = OfflineResource.VOICE_MALE;
                    break;
                case 4:
                    offlineVoice = OfflineResource.VOICE_DUYY;
                    break;
                case 3:
                    offlineVoice = OfflineResource.VOICE_DUXY;
                    break;
            }
            OfflineResource offlineResource = createOfflineResource(offlineVoice);
            // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                    offlineResource.getModelFilename());
        }

        return params;
    }

    private void loadModel(int voicer) {
        String offlineVoice = OfflineResource.VOICE_MALE;
        switch (voicer) {
            case 0:
                offlineVoice = OfflineResource.VOICE_FEMALE;
                break;
            case 1:
                offlineVoice = OfflineResource.VOICE_MALE;
                break;
            case 4:
                offlineVoice = OfflineResource.VOICE_DUYY;
                break;
            case 3:
                offlineVoice = OfflineResource.VOICE_DUXY;
                break;

        }
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        int result = synthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
        return offlineResource;
    }

    /**
     * 批量播放
     */
    private boolean batchSpeak(List<PageParagraphVo> pageParagraphVos) {
        try {
            List<Pair<String, String>> texts = new ArrayList<Pair<String, String>>();
            for (PageParagraphVo item : pageParagraphVos) {
                texts.add(new Pair<String, String>(item.text, String.valueOf(item.paragraphPosition)));
            }
            int result = synthesizer.batchSpeak(texts);
            if (result == 0) {
                return true;
            }
        } catch (Exception e) {

        }
        RxToast.custom("听书初始化异常").show();
        RxEventBusTool.sendEvents(Constant.EventTag.LISTEN_EXIT);
        return false;
    }


    /*
     * 弹出通知框
     */
    private Notification showshowNotification(String text) {


        //ChannelId为"1",ChannelName为"Channel1"
        NotificationChannel channel = null;
        NotificationCompat.Builder mBuilder = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(this,"chat");
        }else {
            mBuilder = new NotificationCompat.Builder(this);
        }

        mBuilder.setContentTitle("连载神器")//设置通知栏标题
                .setContentText(text) //设置通知栏显示内容
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT)) //设置通知栏点击意图
//	.setNumber(number) //设置通知集合的数量
                .setTicker(text) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
	            .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setDefaults(NotificationCompat.FLAG_ONGOING_EVENT)
                .setVibrate(new long[]{0})
                .setSound(null)
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_share);//设置通知小ICON

        return mBuilder.build();
    }

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

}