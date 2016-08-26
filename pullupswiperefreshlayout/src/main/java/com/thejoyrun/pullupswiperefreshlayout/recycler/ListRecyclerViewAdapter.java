package com.thejoyrun.pullupswiperefreshlayout.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerHelper.ItemType.FOOT_TYPE;
import static com.thejoyrun.pullupswiperefreshlayout.recycler.ListRecyclerHelper.ItemType.HEAD_TYPE;

/**
 * Created by keven on 16/8/24.
 */

public abstract class ListRecyclerViewAdapter<T extends ListRecyclerViewAdapter.BaseViewHolder> extends RecyclerView.Adapter {

    private View mFooterView;

    private int mLastItemPosition;

    protected Context mContext;
    private LayoutInflater mLayoutInflater;

    public ListRecyclerViewAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    final public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        if(viewType == FOOT_TYPE){
            holder = new BaseViewHolder(mFooterView);
            holder.setItemType(FOOT_TYPE);
            return holder;
        }

        return onCreateViewContentHolder(parent, viewType);
    }

    public abstract BaseViewHolder onCreateViewContentHolder(ViewGroup parent, int viewType);

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mLastItemPosition = position;
        if( holder.getItemViewType() == FOOT_TYPE){
            return;
        }

        onBindViewContentHolder(holder,position);
    }
    
    /**
     *   与onBindViewHolder一致,
     *  @param holder   该holder不包括 headitem 和 footitem的位置
     * @param position  该position不包括 headitem 和 footitem的位置
     */
    public abstract void onBindViewContentHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    final public int getItemViewType(int position) {
        if(null != mFooterView && (getItemCount() - position) <= 1){
            return FOOT_TYPE;
        }
        return super.getItemViewType(position);
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
        itemcount = null == mFooterView ? itemcount : itemcount + 1;
        itemcount = itemcount + getListCount();
        return itemcount;
    }

    /**
     * 获取 item的数目,不需要将 footeritem 的数量返回
     * @return
     */
    public abstract int getListCount();

    public void addFooterView(View v){
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
