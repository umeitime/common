package com.umeitime.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutEx extends LinearLayout {

    private OnSoftKeyboardListener onSoftKeyboardListener;

    public LinearLayoutEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutEx(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        if (onSoftKeyboardListener != null) {
            final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
            final int oldSpec = getMeasuredHeight();
            if (oldSpec> newSpec){
                onSoftKeyboardListener.onShown();
            } else if(oldSpec < newSpec){
                onSoftKeyboardListener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setOnSoftKeyboardListener(final OnSoftKeyboardListener listener) {
        this.onSoftKeyboardListener = listener;
    }

    public interface OnSoftKeyboardListener {
        void onShown();
        void onHidden();
    }
}