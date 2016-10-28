package com.bzt.screenrecordmanager.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.bzt.screenrecordmanager.util.Utils;

import java.io.IOException;


public class RecordService extends Service {
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;


    private boolean running;
    //录制宽度
    private int width = 0;
    //录制高度
    private int height = 0;
    private int dpi;

    private String fileName;


    @Override
    public IBinder onBind(Intent intent) {

        return new RecordBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    //服务石头启动
    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }

        fileName = Utils.getsaveDirectory() + System.currentTimeMillis() + ".mp4";

        initRecorder();
        createVirtualDisplay();
        mediaRecorder.start();
        running = true;
        return true;
    }

    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        virtualDisplay.release();
        mediaProjection.stop();

        return true;
    }

    private void createVirtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(),
                null, null);
    }

    private void initRecorder() {
        //设置音频源麦克风
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //视频源 桌面
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //3GPP格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //导出目录
        mediaRecorder.setOutputFile(fileName);
        //显示大小
        mediaRecorder.setVideoSize(width, height);
//        Log.d("QQ", "width  =  " + width + "  --    height  =  " + height);
        //视频编码
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //音频编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置视频编码比特率 不要改
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        //设置要捕获的视频的帧速率 不要改
        mediaRecorder.setVideoFrameRate(30);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class RecordBinder extends Binder {
        public RecordService getRecordService() {
            return RecordService.this;
        }
    }

    /**
     * /storage/emulated/0/ScreenRecord/1468388775577.mp4
     * /storage/emulated/0/ScreenRecord/1468388929444.mp4
     * @return
     */
    public String getCurrentFilePath(){
        return fileName;
    }
}