package com.bzt.screenrecordmanager.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bzt.screenrecordmanager.R;

/**
 * Created by sunxy on 2016/7/14.
 */

public class ClipBarView extends View {

    private Context context = null;
    private Paint paint = null;

    public ClipBarView(Context context) {
        this(context, null);
    }

    public ClipBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ClipBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(10f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        canvas.drawLine(0, 10, this.getWidth(), 10, paint);

        canvas.restore();
    }
}
