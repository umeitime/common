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
import com.umeitime.common.tools.DisplayUtils;
import com.umeitime.common.tools.MyDividerItemDecoration;
import com.umeitime.common.tools.NetWorkUtils;
import com.umeitime.common.tools.StringUtils;
import com.umeitime.common.views.ScrollableHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListFragment<P extends BasePresenter, T> extends BaseFragment implements IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener, ScrollableHelper.ScrollableContainer {
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    protected P mvpPresenter;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected int pageCount = 15;
    protected int page = 1;
    protected boolean hasNextPage;
    protected TextView tvEmpty;
    protected String key;
    protected String mKeyWord;
    protected List<T> localData;
    protected List<T> dataList = new ArrayList<>();
    protected View mEmptyView;
    protected long startTime;
    protected Handler handler = new Handler();
    protected int firstVisiblePos;

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

    protected View getHeaderView() {
        return null;
    }

    protected int getHeaderHeight() {
        return 0;
    }

    protected boolean isHasEmptyView() {
        return true;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(canRefresh());
        mvpPresenter = createPresenter();
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (hasDivider()) {
            MyDividerItemDecoration itemDecoration = new MyDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, false);
            itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider_horizontal));
            mRecyclerView.addItemDecoration(itemDecoration);
        } else {
            if (getItemDecoration() != null) {
                mRecyclerView.addItemDecoration(getItemDecoration());
            }
        }
//        mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = getAdapter();
        mAdapter.setHeaderFooterEmpty(true, true);
        if (isHasEmptyView()) {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view, mSwipeRefreshLayout, false);
            mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getHeaderHeight() == 0 ? ViewGroup.LayoutParams.MATCH_PARENT
                            : DisplayUtils.getScreenHeight(mContext) - getHeaderHeight()));
            tvEmpty = mEmptyView.findViewById(R.id.tvEmpty);
            mAdapter.setEmptyView(mEmptyView);
            mEmptyView.setVisibility(View.GONE);
        }
        if (getHeaderView() != null)
            mAdapter.addHeaderView(getHeaderView());
        mAdapter.setEnableLoadMore(false);
//        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (hasNextPage) {
                    loadData();
                }
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisiblePos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
    }
    public void smoothToTop() {
        if (firstVisiblePos > 8) {
            mRecyclerView.scrollToPosition(8);
        }
        mRecyclerView.smoothScrollToPosition(0);
    }
    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    public void onRefresh() {
        page = 1;
        startTime = System.currentTimeMillis();
        loadData();
    }

    protected abstract P createPresenter();

    protected abstract void loadData();

    public void loadData(String keyWord) {
        if(StringUtils.isNotBlank(keyWord)){
            page=1;
            mKeyWord = keyWord;
            getRefreshData();
        }
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    public void showData(List<T> datas) {
        if (datas == null) return;
        if (isDestroy) return;
        mAdapter.loadMoreComplete();
        if (canRefresh() && page == 1) {
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
        mAdapter.addData(datas);
        mAdapter.setEnableLoadMore(hasNextPage);
        if (hasNextPage) page++;
        if (datas.size() == 0 && isHasEmptyView()) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public void getRefreshData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void stopRefreshLoading() {
        if (isDestroy) return;
        mSwipeRefreshLayout.setRefreshing(false);
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
        if (mAdapter.getData().size() > 0) {
            mAdapter.loadMoreFail();
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

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }
}
