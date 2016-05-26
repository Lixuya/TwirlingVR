package com.twirlingvr.www.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twirlingvr.www.R;

public class MainActivity extends ListActivity {
    private ListView mListView;
    private CustomAdapter mAdapter;
    private int mScrollState;
    private View mFooter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //获取ListView
        mListView = getListView();
        //根据footer.xml描述创建View
        mFooter = getLayoutInflater().inflate(R.layout.footer, null);
        // 在ListView底部添加正在加载视图
        //注意：addFooterView方法需要在调用setListAdapter方法前调用！
        mListView.addFooterView(mFooter);
        mAdapter = new CustomAdapter();
        setListAdapter(mAdapter);
        //给ListView添加滚动监听器
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //记录当前状态
                mScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 可视的最后一个列表项的索引
                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                //当列表正处于滑动状态且滑动到列表底部时，执行
                if (mScrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastVisibleItem == totalItemCount - 1) {
                    // 执行线程，模拟睡眠5秒钟后添加10个列表项
                    new Thread() {
                        private Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                //增加Item数量
                                mAdapter.count += 10;
                                //通知数据集变化
                                mAdapter.notify();
                                mAdapter.notifyDataSetChanged();
                            }
                        };

                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(5000);
                                handler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }

    class CustomAdapter extends BaseAdapter {
        // 初始列表项数量
        int count = 20;

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView result = (TextView) convertView;

            //动态创建TextView添加早ListView中
            if (result == null) {
                //result = new TextView(MainActivity.this);
                // result.setTextAppearance(MainActivity.this, android.R.style.TextAppearance_Large);
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams
                        (AbsListView.LayoutParams.FILL_PARENT,
                                AbsListView.LayoutParams.WRAP_CONTENT);
                result.setLayoutParams(layoutParams);
                result.setGravity(Gravity.CENTER);
            }
            result.setText("第 " + (position + 1) + "行");
            return result;
        }
    }
}

