package com.umeitime.common.base;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public abstract class UmBaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context context;
    protected List<T> mList;

    public UmBaseAdapter(Context context, List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
