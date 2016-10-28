package com.bzt.screenrecordmanager.util;

import android.util.Log;

/**
 * Created by sunxy on 2016/7/26.
 */

public class MyThread extends Thread {

    private boolean isPlaying = true;

    @Override
    public void run() {

        while (isPlaying){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("sunxy","  run()   ... ");

            if (playingStateListener != null)
                playingStateListener.onFinish();
        }
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    private OnPlayingStateListener playingStateListener;

    public void setPlayingStateListener(OnPlayingStateListener playingStateListener) {
        this.playingStateListener = playingStateListener;
    }

    public interface OnPlayingStateListener{
        void onFinish();
    }
}
