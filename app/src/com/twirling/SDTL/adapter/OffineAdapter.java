package com.twirling.SDTL.adapter;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.activity.AudioActivity;
import com.twirling.SDTL.activity.SimpleVrVideoActivity;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.download.DownloadChangeObserver;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.SDTL.module.ModuleAlertDialog;
import com.twirling.SDTL.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_download, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OffineAdapter.ViewHolder holder, int position) {
        final VideoItem item = datas.get(position);
        holder.downloadId = item.getDownloadId();
        String path = Constants.PATH_RESOURCE + item.getFolder() + Constants.PAPH_IMAGE + item.getImage();
        Glide.with(holder.itemView.getContext()).load(path).into(holder.iv_background);
        holder.tv_title.setText(item.getName());
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除本地文件
                new ModuleAlertDialog(App.getInst().getCurrentShowActivity()) {
                    @Override
                    protected void onConfirm() {
                        deletefile(item, holder);
                        //
                        RealmHelper.getIns().deleteVideoItem(item);
                        datas.clear();
                        datas.addAll(RealmHelper.getIns().selectVideoList());
                        notifyDataSetChanged();
                    }
                }.setMessage("确定删除 " + item.getName() + " 吗");
            }
        });
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                int vrAudio = item.getVrAudio();
                if (vrAudio == -1) {
                    Intent intent = new Intent(holder.itemView.getContext(), SimpleVrVideoActivity.class);
                    intent.putExtra("videoItem", Constants.PAPH_DOWNLOAD_LOCAL + item.getAppAndroidOnline());
                    holder.itemView.getContext().startActivity(intent);
                } else if (vrAudio != -1 && holder.downloadId == 1) {
                    Intent intent = new Intent(holder.itemView.getContext(), AudioActivity.class);
                    intent.putExtra("videoItem", item);
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        if (holder.downloadId == 1) {
            holder.pb_download.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.cv_card.setEnabled(true);
            return;
        }
        if (holder.downloadId != 1) {
            DownloadChangeObserver pco = (DownloadChangeObserver) App.observers.get(holder.downloadId);
            if (pco == null) {
                return;
            }
            pco.setProgressListener(new DownloadChangeObserver.ProgressListener() {
                @Override
                public void invoke(int progress) {
                    if (progress == 100 || holder.downloadId == 1) {
                        holder.pb_download.setVisibility(View.GONE);
                        holder.tv_title.setVisibility(View.VISIBLE);
                        holder.cv_card.setEnabled(true);
                        return;
                    }
                    holder.cv_card.setEnabled(false);
                    holder.pb_download.setVisibility(View.VISIBLE);
                    holder.tv_title.setVisibility(View.GONE);
                    holder.pb_download.setProgress(progress);
                }
            });
        }
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
        long downloadId;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //
    private void deletefile(VideoItem item, final ViewHolder holder) {
        final String videoName = item.getAppAndroidOnline();
        final String androidOffline = item.getAppAndroidOffline();
        holder.downloadId = RealmHelper.getIns().selectVideoItem(videoName).getDownloadId();
        // TODO 如果下载中，取消下载
        Log.w(getClass() + "", holder.downloadId + " " + item.toString());
        Observable.just(holder.downloadId)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long id) {
                        return id != 1 && id != 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                               @Override
                               public void call(Long aLong) {
                                   DownloadManager dm = App.getDownloadManager();
                                   dm.remove(holder.downloadId);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Log.e(getClass() + "", throwable.toString());
                               }
                           }
                );
        // 主线程更新列表，io删除
        Observable.just(item)
                .filter(new Func1<VideoItem, Boolean>() {
                    @Override
                    public Boolean call(VideoItem item) {
                        return holder.downloadId == 1 && item.getAppAndroidOffline().length() != 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<VideoItem>() {
                    @Override
                    public void call(VideoItem item) {
                        String fileFolder = androidOffline.substring(0, androidOffline.length() - 4);
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + fileFolder + "video.mp4"));
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + fileFolder + "audio.mp4"));
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + fileFolder + "data.json"));
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + fileFolder + "image.jpg"));
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + androidOffline));
                        FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + videoName));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
    }
}