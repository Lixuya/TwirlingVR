package com.twirling.SDTL.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.SDTL.module.ModuleAlertDialog;
import com.twirling.libtwirling.utils.FileUtil;
import com.twirling.player.activity.VRPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class OffineAdapter extends RecyclerView.Adapter<OffineAdapter.ViewHolder> {
	//
	private List<VideoItem> datas = new ArrayList<VideoItem>();

	public OffineAdapter(List<VideoItem> datas) {
		this.datas = datas;
	}


	@Override
	public OffineAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ll_item_download, viewGroup, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	@Override
	public void onBindViewHolder(final OffineAdapter.ViewHolder holder, int position) {
		final VideoItem item = datas.get(position);
		String path = Constants.PATH_RESOURCE + item.getFolder() + Constants.PAPH_IMAGE + item.getImage();
		String videoUrl = Constants.PATH_RESOURCE + item.getFolder() + item.getAppAndroidOffline();
		Glide.with(holder.itemView.getContext()).load(path).into(holder.iv_background);
		holder.tv_title.setText(item.getName());
		//
		RxView.clicks(holder.iv_delete)
				.throttleFirst(2, TimeUnit.SECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) throws Exception {
						// 删除本地文件
						new ModuleAlertDialog(App.getInst().getCurrentShowActivity()) {
							@Override
							protected void onConfirm() {
								deletefile(item, holder);
								//
								datas.clear();
								datas.addAll(RealmHelper.getInstance().selectVideoList());
								notifyDataSetChanged();
							}
						}.setMessage("确定删除 " + item.getName() + " 吗");
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(getClass() + "", throwable.toString());
					}
				});
		holder.cv_card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//
				int vrAudio = item.getVrAudio();
				if (vrAudio == -1) {
					Intent intent = new Intent(holder.itemView.getContext(), VRPlayerActivity.class);
					intent.putExtra("VideoItem", Constants.PATH_DOWNLOAD + item.getAppAndroidOnline());
					holder.itemView.getContext().startActivity(intent);
				} else {
					Intent intent = new Intent(holder.itemView.getContext(), VRPlayerActivity.class);
					intent.putExtra("VideoItem", Constants.PATH_DOWNLOAD + item.getAppAndroidOffline());
					holder.itemView.getContext().startActivity(intent);
				}
			}
		});
		holder.cv_card.setEnabled(true);
		RxDownload.getInstance()
				.receiveDownloadStatus(videoUrl)
				.subscribe(new Consumer<DownloadEvent>() {
					@Override
					public void accept(DownloadEvent event) throws Exception {
						int progress = 0;
						if (event.getFlag() == DownloadFlag.FAILED) {
							Throwable throwable = event.getError();
							Log.w("Error", throwable);
						} else if (event.getFlag() == DownloadFlag.COMPLETED) {
							Toast.makeText(holder.itemView.getContext(), "下载完成", Toast.LENGTH_LONG).show();
						} else {
							if (event.getDownloadStatus().getTotalSize() != 0) {
								downloading(holder);
								progress = (int) (event.getDownloadStatus().getDownloadSize() * 100f / event.getDownloadStatus().getTotalSize());
								holder.pb_download.setProgress(progress);
							}
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.w("www", throwable.toString());
					}
				});
	}

	private void downloading(OffineAdapter.ViewHolder holder) {
		holder.cv_card.setEnabled(false);
		holder.pb_download.setVisibility(View.VISIBLE);
		holder.tv_title.setVisibility(View.GONE);
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.iv_background)
		ImageView iv_background;
		@BindView(R.id.iv_delete)
		ImageView iv_delete;
		@BindView(R.id.cv_card)
		CardView cv_card;
		@BindView(R.id.tv_title)
		TextView tv_title;
		@BindView(R.id.pb_download)
		ProgressBar pb_download;

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
			Drawable icon = new IconicsDrawable(view.getContext())
					.icon(FontAwesome.Icon.faw_trash_o)
					.color(Color.parseColor("#FFFFFF"))
					.sizeDp(25);
			iv_delete.setImageDrawable(icon);
		}
	}

	//
	private void deletefile(VideoItem item, final ViewHolder holder) {
		final String androidOffline = item.getAppAndroidOffline();
		RealmHelper.getInstance().deleteVideoItem(item);
		String videoUrl = Constants.PATH_RESOURCE + item.getFolder() + androidOffline;
		// 如果下载中，取消下载
		RxDownload.getInstance()
				.cancelServiceDownload(videoUrl)
				.subscribe();
		// 主线程更新列表，io删除
		Observable.just(item)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Consumer<VideoItem>() {
					@Override
					public void accept(VideoItem item) {
						FileUtil.delete(new File(Constants.PATH_DOWNLOAD + androidOffline));
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						Log.e(getClass() + "", throwable.toString());
					}
				});
	}
}