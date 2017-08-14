package com.umeitime.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.umeitime.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysx on 2016/7/5.
 */
public class RangeBar extends View {

    private Paint mHorizontalPaint;
    private Paint mVerticalPaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;

    /**
     * 分段个数
     */
    private int mRangeNum;
    private int mVerticalLineWidth;
    private int mVerticalLineHeight;
    private int mHorizontalLineHeight;
    private int mThumbRadius;
    private final float mPadding = 40;
    private int textColor = 0xff333333;
    /**
     * 当前选中的位置,默认为0
     */
    private int mSelectPosition = 0;
    // 一共有多少格
    private float circleX;
    private float circleY;
    // 滑动过程中x坐标
    private float currentX = 0;
    // 有效数据点
    private List<Point> points;
    private final String[] mTextNames;
    private final int[] mTextSizes;
    // 一段的宽度，根据总宽度和总格数计算得来
    private int itemWidth;
    // 控件的宽高
    private int height;
    private int width;
    private OnRangeBarListener mListener;

    public RangeBar(Context context) {
        this(context, null);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RangeBar);
        int background = ta.getColor(R.styleable.RangeBar_range_background, Color.parseColor("#8a8a8a"));
        int thumbBg = ta.getColor(R.styleable.RangeBar_thumb_color, Color.parseColor("#33475f"));
        mSelectPosition = ta.getInt(R.styleable.RangeBar_range_checked, 0);
        mThumbRadius = ta.getDimensionPixelSize(R.styleable.RangeBar_thumb_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics()));

        mHorizontalLineHeight = ta.getDimensionPixelSize(R.styleable.RangeBar_horizontal_line_height,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                        getResources().getDisplayMetrics()));

        mVerticalLineWidth = ta.getDimensionPixelSize(R.styleable.RangeBar_vertical_line_width,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                        getResources().getDisplayMetrics()));

        mVerticalLineHeight = ta.getDimensionPixelSize(R.styleable.RangeBar_vertical_line_height,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                        getResources().getDisplayMetrics()));

        mRangeNum = ta.getInt(R.styleable.RangeBar_range_num, 4);
        ta.recycle();

        mHorizontalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHorizontalPaint.setStrokeWidth(mHorizontalLineHeight);
        mHorizontalPaint.setColor(background);
        mHorizontalPaint.setStrokeCap(Paint.Cap.SQUARE);

        mVerticalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVerticalPaint.setStrokeWidth(mVerticalLineWidth);
        mVerticalPaint.setColor(background);
        mVerticalPaint.setStrokeCap(Paint.Cap.SQUARE);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(thumbBg);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);

        points = new ArrayList<>();
        mTextNames = new String[]{"小", "中", "大", "特"};
        mTextSizes = new int[]{30, 33, 36, 39};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        circleY = height / 2+mPadding;
        // 横线宽度是总宽度-2个圆的半径
        itemWidth = (w - 2 * mThumbRadius) / (mRangeNum - 1);
        // 把可点击点保存起来
        for (int i = 0; i < mRangeNum; i++) {
            points.add(new Point(mThumbRadius + i * itemWidth, height / 2));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(points.get(0).x, height / 2 + mPadding, points.get(points.size() - 1).x, height / 2 + mPadding, mHorizontalPaint);
        // 绘制刻度
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            String text = mTextNames[i];
            mTextPaint.setTextSize(mTextSizes[i]);
            canvas.drawLine(point.x, height / 2 - mVerticalLineHeight + mPadding, point.x, height / 2 + mPadding + mVerticalLineHeight, mVerticalPaint);
            canvas.drawText(text, point.x - getTextWidth(text) / 2, height / 2 - mVerticalLineHeight, mTextPaint);
        }
        // 画圆
        if (canMove) {
            // 随手指滑动过程
            if (currentX < mThumbRadius) {
                currentX = mThumbRadius;
            }
            if (currentX > width - mThumbRadius) {
                currentX = width - mThumbRadius;
            }
            circleX = currentX;
        } else {
            // 最终
            circleX = points.get(mSelectPosition).x;
        }
        canvas.drawCircle(circleX, circleY, mThumbRadius, mCirclePaint);
    }

    float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    float downX = 0;
    private boolean canMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                canMove = isDownOnCircle(downX);
                break;
            case MotionEvent.ACTION_MOVE:
                if (canMove) {
                    currentX = event.getX();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起之后就圆就不能在非有效点
                currentX = 0;
                float upX = event.getX();
                if (canMove) {
                    // 是滑动过来的，要判断距离哪个有效点最近，就滑动到哪个点
                    Point targetPoint = getNearestPoint(upX);
                    if (targetPoint != null) {
                        invalidate();
                    }
                } else {
                    if (Math.abs(downX - upX) < 30) {
                        Point point = isValidPoint(upX);
                        if (point != null) {
                            invalidate();
                        }
                    }
                }
                if (mListener != null) {
                    mListener.onClick(mSelectPosition);
                }
                downX = 0;
                canMove = false;
                break;
        }
        return true;
    }

    /**
     * 判断是否是有效的点击点
     *
     * @param x
     */
    private Point isValidPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < 30) {
                mSelectPosition = i;
                return point;
            }
        }
        return null;
    }

    /**
     * 滑动抬起之后，要滑动到最近的一个点那里
     *
     * @param x
     * @return
     */
    private Point getNearestPoint(float x) {
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (Math.abs(point.x - x) < itemWidth / 2) {
                mSelectPosition = i;
                return point;
            }
        }
        return null;
    }

    /**
     * 判断是否点击到圆上
     *
     * @param x
     * @return
     */
    private boolean isDownOnCircle(float x) {
        return Math.abs(points.get(mSelectPosition).x - x) < mThumbRadius;
    }

    public void setCurrentPosition(int selectPosition) {
        mSelectPosition = selectPosition;
    }

    public void setOnRangeBarListener(OnRangeBarListener listener) {
        mListener = listener;
    }

    public interface OnRangeBarListener {
        void onClick(int position);
    }
}