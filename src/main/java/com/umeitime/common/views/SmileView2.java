package com.umeitime.common.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import com.umeitime.common.R;

/**
 * Created by hujunwei on 2018/1/3.
 */

public class SmileView2 extends View {
    Paint paint,eyePaint;
    DrawFilter drawFilter;
    Path path, pathCircle,pathCircle2;
    PathMeasure pm,pm2;
    float length;// 圆周长
    float fraction = -1;
    long duration = 2000;
    int repeaCount = 1000;
    ValueAnimator val;
    //字段
    private int blue = 0xff4aadff;
    private int mWidth = 200;
    private int mHeight = 200;
    private int radius = 20;
    private int lineWidth = 5;
    private float eyeRadius;
    public SmileView2(Context context) {
        super(context);
        init(context,null);
    }
    public SmileView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        blue = context.getResources().getColor(R.color.colorPrimary);
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        lineWidth = dip2px(context, lineWidth);
        radius = dip2px(context, radius);
        path = new Path();
        pathCircle = new Path();
        pathCircle2 = new Path();
        //在path中添加一个顺时针的圆，这时候路径的起点和终点在最后边
        //在画前半部分的脸和运动中的脸，起点在最右边比较方便的计算，但在最后那部分，运动的终点
        //是在圆形的底部，这样把路径圆进行转换到底部，方便计算
        pathCircle.addCircle(0, 0, radius, Path.Direction.CW);
        pathCircle2.addCircle(0, 0, radius, Path.Direction.CW);
        //利用Matrix，让pathCircle中的圆旋转90度，这样它的路径的起点和终点都在底部了
        Matrix m = new Matrix();
        m.postRotate(90);
        pathCircle.transform(m);
        //画脸的笔
        paint = new Paint();
        //画眼睛的笔
        eyePaint = new Paint();
        paint.setColor(blue);
        eyePaint.setColor(blue);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        eyePaint.setStrokeWidth(lineWidth);
        //设置画脸的笔的端点为圆角(即起点和终点都是圆角)
        paint.setStrokeCap(Paint.Cap.ROUND);
        //使用PathMeasure计算路径的信息
        pm = new PathMeasure();
        pm.setPath(pathCircle, false);
        pm2 = new PathMeasure();
        pm2.setPath(pathCircle2, false);
        //路径的长度，两个路径都是圆形，因此只计算其中一个即可
        length = pm.getLength();
        eyeRadius = (float)(lineWidth/2.0+lineWidth/5.0);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            if (changed) {
                mWidth = right - left;
                mHeight = bottom - top;
            }
        }
    }
    /* @param canvas
         * @param pos 0代表画左眼，1代表画右眼
         */
    public void drawOneEye(Canvas canvas, int pos) {
        float x = (float) ((radius) * Math.cos(Math.PI * 45 / 180));
        float y = x;
        if (pos == 0) {
            canvas.drawCircle(-x, -y, eyeRadius, eyePaint);
        }else if(pos==1){
            canvas.drawCircle(x, -y, eyeRadius , eyePaint);
        }
    }
    /**静态的笑脸
     * @param canvas
     */
    private void first(Canvas canvas) {
        pm2.getSegment(10, length / 2-10, path, true);
        canvas.drawPath(path, paint);
        path = new Path();
        drawEye(canvas);
    }
    /**一开始画的眼睛
     * @param canvas
     */
    public void drawEye(Canvas canvas) {
        float x = (float) ((radius) * Math.cos(Math.PI * 45 / 180));
        float y = x;
        canvas.drawCircle(-x, -y, eyeRadius , eyePaint);
        canvas.drawCircle(x, -y, eyeRadius , eyePaint);
    }
    /**从底部开始画一个在运动的圆，运动时间为0-3/4
     * 即从270度开始，逆时针到0度
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        float degree = 270 - 270 * 4 / 3 * fraction;
        float x = (float) ((radius ) * Math.cos(Math.PI * degree/180));
        float y = -(float) ((radius ) * Math.sin(Math.PI * degree/ 180));
        canvas.drawCircle(x, y, eyeRadius, eyePaint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //从画布上去除锯齿
        canvas.setDrawFilter(drawFilter);
        canvas.translate(mWidth / 2, mHeight / 2);
        if (fraction == -1||!val.isRunning())
            first(canvas);
        //从底部开始画一个在运动的圆，运动时间为0-3/4
        if (0 < fraction && fraction < 0.75) {
            drawCircle(canvas);
        }
        //画左眼
        if (fraction > 1.0 * 3 / 8&&fraction<1.0*6/4) {
            drawOneEye(canvas,0);
        }
        //画右眼
        if(fraction>1.0*5/8&&fraction<1.0*6/4){
            drawOneEye(canvas, 1);
        }
        //画脸
        if(fraction>=0.75&&fraction<=5.0/4){
            drawFace(canvas);
        }
        //把脸运动起来
        if(fraction>=5.0/4&&fraction<=(5.0/4+1.0/4)){
            rotateFace(canvas);
        }
        if(fraction>=6.0/4){
            drawLastFact(canvas);
        }
    }
    public void drawFace(Canvas canvas){
        //需要重新给path赋值
        path=null;
        path = new Path();
        //根据时间来截取一定长度的路径，保存到path中，取值范围(0，length/2)
        pm2.getSegment(0, (float) (length*(fraction-0.75)), path, true);
        canvas.drawPath(path, paint);
    }
    public void rotateFace(Canvas canvas){
        path=null;
        path = new Path();
        pm2.getSegment((float)(length*(fraction-5.0/4)), (float)(length*(fraction-5.0/4)+length*0.5), path, true);
        canvas.drawPath(path, paint);
    }
    public void drawLastFact(Canvas canvas){
        path = null;
        path = new Path();
        //从起点的1/4长度开始（即最左边的圆点)，到圆的路径的终点（即底部）
        pm.getSegment((float)(1.0/4*length+3.0/2*(fraction-3.0/2)*length), (float)(length/2.0+length/8.0+(fraction-3.0/2)*length), path, true);
        canvas.drawPath(path, paint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpectMode == MeasureSpec.AT_MOST
                && heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpectSize);
        } else if (heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpectSize, mHeight);
        }
    }
    public void performAnim() {
        //上面计算的时间比例，加起来就是2，是运动了两周，因此这里设置为(0,2)
        val = ValueAnimator.ofFloat(0, 2);
        val.setDuration(duration);
        val.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                fraction = (float) arg0.getAnimatedValue();
                postInvalidate();
            }
        });
        val.setRepeatCount(repeaCount);
        val.start();
        val.setRepeatMode(ValueAnimator.RESTART);
    }
    public void cancelAnim(){
        if(val!=null) {
            val.cancel();
        }
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
