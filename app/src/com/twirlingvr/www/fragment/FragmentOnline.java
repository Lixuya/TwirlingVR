package com.twirlingvr.www.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.twirlingvr.www.R;
import com.twirlingvr.www.adapter.MainAdapter;
import com.twirlingvr.www.model.DataArray;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.retrofit.RetrofitManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class FragmentOnline extends Fragment {
    private MainAdapter mAdapter = null;
    private XRecyclerView mRecyclerView = null;
    private List<VideoItem> datas = new ArrayList<VideoItem>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_online, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.mRecyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getBaseContext()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshComplete();
                loadData(1);
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
//                page += 1;
//                loadData(page);
            }
        });
        loadData(1);
        mAdapter = new MainAdapter(datas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(final int page) {
        HashMap<String, Object> params = new HashMap<>();
        RetrofitManager.getService().getVideoList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataArray>() {
                    @Override
                    public void call(DataArray dataArray) {
                        datas.clear();
                        datas.addAll(dataArray.getContent());
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        if (page == 1) {
                            mRecyclerView.refreshComplete();
                        } else {
                            mRecyclerView.loadMoreComplete();
                        }
                    }
                });
    }
}
