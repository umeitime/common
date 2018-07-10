package com.umeitime.common.base;

import java.util.List;

public interface IBaseListView<T> extends BaseView{
    void showData(List<T> dataList);
}
