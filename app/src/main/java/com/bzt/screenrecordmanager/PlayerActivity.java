package com.bzt.screenrecordmanager;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bzt.screenrecordmanager.clip.RangeBar;
import com.bzt.screenrecordmanager.config.Config;
import com.bzt.screenrecordmanager.util.MyThread;
import com.bzt.screenrecordmanager.util.TrimmVideo;

/**
 * Created by sunxy on 2016/7/13.
 */
public class PlayerActivity extends AppCompatActivity implements
        MediaController.MediaPlayerControl,
        View.OnTouchListener,
        RangeBar.OnRangeBarChangeListener,
        MyThread.OnPlayingStateListener {

    private VideoView mVideoView = null;
    private MediaController controller = null;

    private String filePath = "";

    private RangeBar rangeBar;

    //左边的位置
    private int leftThumbIndex;
    //右边的位置
    private int rightThumbIndex;

    //视屏剪辑
    private AsyncTask<Void, Void, Void> trimmVideo;
    //视屏合成
    private AsyncTask<String, Integer, String> mergeVideos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        setContentView(R.layout.activity_main_player);

        initView();
    }

    private void initData() {
        filePath = getIntent().getStringExtra(Config.File_Path);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initView() {
        rangeBar = (RangeBar) findViewById(R.id.range);
        rangeBar.setOnRangeBarChangeListener(this);

        mVideoView = (VideoView) findViewById(R.id.videoview);
        controller = new MediaController(this);
        mVideoView.setMediaController(controller);
        mVideoView.setVideoPath(filePath);

        mVideoView.setOnTouchListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                rangeBar.setTickCount(getDuration());
            }
        });

    }

    private MyThread thread;

    @Override
    public void start() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            thread = new MyThread();
            thread.start();
            thread.setPlayingStateListener(this);
        }
        Log.d("sunxy", " start()  ....   " + mVideoView.isPlaying());
    }

    @Override
    public void pause() {
        if (mVideoView.isPlaying() && mVideoView.canPause()) {
            mVideoView.canPause();
            thread.setPlaying(false);
            thread.stop();
        }

    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mVideoView.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mVideoView.getBufferPercentage();
    }

    @Override
    public boolean canPause() {

        return mVideoView.canPause();
    }

    @Override
    public boolean canSeekBackward()
    {
        return mVideoView.canSeekBackward();
    }

    @Override
    public boolean canSeekForward() {
        return mVideoView.canSeekForward();
    }

    @Override
    public int getAudioSessionId() {
        return mVideoView.getAudioSessionId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView = null;
        controller = null;
    }

    /**
     * videoview的触摸事件
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
        this.leftThumbIndex = leftThumbIndex;
        this.rightThumbIndex = rightThumbIndex;
        seekTo(leftThumbIndex);

    }

    /**
     * 剪辑
     */
    public void onClip(View view) {
        trimmVideo = new TrimmVideo(this, filePath, leftThumbIndex / 1000, Math.abs((rightThumbIndex - leftThumbIndex) / 1000));
        trimmVideo.execute();
        ((TrimmVideo) trimmVideo).setClipCompletedListener(new TrimmVideo.OnClipCompletedListener() {
            @Override
            public void onSuccess(String text, String videoPath) {
                Bundle bundle = new Bundle();
                bundle.putString("text", text);
                bundle.putString("videoPath", videoPath);
                bundle.putInt("state", 1);
                Message message = new Message();
                message.setData(bundle);

                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e, String text) {
                Bundle bundle = new Bundle();
                bundle.putString("text", text);
                bundle.putString("e", e.getMessage());
                bundle.putInt("state", 0);
                Message message = new Message();
                message.setData(bundle);

                handler.sendMessage(message);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();

            int state = bundle.getInt("state");

            switch (state) {
                case 1://成功
                    showToast(bundle.getString("text") + "\n" + "路径 : " + bundle.getString("videoPath"));
                    break;
                case 0://失败
                    showToast(bundle.getString("text") + "\n" + "原因 : " + bundle.getString("e"));
                    break;
            }
        }
    };

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        if (rightThumbIndex != 0) {
            if (rightThumbIndex == getCurrentPosition()) {
                pause();
            }
        }
    }
}
