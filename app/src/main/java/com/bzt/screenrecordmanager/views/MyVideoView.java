package com.bzt.screenrecordmanager.views;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.VideoView;

import com.bzt.screenrecordmanager.PlayerActivity;

/**
 * Created by sunxy on 2016/7/13.
 */

public class MyVideoView extends VideoView {

    private Context context;

    public MyVideoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub0 ~3 K( G/ M( B# I) `
        this.context = context;
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        //重点。
                int width = getDefaultSize(0, widthMeasureSpec);
                int height = getDefaultSize(0, heightMeasureSpec);
                Log.d("QQ", "width  =  " + width + "  --    height  =  " + height);
                setMeasuredDimension(width, height);
    }

    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        // TODO Auto-generated method stub
        super.setOnPreparedListener(l);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

}
