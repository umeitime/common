package com.umeitime.common.helper;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    /**
     * 存储listview所有的实体类
     */
    public int type = 0;
    public ArrayList dataList = new ArrayList();
    public List<Integer> viewTypeAllList = new ArrayList<Integer>();

    public void initData(List list) {
        dataList.addAll(list);
    }
    public void clearData() {
        dataList.clear();
        viewTypeAllList.clear();
    }
}
