package com.thejoyrun.swiperefreshlayout.demo;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的ListView适配器，继承该类，实现getItemId类可以实现去重
 *
 * @param <E>
 * @author Wiki
 */
public abstract class BaseListAdapter<E> extends BaseAdapter {
    protected List<E> items;
    public final String TAG = getClass().getSimpleName();

    public void insert(E item, int to) {
        items.add(to, item);
    }

    public void add(E[] day_type_name) {
        for (E e : day_type_name) {
            add(e);
        }
    }


    protected LayoutInflater mInflater;// 得到�?��LayoutInfalter对象用来导入布局
    protected Context mContext;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        items = new ArrayList<E>();
    }


    public abstract Long getItemId(E e);


    /**
     * 清空�?��的items
     */
    public void clear() {
        items.clear();
    }

    public void remove(long id) {
        this.items.remove(getItemById(id));

    }

    /**
     * 添加�?��对象到items
     *
     * @param object
     */
    public void add(E object) {
        for (E item : items) {
            if (equals(object, item)) {
                return;
            }
        }
        items.add(object);
    }

    public void add(int location, E object) {
        for (E item : items) {
            if (equals(object, item)) {
                return;
            }
        }
        items.add(location, object);
    }

	/*
     * public List<E> getItems() { return items; }
	 *
	 * public void setItems(List<E> items) { this.items = items; }
	 */

    /**
     * 添加�?��list到items
     *
     * @param list
     */
    public void add(List<E> list) {
        if (list != null)
            for (E item : list) {
                add(item);
            }
    }

    /**
     * 覆盖原来的item
     *
     * @param object
     */
    public void update(E object) {
        long postion = getPosition(object);
        if (postion >= 0) {
            this.items.set((int) postion, object);
        }
        notifyDataSetChanged();
    }

    /*
     * 获取items的大�?
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /*
     * 根据position获取item
     */
    @Override
    public E getItem(int position) {
        return items.get(position);
    }

    public List<E> getItems() {
        return items;
    }

    /*
     * 根据position获取item的id
     */
    @Override
    public long getItemId(int position) {
        Long id = getItemId(this.items.get(position));
        if (id == null) {
            return position;
        }
        return id;

    }

    public E getItemById(long id) {
        for (int i = 0; i < items.size(); i++) {
            if (getItemId(i) == id) {
                return items.get(i);
            }
        }
        return null;
    }

    public long getPosition(E object) {
        for (int i = 0; i < items.size(); i++) {
            if (equals(object, items.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean equals(E arg0, E arg1) {

        Long id1 = getItemId(arg0);
        Long id2 = getItemId(arg1);
        if (id1 == null) {
            return arg0.equals(arg1);
        }
        return id1.equals(id2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(getItemLayoutId(), viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return getItemView(position, convertView, viewHolder, viewGroup);
    }
    // public abstract void setItem(ViewHolder viewHolder, E item,int position);

    public abstract View getItemView(int position, View convertView, ViewHolder viewHolder, ViewGroup viewGroup);

    /**
     * 抽象方法，设置子布局的ID
     *
     * @return
     */
    public abstract int getItemLayoutId();

    /**
     * 存放view控件
     */
    public static class ViewHolder {
        private SparseArray<View> viewArray = new SparseArray<View>();
        private View convertView;

        public ViewHolder(View view) {
            this.convertView = view;
        }

        public <T extends View> T getView(int viewId) {
            View v = viewArray.get(viewId);
            if (v == null) {
                v = convertView.findViewById(viewId);
                viewArray.put(viewId, v);
            }
            return (T) v;
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setItems(List<E> items) {
        this.items = items;
    }
}