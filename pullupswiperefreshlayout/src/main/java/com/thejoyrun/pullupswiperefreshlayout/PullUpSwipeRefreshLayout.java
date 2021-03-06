package com.thejoyrun.pullupswiperefreshlayout;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 * Created by Wiki on 16/7/27.
 */
public class PullUpSwipeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    private boolean mLoadAutoEnabled = true;
    private boolean mLoadEnabled = true;
    private boolean mRefreshEnabled = true;
    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;

    private FooterView mFooterView;
    private TextView pull_to_refresh_load_more_text;
    private ProgressBar pull_to_refresh_load_progress;
    private EmptyView mEmptyView;
    private ListViewInter mListView;

    private long mCreateTimeMillis = System.currentTimeMillis();

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

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
        initView(context);
    }

    private void initView(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        this.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_bright);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setListView(ListViewInter listView) {
        this.mListView = listView;
        initListView();
    }

    public void setListViewAndEmptyView(ListViewInter listView, EmptyView emptyView) {
        setListView(listView);
        this.mEmptyView = emptyView;
        listView.setListEmptyView(emptyView);
    }

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
                    autoLoad();
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
        return mListView != null && mListView.isAdapterExist() && mListView.getListLastVisiblePosition() >= (mListView.getListItemCount() - 1);
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
    private void autoLoad() {
        if (isLoadAutoEnabled() && isLoadEnabled()) {
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

    public void initListView() {
        if (mFooterView == null) {
            mFooterView = new FooterView(getContext());

            pull_to_refresh_load_more_text = mFooterView.getTextView();
            pull_to_refresh_load_progress = mFooterView.getProgressBar();
            pull_to_refresh_load_progress.setVisibility(GONE);

            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLoadEnabled()) {
                        setLoading(true);
                    }
                }
            });

        }
        mListView.addFooterItem(mFooterView);
        mFooterView.setVisibility(INVISIBLE);
    }

    private void updateFooter() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (PullUpSwipeRefreshLayout.this) {
                    if (mListView != null) {
                        if (isLoadEnabled()) {
                            pull_to_refresh_load_more_text.setVisibility(View.VISIBLE);
                        }
                        if (isLoading()) {
                            pull_to_refresh_load_progress.setVisibility(View.VISIBLE);
                            pull_to_refresh_load_more_text.setText("正在加载...");
                        } else {
                            pull_to_refresh_load_progress.setVisibility(View.GONE);
                            pull_to_refresh_load_more_text.setText("加载更多");
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
        if (mListView == null || mListView.getListChildAt(0) == null)
            return 0;
        View topChild = mListView.getListChildAt(0);
        return topChild.getTop();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            autoLoad();
        }

        if (scrollUpDownListener == null)
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

    public void setListViewUpDownListener(ScrollUpDownListener scrollUpDownListener) {
        this.scrollUpDownListener = scrollUpDownListener;
    }

    public void removeListViewUpDownListener() {
        if (this.scrollUpDownListener != null) {
            this.scrollUpDownListener = null;
        }
    }

    /**
     * 加载更多的监听器
     */
    public interface OnLoadListener {
        void onLoad();
    }

    public interface ScrollUpDownListener {
        void onUp();

        void onDown();
    }

    //重写父类的setRefreshing，setOnRefreshListener，因为android提供的setRefreshing(true),无法调用mListener.onRefresh()，
    //同时如果在Activity没有初始化完毕的时候调用setRefreshing不会有任何反应
    private Handler mHandler = new Handler();

    private boolean isRealRefreshing = false;


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured) {
            mMeasured = true;
            if (mPreMeasureRefreshing) {
                super.setRefreshing(true);
            }
        }
    }


    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        isRealRefreshing = refreshing;
        if (!isRealRefreshing && null != mListView && mListView.isAdapterExist() && mEmptyView != null) {
            int listCount = mListView.getListItemCount() - mListView.getListHeaderViewsCount() - mListView.getListFooterViewsCount();
            if (listCount == 0) {
                mEmptyView.setRefreshing(isRealRefreshing);
            }else {
                mListView.removeEmptyView();
            }
        }else if(!isRealRefreshing && (null == mListView || !mListView.isAdapterExist()) && mEmptyView != null){
            mEmptyView.setRefreshing(isRealRefreshing);
        }



        if (!mMeasured) {
            mPreMeasureRefreshing = refreshing;
        }else {
            super.setRefreshing(refreshing);
        }
        if (!refreshing) {
            mPreMeasureRefreshing = false;
        }

    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }

//    public void setFooterPadding(boolean show) {
//        findViewById(R.id.footer_padding).setVisibility(show ? VISIBLE : GONE);
//    }

    public boolean isLoadAutoEnabled() {
        return mLoadAutoEnabled;
    }

    public void setLoadAutoEnabled(boolean loadAutoEnabled) {
        this.mLoadAutoEnabled = loadAutoEnabled;
    }

    public boolean isLoadEnabled() {
        return mLoadEnabled;
    }

    public void setLoadEnabled(boolean mLoadEnabled) {
        this.mLoadEnabled = mLoadEnabled;
//        updateFooter();
    }

    public boolean isRefreshEnabled() {
        return mRefreshEnabled;
    }

    public void setRefreshEnabled(boolean mRefreshEnabled) {
        this.mRefreshEnabled = mRefreshEnabled;
    }

    public void setFooterViewShow(boolean toShow){
        if(null != mFooterView){
            if(toShow){
                mFooterView.setVisibility(VISIBLE);
//                mFooterView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            }else {
                mFooterView.setVisibility(GONE);
//                mFooterView.getLayoutParams().height = 0;
            }
        }
    }

    public FooterView getFooterView() {
        return mFooterView;
    }
}