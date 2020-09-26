package com.lianzai.reader.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxLogTool;

import java.io.File;
import java.io.IOException;


/**
 *@description 语音录制
 *@author qicheng.qing
 *@create  17/1/14 11:02
 */
public class RecordButton extends AppCompatTextView {
    private final int Volume_What_100 = 100;
    private final int Time_What_101 = 101;
    private final int CancelRecordWhat_102 = 102;
    private String mFilePath = null;
    private OnRecordListener finishedListener;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 60;
    private long mStartTime;
    private Dialog mDialog;
    private ImageView mImageView;

    ImageView record_audio_cancel;
    private TextView mTitleTv, mTimeTv;
    private MediaRecorder mRecorder;
    private ObtainDecibelThread mthread;
    private Handler mVolumeHandler;
    private int CANCLE_LENGTH = -200;// 上滑取消距离


    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 保存路径，为空取默认值
     *
     * @param path
     */
    public void setSavePath(String path) {
        if (TextUtils.isEmpty(path)) {
            mFilePath = path;
        } else {
            setDefaultFilePath();
        }

    }

    /****
     * 设置最大时间。15秒-10分钟
     *
     * @param time 单位秒
     */
    public void setMaxIntervalTime(int time) {
        if (time > 15 && time < 10 * 60) {
            MAX_INTERVAL_TIME = time * 1000;
        }
    }

    private void setDefaultFilePath() {
        File dir=new File(Environment.getExternalStorageDirectory()+"/LianzaiReader/audio/");
        if (dir.mkdirs()){

        }
        File file = new File(dir, System.currentTimeMillis() + "");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mFilePath = file.getAbsolutePath();
    }

    /**
     * 录音完成的回调
     *
     * @param listener
     */
    public void setOnRecordListener(OnRecordListener listener) {
        finishedListener = listener;
    }


    private void init() {
        mVolumeHandler = new ShowVolumeHandler();
    }

    int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!finishedListener.checkPermission()){
                    return false;
                }

                startY = (int) event.getY();
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
                int endY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (endY - startY < CANCLE_LENGTH) {
                    cancelRecord();
                } else {
                    finishRecord(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int tempNowY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (tempNowY - startY < CANCLE_LENGTH) {
                    mTitleTv.setTextColor(getContext().getResources().getColor(R.color.red_color));
                    mTitleTv.setText(getContext().getString(R.string.record_button_releasing_finger_to_cancel_send));
                    mImageView.setVisibility(View.GONE);
                    record_audio_cancel.setVisibility(View.VISIBLE);
                } else {
                    mTitleTv.setTextColor(getContext().getResources().getColor(R.color.white));
                    mTitleTv.setText(getContext().getString(R.string.record_button_finger_up_to_cancel_send));
                    mImageView.setVisibility(View.VISIBLE);
                    record_audio_cancel.setVisibility(View.GONE);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelRecord();
                break;
        }

        return true;
    }

    private void initDialogAndStartRecord() {
        setText(getResources().getString(R.string.audio_record_press));

        CANCLE_LENGTH = -this.getMeasuredHeight();
        //
        if (TextUtils.isEmpty(mFilePath)) setDefaultFilePath();
        mStartTime = System.currentTimeMillis();
        mDialog = new Dialog(getContext(), R.style.RecordButtonDialog);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recordbutton_alert_dialog, null);
        mImageView = (ImageView) contentView.findViewById(R.id._record_button_dialog_imageview);
        mTimeTv = (TextView) contentView.findViewById(R.id.record_button_dialog_time_tv);
        record_audio_cancel=contentView.findViewById(R.id.record_audio_cancel);
        mTitleTv = (TextView) contentView.findViewById(R.id.record_button_dialog_title_tv);
        mDialog.setContentView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.setOnDismissListener(onDismiss);
        startRecording();
        mDialog.show();
    }

    private void finishRecord(boolean isAutoFinish) {
        setText(getResources().getString(R.string.audio_record_cancel));
        mImageView.setVisibility(View.VISIBLE);
        record_audio_cancel.setVisibility(View.GONE);

        stopRecording();
        mDialog.dismiss();
        long intervalTime = System.currentTimeMillis() - mStartTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.record_button_time_too_short), Toast.LENGTH_SHORT).show();
            File file = new File(mFilePath);
            if (file.exists())
                file.delete();
            return;
        }

        RxLogTool.e("intervalTime:"+intervalTime);

        if (finishedListener != null){
            int sec=(int)(intervalTime% (1000 * 60)) / 1000;
            finishedListener.onFinishedRecord(isAutoFinish?60:sec,mFilePath);
        }
    }

    private void cancelRecord() {
        setText(getResources().getString(R.string.audio_record_cancel));
        mImageView.setVisibility(View.VISIBLE);
        record_audio_cancel.setVisibility(View.GONE);

        stopRecording();
        mDialog.dismiss();
        File file = new File(mFilePath);
        if (file.exists())
            file.delete();
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile(mFilePath);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
        mthread = new ObtainDecibelThread();
        mthread.start();

    }

    private void stopRecording() {
        if (mthread != null) {
            mthread.exit();
            mthread = null;
        }
        if (mRecorder != null) {

            try {
                //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
                //报错为：RuntimeException:stop failed
                mRecorder.setOnErrorListener(null);
                mRecorder.setOnInfoListener(null);
                mRecorder.setPreviewDisplay(null);
                mRecorder.stop();
            } catch (IllegalStateException e) {
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (RuntimeException e) {
                Log.i("Exception", Log.getStackTraceString(e));
            }catch (Exception e) {
                Log.i("Exception", Log.getStackTraceString(e));
            }
            mRecorder.release();
            mRecorder = null;
        }
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mRecorder == null || !running) {
                    break;
                }
                if (System.currentTimeMillis() - mStartTime >= MAX_INTERVAL_TIME) {
                    // 如果超过最长录音时间-自动发送
                    mVolumeHandler.sendEmptyMessage(CancelRecordWhat_102);
                }
                //发送时间
                mVolumeHandler.sendEmptyMessage(Time_What_101);
                //
                int x = mRecorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (20 * Math.log(x) / Math.log(10));
                    Message msg = new Message();
                    msg.obj = f;
                    msg.what = Volume_What_100;
                    mVolumeHandler.sendMessage(msg);
                }

            }
        }

    }

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    class ShowVolumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Volume_What_100:
                    int tempVolumeMax = (int) msg.obj;
                    setLevel(tempVolumeMax);
                    break;
                case Time_What_101:
                    long nowTime = System.currentTimeMillis();
                    int time = ((int) (nowTime - mStartTime) / 1000);
                    int second = time % 60;
                    int mil = time / 60;
                    if (mil < 10) {
                        if (second < 10)
                            mTimeTv.setText("0" + mil + ":0" + second);
                        else
                            mTimeTv.setText("0" + mil + ":" + second);
                    } else if (mil >= 10 && mil < 60) {
                        if (second < 10)
                            mTimeTv.setText(mil + ":0" + second);
                        else
                            mTimeTv.setText(mil + ":" + second);
                    }
                    break;
                case CancelRecordWhat_102:
                    finishRecord(true);
                    break;
            }
        }
    }

    private void setLevel(int level) {

        if (level<17){
            level=0;
        }else if(level<34){
            level=1;
        }else if(level<51){
            level=2;
        }else if(level<68){
            level=3;
        }else if(level<85){
            level=4;
        }else if(level<100){
            level=5;
        }

//        level=3000 + 6000 * level / 100;
        RxLogTool.e("setLevel:"+level);
        if (mImageView != null)
            mImageView.getDrawable().setLevel(level);
    }

    /**
     * 完成录音回调
     */
    public interface OnRecordListener {
        void onFinishedRecord(int length, String audioPath);
        boolean checkPermission();
    }
}
