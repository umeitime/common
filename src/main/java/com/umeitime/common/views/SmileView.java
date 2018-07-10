package com.umeitime.common.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.umeitime.common.R;

/**
 * Created by moluls on 2017/4/21.
 */

public class SmileView extends View {

    private static final int DEFAULT_SIZE = 60;

    private static final int SmileSpaceAngle = 50;
    private static final int MIN_SWEEP_ANGLE = 180;
    private static final int MAX_SWEEP_ANGLE = 270;

    //笑脸位置 宽度
    private float mStartAngle = 0;
    private float mSweepAngle = 180;


    //眼睛位置 眼睛宽度
    private float mSmileAngle1 = 270 - SmileSpaceAngle;
    private float mSmileAngle2 = 270 + SmileSpaceAngle;
    private float mSmileSweepAngle = 0;


    private ValueAnimator mAnimator;


    private float mRadius;
    private float mStrokeWidth;
    private Point mCenter;
    private Paint mPaint;
    private RectF mRectF;
    private int mSmileColor = 0xff02bbfe;

    public SmileView(Context context) {
        super(context);
    }

    public SmileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmileView);
        mSmileColor = typedArray.getColor(R.styleable.SmileView_smileColor, 0xff02bbfe);
        init();
    }

    public SmileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void startAnimation() {
        stopAnimation();
        setAnimator(1500);
        mAnimator.start();
    }

    public void startAnimation(long time) {
        stopAnimation();
        setAnimator(time);
        mAnimator.start();
    }

    public boolean isAnimationRun() {
        return mAnimator == null ? false : mAnimator.isRunning();
    }

    public void stopAnimation() {
        if (mAnimator != null) {
            clearAnimation();
            mAnimator.setRepeatCount(0);
            mAnimator.cancel();
            mAnimator.end();
        }
    }


    private void init() {
        initPaint();
        setAnimator(1500);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
                int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

                mCenter = new Point(width / 2, height / 2);
                mRadius = Math.min(width, height) * 2 / 5;
                mRectF = new RectF(mCenter.x - mRadius, mCenter.y - mRadius, mCenter.x + mRadius, mCenter.y + mRadius);
                mStrokeWidth = mRadius / 4;
                mPaint.setStrokeWidth(mStrokeWidth);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });


    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mSmileColor);
    }

    private void setAnimator(long time) {

        mAnimator = ValueAnimator.ofFloat(-0.1f, 1.1f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(time);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                if (value >= 0 && value <= 1) {
                    if (value > 0.99) {
                        value = 1;
                    }
                    mStartAngle = 720 * value;
                    mSweepAngle = MIN_SWEEP_ANGLE + value * (MAX_SWEEP_ANGLE * 2 - 360);
                    if (mSweepAngle > MAX_SWEEP_ANGLE) {
                        mSweepAngle = MAX_SWEEP_ANGLE * 2 - mSweepAngle;
                    }
                    mSmileAngle1 = 270 - SmileSpaceAngle + 360 * value;
                    mSmileAngle2 = 270 + SmileSpaceAngle + 360 * value;
                    mSmileSweepAngle = value * 30;
                    if (mSmileSweepAngle > 15) {
                        mSmileSweepAngle = 30 - mSmileSweepAngle;
                    }

                    Log.v("smileview", "value=" + value);
                    invalidate();
                }
            }
        });
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //笑脸
        drawArc(canvas, mRectF, mStartAngle, mSweepAngle);

        //笑脸眼睛
        drawArc(canvas, mRectF, mSmileAngle1, mSmileSweepAngle);
        drawArc(canvas, mRectF, mSmileAngle2, mSmileSweepAngle);


    }

    private void drawArc(Canvas canvas, RectF oval, float startAngle, float sweepAngle) {
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, startAngle, sweepAngle, false, mPaint);

        drawArcBorder(canvas, mCenter, mRadius, mStrokeWidth, startAngle);
        drawArcBorder(canvas, mCenter, mRadius, mStrokeWidth, startAngle + sweepAngle);
    }


    /**
     * 将圆弧边界圆滑
     *
     * @param canvas
     * @param center
     * @param radius
     * @param strokeWidth
     * @param angle
     */
    private void drawArcBorder(Canvas canvas, Point center, float radius, float strokeWidth, float angle) {
        float bx = (float) (center.x + radius * Math.cos(Math.PI * angle / 180));
        float by = (float) (center.y + radius * Math.sin(Math.PI * angle / 180));
        mPaint.setStyle(Paint.Style.FILL);
        RectF mf = new RectF(bx - strokeWidth / 2, by - strokeWidth / 2, bx + strokeWidth / 2, by + strokeWidth / 2);
        canvas.drawArc(mf, 0, 360, true, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getProperSize(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), widthMeasureSpec);
        int height = getProperSize(getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int getProperSize(int defaultSize, int measureSpec) {
        int properSize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                properSize = defaultSize;
                break;
            case MeasureSpec.EXACTLY:
                properSize = size;
                break;
            case MeasureSpec.AT_MOST:
                properSize = dip2px(DEFAULT_SIZE);
        }
        return properSize;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}