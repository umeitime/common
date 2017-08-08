package com.umeitime.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.umeitime.common.tools.DisplayUtils;

public class DynamicHeightListView extends ListView {
  
  
    private int maxHeight = 0;
      
    public DynamicHeightListView(Context context) {
        super(context);  
    }  
  
  
    public DynamicHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
  
    public DynamicHeightListView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);
        maxHeight = DisplayUtils.dip2px(context,400);
    }  
  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        Log.i("ooo", "onMeasure.....");
    }  
  
  
    @Override  
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {  
        super.onLayout(changed, left, top, right, bottom);  
        Log.i("ooo", "onLayout.....");  
        setViewHeightBasedOnChildren();  
    }  
  
  
    public void setViewHeightBasedOnChildren() {  
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter == null) {  
            return;  
        }  
        // int h = 10;  
        // int itemHeight = BdUtilHelper.getDimens(getContext(), R.dimen.ds30);  
        int sumHeight = 0;  
        int size = listAdapter.getCount();  
  
  
        for (int i = 0; i < size; i++) {  
            View v = listAdapter.getView(i, null, this);
            v.measure(0, 0);  
            Log.i("melon", "item.heiht = " + v.getMeasuredHeight());  
            sumHeight += v.getMeasuredHeight();  
  
  
        }  
  
  
        if (sumHeight > maxHeight) {  
            sumHeight = maxHeight;  
        }  
        android.view.ViewGroup.LayoutParams params = this.getLayoutParams();  
        // this.getLayoutParams();  
        params.height = sumHeight;  
  
  
        this.setLayoutParams(params);  
    }  
  
  
    public int getMaxHeight() {  
        return maxHeight;  
    }  
  
  
    public void setMaxHeight(int maxHeight) {  
        this.maxHeight = maxHeight;  
    }  
  
  
}  