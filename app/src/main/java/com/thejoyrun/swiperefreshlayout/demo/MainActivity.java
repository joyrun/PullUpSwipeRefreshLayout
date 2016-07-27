package com.thejoyrun.swiperefreshlayout.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thejoyrun.pullupswiperefreshlayout.PullUpSwipeRefreshLayout;
import com.thejoyrun.pullupswiperefreshlayout.SwipeRefreshListView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshListView refresh_layout;
    private SampleAdapter sampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh_layout = (SwipeRefreshListView) findViewById(R.id.swipe_refresh_list_view);
        sampleAdapter = new SampleAdapter(this);

        refresh_layout.getListView().setAdapter(sampleAdapter);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sampleAdapter.clear();
                        addData();
                        refresh_layout.setRefreshing(false);
                    }
                },2000);
            }
        });
        refresh_layout.setOnLoadListener(new PullUpSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                        refresh_layout.setLoading(false);
                    }
                },2000);
            }
        });
        refresh_layout.setLoadAutoEnabled(false);



        refresh_layout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sampleAdapter.clear();
                addData();
                refresh_layout.setRefreshing(false);
            }
        },5000);
    }


    private void addData(){
        for (int i = 0; i < 20; i++) {
            sampleAdapter.add("item : " + new Random().nextInt(555));
        }
        sampleAdapter.notifyDataSetChanged();
    }

    public class SampleAdapter extends BaseListAdapter<String> {

        public SampleAdapter(Context context) {
            super(context);
        }

        @Override
        public Long getItemId(String s) {
            return Long.valueOf(s.hashCode());
        }

        @Override
        public View getItemView(int position, View convertView, ViewHolder viewHolder, ViewGroup viewGroup) {
            TextView textView = viewHolder.getView(R.id.text);
            textView.setText(getItem(position));
            return convertView;
        }

        @Override
        public int getItemLayoutId() {
            return R.layout.item_sample;
        }
    }
}
