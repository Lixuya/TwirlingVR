package com.twirling.SDTL.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.adapter.AudioAdapter;
import com.twirling.SDTL.model.AudioItem;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.retrofit.RetrofitManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class FragmentAudio extends Fragment {
	private AudioAdapter mAdapter = null;
	private XRecyclerView mRecyclerView = null;
	private List<AudioItem> datas = new ArrayList<>();
	private List<Integer> mHeights = new ArrayList<Integer>();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialBaseTheme_Light);
		LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
		View rootView = localInflater.inflate(R.layout.fragment_list, container, false);
		initView(rootView);
		//
		loadData();
		return rootView;
	}

	private void initView(View view) {
		mRecyclerView = (XRecyclerView) view.findViewById(R.id.mRecyclerview);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
		StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        SpacesItemDecoration decoration = new SpacesItemDecoration(0);
//        mRecyclerView.addItemDecoration(decoration);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
//        mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				loadData();
				mRecyclerView.refreshComplete();
			}

			@Override
			public void onLoadMore() {
				mRecyclerView.loadMoreComplete();
//                page += 1;
			}
		});
		mAdapter = new AudioAdapter(datas, mHeights);
		mRecyclerView.setAdapter(mAdapter);
	}

	private void loadData() {
		HashMap<String, Object> params = new HashMap<>();
		params.put("mobile", Constants.USER_MOBILE);
		RetrofitManager.getService().getAudioList(params)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<DataArray<AudioItem>>() {
					@Override
					public void accept(DataArray<AudioItem> dataArray) throws Exception {
						datas.clear();
						datas.addAll(dataArray.getData());
						//
						mHeights.clear();
						while (mHeights.size() <= datas.size()) {
							mHeights.add((int) (Math.random() * 400) + 400);
						}
						mAdapter.notifyDataSetChanged();
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception{
						Log.e(FragmentAudio.class.getSimpleName(), throwable.toString());
					}
				}, new Action() {
					@Override
					public void run() throws Exception {
						if (true) {
							mRecyclerView.refreshComplete();
						} else {
							mRecyclerView.loadMoreComplete();
						}
					}
				});
	}

	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.top = space;
			}
		}
	}
}