package com.twirling.SDTL.fragment;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.twirling.SDTL.R;
import com.twirling.SDTL.adapter.OffineAdapter;
import com.twirling.lib_cobb.util.FileUtil;
import com.twirling.player.Constants;
import com.twirling.player.model.OfflineModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Target: 本地视频页
 */
public class FragmentDownload extends Fragment {
	private View rootView = null;
	private XRecyclerView recyclerView;
	private OffineAdapter adapter = null;
	private List<OfflineModel> models = new ArrayList<OfflineModel>();
	private boolean know = false;
	private Snackbar snackbar = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(com.twirling.player.R.layout.fragment_download, container, false);
		//
		GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getBaseContext()));
		recyclerView = (XRecyclerView) rootView.findViewById(com.twirling.player.R.id.rv);
		recyclerView.setBackgroundResource(R.color.colorBackground);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
//        mRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
//        mRecyclerView.setHasFixedSize(true);
		recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				loadData();
				recyclerView.refreshComplete();
			}

			@Override
			public void onLoadMore() {
				recyclerView.loadMoreComplete();
			}
		});
		adapter = new OffineAdapter(models);
		recyclerView.setAdapter(adapter);
		//
		snackbar = Snackbar.make(container, 0, 0);
		snackbar.setActionTextColor(Color.YELLOW)
				.setDuration(Snackbar.LENGTH_INDEFINITE)
				.setAction("我知道了", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						know = true;
					}
				});
		return rootView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			loadData();
			if (know) {
				snackbar.show();
			}
		}
	}

	private void loadData() {
		new RxPermissions(getActivity())
				.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.doOnNext(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) throws Exception {
						if (!granted) {
							Toast.makeText(getActivity(), "请到设置中心打开应用存储权限", Toast.LENGTH_LONG).show();
						}
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean aBoolean) throws Exception {
						models.clear();
						List<String> strings = FileUtil.Get.getFileList();
						int i = 0;
						OfflineModel model = null;
						for (String name : strings) {
							model = new OfflineModel(getActivity());
							model.setName(name);
							model.setAsset(false);
							model.setVideoPath(Constants.PATH_MOVIES + name);
							models.add(model);
							i++;
						}
						strings = FileUtil.Get.getAssetList(getActivity());
						for (String name : strings) {
							model = new OfflineModel(getActivity());
							model.setName(name);
							model.setAsset(true);
							model.setVideoPath(Constants.PATH_MOVIES + name);
							models.add(model);
							i++;
						}
//						DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallBack<>(oldModels, models), true);
//						result.dispatchUpdatesTo(adapter);
//						adapter.setModels(models);
						adapter.notifyDataSetChanged();
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Toast.makeText(rootView.getContext(), "刷新失败", Toast.LENGTH_LONG).show();
					}
				});
	}
}