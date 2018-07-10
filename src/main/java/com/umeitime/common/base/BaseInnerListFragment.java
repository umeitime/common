package com.umeitime.common.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culiu.mhvp.core.InnerListView;
import com.culiu.mhvp.core.InnerScroller;
import com.culiu.mhvp.core.InnerScrollerContainer;
import com.culiu.mhvp.core.OuterScroller;
import com.umeitime.common.R;
import com.umeitime.common.interfaces.OnScrollYListener;
import com.umeitime.common.tools.DisplayUtils;
import com.umeitime.common.tools.StringUtils;
import com.zhy.adapter.abslistview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseInnerListFragment<P extends BasePresenter, T> extends BaseFragment implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener, InnerScrollerContainer {
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public InnerListView mListView;
    public OuterScroller mOuterScroller;
    protected P mvpPresenter;
    protected MultiItemTypeAdapter mAdapter;
    protected int pageCount = 15;
    protected int page = 1;
    protected boolean hasNextPage;
    protected OnScrollYListener scrollYListener;
    protected int mIndex;
    protected TextView tvEmpty;
    protected String key;
    protected String mKeyWord;
    protected List<T> localData;
    protected List<T> dataList = new ArrayList<>();
    protected RelativeLayout mEmptyView;
    private boolean isFirstLoad = true;
    private Handler handler = new Handler();
    private View footerView;
    private LinearLayout loadingView;
    private FrameLayout failView;
    private int firstVisiblePos;

    @Override
    public void setOuterScroller(OuterScroller outerScroller, int myPosition) {
        if (outerScroller == mOuterScroller && myPosition == mIndex) {
            return;
        }
        mOuterScroller = outerScroller;
        mIndex = myPosition;

        if (getInnerScroller() != null) {
            getInnerScroller().register2Outer(mOuterScroller, mIndex);
        }
    }

    @Override
    public InnerScroller getInnerScroller() {
        return mListView;
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_base_inner_listview;
    }

    protected boolean hasLoadMore() {
        return true;
    }

    protected boolean hasDivider() {
        return false;
    }

    protected boolean canRefresh() {
        return true;
    }

    protected int getHeaderHeight() {
        return 0;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setEnabled(canRefresh());
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mListView = mRootView.findViewById(R.id.listView);
        mEmptyView = mRootView.findViewById(R.id.empty_view);
        mEmptyView.setVisibility(View.GONE);
        tvEmpty = mRootView.findViewById(R.id.tvEmpty);
        mvpPresenter = createPresenter();
        mAdapter = getAdapter();
//        mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view, null);
//        tvEmpty = mEmptyView.findViewById(R.id.tvEmpty);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, DisplayUtils.dip2px(mContext, 100), 0, 0);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        tvEmpty.setLayoutParams(params);
//        mListView.setEmptyView(mEmptyView);
        mListView.setFooterDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        if (hasDivider()) {
            mListView.setSelector(new ColorDrawable(ContextCompat.getColor(mContext, R.color.line_color)));
            mListView.setDividerHeight(DisplayUtils.dip2px(mContext, 0.4f));
        } else {
            mListView.setDividerHeight(0);
        }
        footerView = LayoutInflater.from(mContext).inflate(R.layout.quick_view_load_more, null, false);
        loadingView = footerView.findViewById(R.id.load_more_loading_view);
        loadingView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(mContext, 40)));
        failView = footerView.findViewById(R.id.load_more_load_fail_view);
        mListView.addFooterView(footerView);
//        mListView.setAdapter(mAdapter);
        mListView.register2Outer(mOuterScroller, mIndex);
        failView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadMoreState(1);
                loadData();
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        // 判断是否滚动到底部
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            //加载更多功能的代码
                            if (hasNextPage) {
                                setLoadMoreState(1);
                                loadData();
                            }
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisiblePos = firstVisibleItem;
                try {
                    int headerVisibleHeight = mOuterScroller.getCurrentInnerScroller().getOuterScroller().getHeaderVisibleHeight();
                    Log.e("headerVisibleHeight", headerVisibleHeight + "");
                    if (scrollYListener != null) {
                        scrollYListener.OnScrollY(headerVisibleHeight);
                    }
                } catch (Exception e) {

                }

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public void smoothToTop() {
        if (firstVisiblePos > 8) {
            mListView.setSelection(8);
        }
        mListView.smoothScrollToPosition(0);
    }

    public void setLoadMoreState(int status) {
        if (status == 3) {
            if (mListView.getFooterViewsCount() > 0 && footerView != null) {
                mListView.removeFooterView(footerView);
            }
        } else {
            loadingView.setVisibility(status == 1 ? View.VISIBLE : View.GONE);
            failView.setVisibility(status == 2 ? View.VISIBLE : View.GONE);
        }
//        endView.setVisibility(status==3?View.VISIBLE:View.GONE);
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    protected abstract P createPresenter();

    protected abstract void loadData();

    public void loadData(String keyWord) {
        if (StringUtils.isNotBlank(keyWord)) {
            page = 1;
            mKeyWord = keyWord;
            getRefreshData();
        }
    }

    protected abstract MultiItemTypeAdapter getAdapter();

    public void showData(List<T> datas) {
        if (datas == null || isDestroy) return;
        mSwipeRefreshLayout.setRefreshing(false);
        if (canRefresh() && page == 1) {
            if (dataList.size() > 0) {
                dataList.clear();
                if (!isFirstLoad) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        if (hasLoadMore()) {
            hasNextPage = datas.size() >= pageCount;
        } else {
            hasNextPage = false;
        }
        for (T t : datas) {
            if (!dataList.contains(t)) {
                dataList.add(t);
            }
        }
        if (isFirstLoad && dataList.size() > 0) {
            isFirstLoad = false;
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (hasNextPage) {
            page++;
        } else {
            setLoadMoreState(3);
        }
        if (datas.size() == 0 || dataList.size() == 0) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public void getRefreshData() {
        onRefresh();
    }

    public void getDataFail(String msg) {
        if (isDestroy) return;
        if (dataList.size() > 0) {
            setLoadMoreState(2);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void showLoading(String charSequence) {
        showProgressDialog(charSequence);
    }

    public void hideLoading() {
        dismissProgressDialog();
    }

    public void setOnScrollYListener(OnScrollYListener listener) {
        scrollYListener = listener;
    }

}
