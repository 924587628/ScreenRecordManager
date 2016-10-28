package com.bzt.screenrecordmanager.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bzt.screenrecordmanager.R;
import com.bzt.screenrecordmanager.util.ScreenUtils;

/**
 * Created by sunxy on 2016/7/14.
 */

public class ClipFragmentView extends LinearLayout  {

    private Context context = null;

    public ClipFragmentView(Context context) {
        this(context, null);
    }

    public ClipFragmentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipFragmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ClipFragmentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.context = context;

        initView();
    }

    private ImageView mImgLeft;
    private ImageView mImgRight;

    private FrameLayout frameLayout;

    private boolean isLeftMove;
    private boolean isRightMove;

    private ClipBarView mClip;

    private void initView() {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_clip, null);
        addView(view);

        frameLayout = (FrameLayout) view.findViewById(R.id.touch_outside);
        mImgLeft = (ImageView) view.findViewById(R.id.left);
        mImgRight = (ImageView) view.findViewById(R.id.right);
        mClip = (ClipBarView) view.findViewById(R.id.clipbarview);


    }

}
