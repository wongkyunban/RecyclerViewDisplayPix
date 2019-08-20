package com.jk.displaypix;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PixIndicator extends View {
    private int number;
    private int position = 0;
    private Paint paint = new Paint();
    private int selectColor;
    private int unselectColor;
    private float radius;
    private float space;

    public PixIndicator(Context context) {
        this(context,null);
    }

    public PixIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PixIndicator);
        this.selectColor = typedArray.getColor(R.styleable.PixIndicator_selectColor, Color.RED);
        this.unselectColor = typedArray.getColor(R.styleable.PixIndicator_unselectedColor, Color.BLACK);
        this.radius = typedArray.getDimension(R.styleable.PixIndicator_radius, 10);
        this.space = typedArray.getDimension(R.styleable.PixIndicator_space, 20);
        typedArray.recycle();

    }

    {
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startPosition = ((float)getWidth()) / 2 - (radius * 2 * number + space * (number - 1)) / 2;

        canvas.save();
        for (int i = 0; i < number; i++) {
            if (i == position) {
                paint.setColor(selectColor);
            } else {
                paint.setColor(unselectColor);
            }
            canvas.drawCircle(startPosition + radius * (2 * i + 1) + i * space, ((float)getHeight()) / 2, radius, paint);
        }
        canvas.restore();

    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPosition(int position) {
        this.position = position;
        invalidate();
    }

}