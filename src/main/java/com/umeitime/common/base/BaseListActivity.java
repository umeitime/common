package com.umeitime.common.base;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeitime.common.R;
import com.umeitime.common.interfaces.OnScrollYListener;
import com.umeitime.common.tools.DisplayUtils;
import com.umeitime.common.tools.NetWorkUtils;
import com.umeitime.common.tools.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListActivity<P extends BasePresenter, T> extends BaseActivity implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {
    public RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected P mvpPresenter;
    protected int pageCount = 15;
    protected int page = 1;
    protected boolean hasPrePage;
    protected boolean hasNextPage;
    protected View mEmptyView;
    protected TextView tvEmpty;
    protected String key;
    protected List<T> localData;
    protected List<T> dataList = new ArrayList<>();
    protected long startTime;
    protected Handler handler = new Handler();
    protected OnScrollYListener scrollYListener;

    public void setOnScrollYListener(OnScrollYListener listener) {
        scrollYListener = listener;
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_baselist;
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

    protected boolean hasEmptyView() {
        return true;
    }

    protected View getHeaderView() {
        return null;
    }

    protected int getHeaderHeight() {
        return 0;
    }

    @Override
    protected void initEvent() {

    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }
    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mvpPresenter = createPresenter();
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(canRefresh());
        mRecyclerView.setLayoutManager(getLayoutManager());
        if (hasDivider()) {
            mRecyclerView.addItemDecoration(new RecyclerViewDivider(this,
                    LinearLayoutManager.VERTICAL,
                    DisplayUtils.dip2px(mContext, 0.6f),
                    ContextCompat.getColor(mContext, R.color.line_color)));
        } else {
            if (getItemDecoration() != null) {
                mRecyclerView.addItemDecoration(getItemDecoration());
            }
        }
//        mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter = getAdapter();
        mAdapter.setHeaderFooterEmpty(true, true);
        if (hasEmptyView()) {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view, mSwipeRefreshLayout, false);
            mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeaderHeight() == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : DisplayUtils.getScreenHeight(mContext) - getHeaderHeight()));
            tvEmpty = mEmptyView.findViewById(R.id.tvEmpty);
            mAdapter.setEmptyView(mEmptyView);
            mEmptyView.setVisibility(View.GONE);
        }
        if (getHeaderView() != null)
            mAdapter.addHeaderView(getHeaderView());
        mAdapter.setEnableLoadMore(false);
//        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (scrollYListener != null && firstVisibleItem == 0) {
                    scrollYListener.OnScrollY(getScollYDistance());
//                    firstPosListener.OnFirstPosListener(firstVisibleItem);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    protected abstract P createPresenter();

    protected abstract void loadData();

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        return linearLayoutManager;
    }

    public void setSelection(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void onRefresh() {
        page = 1;
        startTime = System.currentTimeMillis();
        loadData();
    }

    public void stopRefreshLoading() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 800 - (System.currentTimeMillis() - startTime));
    }

    public void getRefreshData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void showData(List<T> datas) {
        if (datas == null) return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                mAdapter.loadMoreComplete();
                if (page == 1 && canRefresh()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mAdapter.getData().size() > 0) {
                        mAdapter.getData().clear();
                    }
                }
                if (hasLoadMore()) {
                    hasNextPage = datas.size() >= pageCount;
                } else {
                    hasNextPage = false;
                }
                if (page == 1 && hasNextPage) {
                    mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                        @Override
                        public void onLoadMoreRequested() {
                            if (hasNextPage) {
                                loadData();
                            }
                        }
                    }, mRecyclerView);
                }
                mAdapter.addData(datas);
                mAdapter.setEnableLoadMore(hasNextPage);
                if (hasNextPage) page++;
                if (datas.size() == 0 && hasEmptyView()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                }

            }
        }, mSwipeRefreshLayout.isRefreshing() ? 800 - (System.currentTimeMillis() - startTime) : 0);
    }

    public void getDataFail(String msg) {
        if (isDestroy) return;
        if (mSwipeRefreshLayout.isRefreshing()) {
            stopRefreshLoading();
        }
        if (NetWorkUtils.checkNetwork(mContext)) {
            showMsg(msg);
        } else {
            showMsg(getString(R.string.nonet));
        }
        mAdapter.loadMoreComplete();
        if (mAdapter.getData().size() > 0) {
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
