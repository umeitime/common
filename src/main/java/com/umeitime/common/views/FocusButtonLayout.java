package com.umeitime.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeitime.common.R;
import com.umeitime.common.tools.DisplayUtils;
/**
 * Created by hujunwei on 17/7/4.
 */
public class FocusButtonLayout extends RelativeLayout {
    public Context mContext;
    public Drawable backgroundDrawable;
    public Drawable leftNormalDrawable;
    public Drawable leftSelectedDrawable;
    int textColor;
    String[] follows = new String[]{"关注", "已关注"};
    TextView textView;
    MaterialProgressView contentLoadingProgressBar;
    public FocusButtonLayout(Context context) {
        this(context, null);
    }

    public FocusButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusButtonLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FocusButtonStyle);
        backgroundDrawable = a.getDrawable(R.styleable.FocusButtonStyle_fbl_button_background);
        leftNormalDrawable = a.getDrawable(R.styleable.FocusButtonStyle_fbl_button_drawable_left_normal);
        leftSelectedDrawable = a.getDrawable(R.styleable.FocusButtonStyle_fbl_button_drawable_left_selected);
        textColor = a.getColor(R.styleable.FocusButtonStyle_fbl_button_text_selector, 0);
        a.recycle();

        initTextView(attrs);
    }

    private void initTextView(AttributeSet attrs) {
        textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(backgroundDrawable);
        textView.setCompoundDrawablePadding(DisplayUtils.dip2px(mContext,3));
        textView.setTextSize(12);
        addView(textView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        contentLoadingProgressBar = new MaterialProgressView(mContext);
        contentLoadingProgressBar.setPadding(10,10,10,10);
        int width = DisplayUtils.dip2px(mContext,30);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,width);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(contentLoadingProgressBar,layoutParams);
        textView.setVisibility(INVISIBLE);
        showFollowStatus(0);
    }

    private void showFollowStatus(int followStatus) {
        textView.setSelected(followStatus==1);
        contentLoadingProgressBar.setVisibility(GONE);
        int dp8 = DisplayUtils.dip2px(mContext, 8);
        if (followStatus==0) {
            textView.setPadding(dp8/2*3, dp8, dp8/2*3, dp8);
            textView.setText(follows[0]);
            textView.setTextColor(textColor);
            leftNormalDrawable.setBounds(0, 0, leftNormalDrawable.getMinimumWidth(), leftNormalDrawable.getMinimumHeight());
            //必须设置图片大小，否则不显示
            textView.setCompoundDrawables(leftNormalDrawable, null, null, null);
        } else {
            textView.setPadding(dp8, dp8, dp8, dp8);
            textView.setText(follows[1]);
            textView.setTextColor(Color.WHITE);
            leftSelectedDrawable.setBounds(0, 0, leftSelectedDrawable.getMinimumWidth(), leftSelectedDrawable.getMinimumHeight());
            textView.setCompoundDrawables(leftSelectedDrawable, null, null, null);
        }
    }

    private void showLoadingView(){
        contentLoadingProgressBar.setVisibility(VISIBLE);
        textView.setVisibility(GONE);
    }

}
