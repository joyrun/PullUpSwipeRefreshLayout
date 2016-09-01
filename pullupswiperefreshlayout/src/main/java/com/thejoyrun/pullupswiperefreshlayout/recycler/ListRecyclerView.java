package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.thejoyrun.pullupswiperefreshlayout.EmptyView;
import com.thejoyrun.pullupswiperefreshlayout.ListViewInter;

/**
 * Created by keven on 16/8/24.
 */

public class ListRecyclerView<T extends ListRecyclerViewAdapter>extends RecyclerView implements ListViewInter{

    private ListRecyclerViewAdapter mListRecyclerViewAdapter;
    private View mEmptyView;
    private View mFooterView;
    private View mHeaderView;

    public ListRecyclerView(Context context) {
        this(context, null, 0);
    }

    public ListRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 可以通过setRecyclerAdapter api设置Adapter
     * @param adapter
     * @hide
     */
    @Override
    final public void setAdapter(Adapter adapter) {
        if(adapter instanceof ListRecyclerViewAdapter) {
            super.setAdapter(adapter);
        }else {
            new Throwable("setAdapter need to put in class extend ListRecyclerViewAdapter");
        }
    }

    /**
     * 设置 adapter 其用法跟setAdapter一样
     * @param adapter
     */
    public void setRecyclerAdapter(ListRecyclerViewAdapter adapter){
        mListRecyclerViewAdapter = adapter;
        setAdapter(mListRecyclerViewAdapter);
        if(null != mFooterView){
            addFooterItem(mFooterView);
        }
        setEmptyView(mEmptyView);
    }

    /**
     * 可以通过getRecyclerAdapter 获取adapter
     * @hide
     */
    @Override
    public Adapter getAdapter() {
        return super.getAdapter();
    }

    public ListRecyclerViewAdapter getRecyclerAdapter(){
       return (ListRecyclerViewAdapter) getAdapter();
    }

    @Override
    public boolean isAdapterExist() {
        return null != getAdapter() ? true : false;
    }

    @Override
    public int getListLastVisiblePosition() {
        int position = -1;
        if(null != mListRecyclerViewAdapter){
            position = mListRecyclerViewAdapter.getLastVisiblePosition();
        }
        return position;
    }

    @Override
    public int getListItemCount() {
        int itemCount = 0;
        if(null != mListRecyclerViewAdapter){
            itemCount =  mListRecyclerViewAdapter.getItemCount();
        }
        return itemCount;
    }

    @Override
    public void addFooterItem(View v) {
        mFooterView = v;
        if(null != mListRecyclerViewAdapter){
            mListRecyclerViewAdapter.addFooterView(mFooterView);
        }
    }

    @Override
    public View getListChildAt(int index) {
        return getChildAt(index);
    }

    /**
     * 改方法只会返回 0,若用户需要头需要自己定义
     * @return
     */
    @Override
    public int getListHeaderViewsCount() {
        int count = 0;
        if(null != mListRecyclerViewAdapter){
            count = mListRecyclerViewAdapter.getHeaderViewsCount();
        }
        return count;
    }

    @Override
    public int getListFooterViewsCount() {
        int count = 0;
        if(null != mListRecyclerViewAdapter){
            count = mListRecyclerViewAdapter.getFooterViewsCount();
        }
        return count;
    }

    @Override
    public void setListEmptyView(View v) {
        setEmptyView(v);
    }

    @Override
    public void removeEmptyView() {
        if(null != mEmptyView){
            mEmptyView.setVisibility(GONE);
            setEmptyView(null);
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        removeEmptyView();
    }

    /**
     * Sets the view to show if the adapter is empty
     */
//    @android.view.RemotableViewMethod
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;

        // If not explicitly specified this view is important for accessibility.

//        if (emptyView != null
//                && emptyView.getImportantForAccessibility() == IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
//            emptyView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
//        }

        ListRecyclerViewAdapter recyclerAdapter = getRecyclerAdapter();
        final boolean empty = ((recyclerAdapter == null) || recyclerAdapter.getListCount() == 0);
        updateEmptyStatus(empty);
    }

    /**
     * Update the status of the list based on the empty parameter.  If empty is true and
     * we have an empty view, display it.  In all the other cases, make sure that the listview
     * is VISIBLE and that the empty view is GONE (if it's not null).
     */
    private void updateEmptyStatus(boolean empty) {
        if (empty) {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            } else {
                // If the caller just removed our empty view, make sure the list view is visible
                setVisibility(View.VISIBLE);
            }

            // We are now GONE, so pending layouts will not be dispatched.
            // Force one here to make sure that the state of the list matches
            // the state of the adapter.
            if (isAnimating()) {
                layout(getLeft(), getTop(), getRight(), getBottom());
            }
        } else {
            if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
            setVisibility(View.VISIBLE);
        }
    }
}
