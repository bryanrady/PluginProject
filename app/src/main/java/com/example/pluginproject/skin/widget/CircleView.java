package com.example.pluginproject.skin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.pluginproject.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class CircleView extends View {

    private Paint mTextPaint;
    private int mCircleColorResId;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        int color = typedArray.getColor(R.styleable.CircleView_circleColor, 0);
        mCircleColorResId = typedArray.getResourceId(R.styleable.CircleView_circleColor, 0);
        typedArray.recycle();

        mTextPaint = new Paint();
        mTextPaint.setColor(color);
        mTextPaint.setAntiAlias(true);
        //设置文本位于相对于原点的中间
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取宽度一半
        int width = getWidth() / 2;
        //获取高度一半
        int height = getHeight() / 2;
        //设置半径为宽或者高的最小值
        int radius = Math.min(width, height);
        //利用canvas画一个圆
        canvas.drawCircle(width, height, radius, mTextPaint);
    }

    public void setCircleColor(@ColorInt int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

}
