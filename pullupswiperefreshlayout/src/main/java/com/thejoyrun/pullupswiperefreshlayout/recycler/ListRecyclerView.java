package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by keven on 16/8/24.
 */

public class ListRecyclerView extends RecyclerView {

    private ListRecyclerViewAdapter mListRecyclerViewAdapter;

    public ListRecyclerView(Context context) {
        this(context,null,0);
    }

    public ListRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    final public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    final public Adapter getAdapter() {
        return super.getAdapter();
    }

    /**
     * 设置 adapter 其用法跟setAdapter一样
     * @param adapter
     */
    public void setRecyclerAdapter(ListRecyclerViewAdapter adapter){
        mListRecyclerViewAdapter = adapter;
        setAdapter(mListRecyclerViewAdapter);
    }

    public ListRecyclerViewAdapter getRecyclerAdapter(){
       return (ListRecyclerViewAdapter) getAdapter();
    }

    public int getHeaderViewsCount(){
        if(null != mListRecyclerViewAdapter){
           return mListRecyclerViewAdapter.getHeaderViewsCount();
        }
        return 0;
    }

    public int getFooterViewsCount(){
        if(null != mListRecyclerViewAdapter){
            return mListRecyclerViewAdapter.getFooterViewsCount();
        }
        return 0;
    }

}
