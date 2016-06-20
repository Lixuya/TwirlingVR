package com.twirlingvr.www.adapter;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
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
import com.twirlingvr.www.App;
import com.twirlingvr.www.R;
import com.twirlingvr.www.activity.AudioActivity;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.utils.Constants;
import com.twirlingvr.www.utils.DownloadChangeObserver;
import com.twirlingvr.www.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        Log.w("downloadId", holder.downloadId + "");
        Glide.with(holder.itemView.getContext()).load(Constants.PAPH_IMAGE + item.getImage()).into(holder.iv_background);
        holder.tv_title.setText(item.getName());
        //
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果下载中，取消下载
                holder.downloadId = RealmHelper.getIns().selectVideoItem(item.getVideo()).getDownloadId();
                Log.w("downloadId", holder.downloadId + "");
                if (holder.downloadId != 1 && holder.downloadId != 0) {
                    DownloadManager dm = (DownloadManager) App.getInst().getApplicationContext().getSystemService(
                            App.getInst().getApplicationContext().DOWNLOAD_SERVICE);
                    dm.remove(RealmHelper.getIns().selectVideoItem(item.getVideo()).getDownloadId());
                }
                // 删除本地文件
                else if (holder.downloadId == 1) {
                    FileUtil.delete(Uri.parse(Constants.URI_VIDEO + item.getVideo()));
                }
                // 删除数据库下载记录
                RealmHelper.getIns().deleteVideoItem(item);
                datas.clear();
                datas.addAll(RealmHelper.getIns().selectVideoList());
                notifyDataSetChanged();
            }
        });
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                Intent intent = new Intent(holder.itemView.getContext(), AudioActivity.class);
                intent.putExtra("videoUrl", Constants.URI_VIDEO + item.getVideo());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        if (holder.downloadId == 1) {
            holder.pb_download.setVisibility(View.GONE);
            holder.tv_title.setVisibility(View.VISIBLE);
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
                        return;
                    }
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
}