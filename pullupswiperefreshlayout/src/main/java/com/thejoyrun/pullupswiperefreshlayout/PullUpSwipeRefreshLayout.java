package com.thejoyrun.pullupswiperefreshlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 */
public class PullUpSwipeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    private LoadingMode mLoadingMode = LoadingMode.none;
    private EmptyMode   mEmptyMode;
    private TextView    mButtonRefresh;
    private TextView    mTextViewRefresh;
    private ProgressBar mProgressBarRefresh;
    private ImageView   mImageViewRefresh;
    private View        mFooterView;

    public void setLoadingMode(LoadingMode mode) {
        this.mLoadingMode = mode;
        updateFooter();
    }

    public static enum LoadingMode {
        none, auto, click
    }

    /**
     * 滑动到最下面时的上拉操作
     */

    private int      mTouchSlop;
    /**
     * listView实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewLoadingFooter;
    private View mListViewClickFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean mLoading = false;

    public boolean isLoading() {
        return mLoading;
    }

    public PullUpSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public PullUpSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        this.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
        if (mListView == null) {
            getListView();
        }
        updateFooter();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
        if (mEmptyView != null) {
            final int width = getMeasuredWidth();
            final int height = getMeasuredHeight();
            mEmptyView.layout(0, 0, width, height);
        }
    }

    /**
     * 获取ListView对象
     */

    private ListView getListView() {
        if (mListView != null) {
            return mListView;
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
            }
        }
        return mListView;
    }

//    protected void ensureTarget() {
//        if (mTarget == null) {
//            for (int i = 0; i < getChildCount(); i++) {
//                View child = getChildAt(i);
//                if (!child.equals(mEmptyView) && !child.equals(mCircleView)) {
//                    mTarget = child;
//                    break;
//                }
//            }
//        }
//    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listView不在加载中, 且为上拉操作.
     */
    private boolean canLoad() {
        return isBottom() && !mLoading && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        return mListView != null && mListView.getAdapter() != null && mListView.getLastVisiblePosition() >= (mListView.getAdapter().getCount() - 1);
    }

    /**
     * 是否是上拉操作
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mLoadingMode == LoadingMode.auto) {
            setLoading(true);
        }
    }

    public void setLoading(boolean b) {

        if (b != mLoading && !isRefreshing()) {
            this.mLoading = b;
            if (this.mLoading) {
                mOnLoadListener.onLoad();
            }
            updateFooter();
        }

    }

    public void init() {
        if (getListView() != null) {
            if (mFooterView == null) {
                mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_loading_footer, null, false);
                mListViewLoadingFooter = mFooterView.findViewById(R.id.view_refresh_loading_footer);
                mListViewClickFooter = mFooterView.findViewById(R.id.view_refresh_click_footer);

                mListView.addFooterView(mFooterView, null, false);
                mListViewClickFooter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (LoadingMode.click == mLoadingMode || LoadingMode.auto == mLoadingMode) {
                            setLoading(true);
                        }
                    }
                });
            }
            mListViewLoadingFooter.setVisibility(View.GONE);
            mListViewClickFooter.setVisibility(View.GONE);
        }
    }

    private void updateFooter() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (PullUpSwipeRefreshLayout.this) {
                    init();
                    if (getListView() != null) {
                        if (isLoading()) {
                            mListViewLoadingFooter.setVisibility(View.VISIBLE);
                        } else {
                            if (mLoadingMode != LoadingMode.none) {
                                mListViewClickFooter.setVisibility(View.VISIBLE);
                            }
                            mYDown = 0;
                            mLastY = 0;
                        }
                    }
                }
            }
        });
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private int mLastScrollY;
    private int mPreviousFirstVisibleItem = 0;
    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null)
            return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }

        if (scrollUpDownListener==null)
            return;
        if (totalItemCount == 0)
            return;
        if (firstVisibleItem == mPreviousFirstVisibleItem) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > 4;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    // 向下滑动
                    scrollUpDownListener.onDown();
                } else {
                    // 向上滑动
                    scrollUpDownListener.onUp();
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                // 向下滑动
                scrollUpDownListener.onDown();
            } else {
                // 向上滑动
                scrollUpDownListener.onUp();
            }
            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }
    private ScrollUpDownListener scrollUpDownListener;
    public void setListViewUpDownListener(ScrollUpDownListener scrollUpDownListener){
        this.scrollUpDownListener=scrollUpDownListener;
    }
    public void removeListViewUpDownListener(){
        if (this.scrollUpDownListener!=null){
            this.scrollUpDownListener=null;
        }
    }
    /**
     * 加载更多的监听器
     */
    public static interface OnLoadListener {
        public void onLoad();
    }

    public abstract interface ScrollUpDownListener{
        public void onUp();
        public void onDown();
    }

    //重写父类的setRefreshing，setOnRefreshListener，因为android提供的setRefreshing(true),无法调用mListener.onRefresh()，
    //同时如果在Activity没有初始化完毕的时候调用setRefreshing不会有任何反应
    protected OnRefreshListener mListener;
    private Handler mHandler = new Handler();

    public void setRefreshing(final boolean refreshing) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing() != refreshing) {
                    if (refreshing && mListener != null) {
                        mListener.onRefresh();
                    }
                }
                PullUpSwipeRefreshLayout.super.setRefreshing(refreshing);
            }
        }, refreshing ? 200 : 0);

    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
        super.setOnRefreshListener(listener);
    }

    private void updateState() {
        Log.d("mark", "网络状态已经改变");
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.d("mark", "当前网络名称：" + name);
            if (mEmptyMode == EmptyMode.network_error) {
                updateEmpty(EmptyMode.failed);
            }
        } else {
            Log.d("mark", "没有可用网络");
            if (mEmptyMode == EmptyMode.failed || mEmptyMode == EmptyMode.loading) {
                updateEmpty(EmptyMode.network_error);
            }
        }
    }

    protected View mEmptyView;

    public void updateEmpty(EmptyMode emptyMode) {

        if (mEmptyView == null) {
            mReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                        updateState();
                    }
                }
            };
            IntentFilter mFilter = new IntentFilter();
            mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getContext().registerReceiver(mReceiver, mFilter);

            mEmptyView = LayoutInflater.from(this.getContext()).inflate(R.layout.view_refresh_empty, null);
            mButtonRefresh = (TextView) mEmptyView.findViewById(R.id.button);
            mTextViewRefresh = (TextView) mEmptyView.findViewById(R.id.textView);
            mProgressBarRefresh = (ProgressBar) mEmptyView.findViewById(R.id.progressBar);
            mImageViewRefresh = (ImageView) mEmptyView.findViewById(R.id.imageView);

            mButtonRefresh.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEmptyMode == EmptyMode.network_error) {
                        Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        updateEmpty(null);
                    } else {
                        updateEmpty(EmptyMode.loading);

                    }
                }
            });
            mEmptyView.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(mEmptyView, params);
        }

        if (this.mEmptyMode == emptyMode) {
            return;
        }
        if (emptyMode != null) {
            this.mEmptyMode = emptyMode;
        }

        mEmptyView.setVisibility(View.VISIBLE);
        getListView();
        this.setEnabled(false);
        if (mListView != null) {
            mListView.setVisibility(View.GONE);
        }
        if (mEmptyMode == EmptyMode.network_error) {
            mButtonRefresh.setVisibility(View.VISIBLE);
            mButtonRefresh.setText("设置");

            mTextViewRefresh.setText("无网络，请检查网络设置");
            mProgressBarRefresh.setVisibility(View.INVISIBLE);
            mImageViewRefresh.setVisibility(View.VISIBLE);
        } else if (mEmptyMode == EmptyMode.failed) {
            mButtonRefresh.setVisibility(View.VISIBLE);
            mButtonRefresh.setText("刷新");

            mTextViewRefresh.setText("请点击刷新");
            mProgressBarRefresh.setVisibility(View.INVISIBLE);
            mImageViewRefresh.setVisibility(View.VISIBLE);
            if (mListener != null) {
                mListener.onRefresh();
            }
        } else if (mEmptyMode == EmptyMode.nothing) {
            mButtonRefresh.setVisibility(View.VISIBLE);
            mButtonRefresh.setText("刷新");

            mTextViewRefresh.setText("没有获取到内容");
            mProgressBarRefresh.setVisibility(View.INVISIBLE);
            mImageViewRefresh.setVisibility(View.VISIBLE);
        } else if (mEmptyMode == EmptyMode.loading) {
            mButtonRefresh.setVisibility(View.INVISIBLE);

            mTextViewRefresh.setText("正在加载中");
            mProgressBarRefresh.setVisibility(View.VISIBLE);
            mImageViewRefresh.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            if (mListView != null) {
                mListView.setVisibility(View.VISIBLE);
            }
            this.setEnabled(true);
        }
        mEmptyView.invalidate();
        PullUpSwipeRefreshLayout.this.invalidate();
        updateState();
    }

    private BroadcastReceiver mReceiver;

    protected void onDestroy() {
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }

    public enum EmptyMode {
        none, nothing, network_error, failed, loading
    }

    public void setFooterPadding(boolean show) {
        findViewById(R.id.footer_padding).setVisibility(show ? VISIBLE : GONE);
    }

}