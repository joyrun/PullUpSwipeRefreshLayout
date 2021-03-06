package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thejoyrun.pullupswiperefreshlayout.FooterView;

import java.util.List;

import static com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerHelper.ItemType.FOOT_TYPE;
import static com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerHelper.ItemType.HEAD_TYPE;

/**
 * Created by keven on 16/8/24.
 */

public abstract class ListRecyclerViewAdapter<T extends ListRecyclerViewAdapter.BaseViewHolder,E extends View> extends RecyclerView.Adapter<T> {

    private E mFooterView;

    private int mLastItemPosition;

    protected Context mContext;
    private LayoutInflater mLayoutInflater;

    public ListRecyclerViewAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    final public T onCreateViewHolder(ViewGroup parent, int viewType) {
        T holder = null;
        if(viewType == FOOT_TYPE){
            holder = (T)(new BaseViewHolder(mFooterView));
            holder.setItemType(FOOT_TYPE);
            return holder;
        }

        return onCreateViewContentHolder(parent, viewType);
    }

    public abstract T onCreateViewContentHolder(ViewGroup parent, int viewType);

    @Override
    final public void onBindViewHolder(T holder, int position) {
        mLastItemPosition = position;
        if( holder.getItemViewType() == FOOT_TYPE){
            onBindFooterView((E)(holder.itemView), position);
            return;
        }
        onBindViewContentHolder(holder,position);
    }

    public void onBindFooterView(E footerView, int position) {
    }

    /**
     *   与onBindViewHolder一致,
     *  @param holder   该holder不包括 headitem 和 footitem的位置
     * @param position  该position不包括 headitem 和 footitem的位置
     */
    public abstract void onBindViewContentHolder(T holder, int position);

    @Override
    final public int getItemViewType(int position) {
        if(null != mFooterView && (getItemCount() - position) <= 1){
            return FOOT_TYPE;
        }
        return getItemType(position);
    }

    /**
     * 获取item的类型  与getItemViewType一致,
     * @param position  该position不包括 headitem 和 footitem的位置
     * @return
     */
    public abstract int getItemType(int position);

    /**
     * 获取所有item的数量 (包括footeritem)
     * @return
     */
    @Override
    final public int getItemCount() {
        int itemcount = 0;
        itemcount = itemcount + getListCount();
        itemcount = null == mFooterView ? itemcount : itemcount + 1;
        return itemcount;
    }

    /**
     * 获取 item的数目,不需要将 footeritem 的数量返回
     * @return
     */
    public abstract int getListCount();

    public void addFooterView(E v){
        mFooterView = v;
    }

    /**
     * 返回最后一个item的位置  (包括 footItem)
     * @return
     */
    public int getLastVisiblePosition(){
        return mLastItemPosition;
    }

    public int getHeaderViewsCount(){
        return 0;
    }

    public int getFooterViewsCount(){
        return mFooterView == null ? 0 : 1;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        private int itemType;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }
}
