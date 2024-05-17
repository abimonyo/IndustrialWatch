package com.app.industrialwatch.app.module.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.app.industrialwatch.R;

public class CircularProgressBar extends View {
    private Paint progressPaint;
    private Paint backgroundPaint;
    private RectF rectF;
    private float strokeWidth = 20f;
    private float progress = 0;
    private int progressColor = 0xFF00FF00; // Default green
    private int backgroundColor = 0xFF000000; // Default black

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public CircularProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);
            try {
                strokeWidth = a.getDimension(R.styleable.CircularProgressBar_strokeWidth, strokeWidth);
                progress = a.getFloat(R.styleable.CircularProgressBar_progress, progress);
                progressColor = a.getColor(R.styleable.CircularProgressBar_progressColor, progressColor);
                backgroundColor = a.getColor(R.styleable.CircularProgressBar_backgroundColor, backgroundColor);
            } finally {
                a.recycle();
            }
        }

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setColor(progressColor);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int smallest = Math.min(width, height);
        float half = smallest / 2f;
        float radius = half - strokeWidth / 2f;

        rectF.set(half - radius, half - radius, half + radius, half + radius);

        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        canvas.drawArc(rectF, -90, angle, false, progressPaint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }
}
