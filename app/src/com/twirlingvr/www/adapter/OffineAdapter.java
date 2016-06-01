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
import com.twirlingvr.www.activity.SimpleVrVideoActivity;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.utils.Constants;
import com.twirlingvr.www.utils.DownloadChangeObserver;
import com.twirlingvr.www.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;

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
        Log.w("item", item + "");
        String imageName = item.getImageName();
        String title = item.getTitle();
//        final String videoName = item.getVideoName();
        Glide.with(holder.itemView.getContext()).load(Constants.PAPH_IMAGE + imageName).into(holder.iv_background);
        holder.tv_title.setText(title);
        //
//        holder.pb_download.startIntro();
        DownloadChangeObserver pco = (DownloadChangeObserver) App.observer;
        if (pco != null) {
            pco.setProgressListener(new DownloadChangeObserver.ProgressListener() {
                @Override
                public void invoke(int progress) {
                    if (progress == 100) {
                        holder.pb_download.setVisibility(View.GONE);
                        holder.tv_title.setVisibility(View.VISIBLE);
                    } else {
                        holder.pb_download.setVisibility(View.VISIBLE);
                        holder.tv_title.setVisibility(View.GONE);
                        holder.pb_download.setProgress(progress);
                    }
                }
            });
        }
        //
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果下载中，取消下载
                if (App.services.size() != 0) {
                    DownloadManager dm = (DownloadManager) App.getInst().getApplicationContext().getSystemService(
                            App.getInst().getApplicationContext().DOWNLOAD_SERVICE);
                    dm.remove(App.services.get(item.getVideoName()));
                }
                // 删除本地文件
                else {
                    FileUtil.delete(Uri.parse(Constants.URI_VIDEO + item.getVideoName()));
                }
                // 删除数据库下载记录
                RealmHelper.getIns().deleteVideoItem(item);
                datas.clear();
                datas.addAll(RealmHelper.getIns().selectVideoList());
                notifyDataSetChanged();
            }
        });
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//
                Intent intent = new Intent(holder.itemView.getContext(), SimpleVrVideoActivity.class);
                intent.putExtra("videoUrl", Constants.URI_VIDEO + item.getVideoName());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_background,
                iv_delete;
        private CardView cv_card = null;
        private TextView tv_title = null;
        private ProgressBar pb_download = null;

        public ViewHolder(View view) {
            super(view);
            iv_background = (ImageView) view.findViewById(R.id.iv_background);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            cv_card = (CardView) view.findViewById(R.id.cv_card);
            pb_download = (ProgressBar) view.findViewById(R.id.pb_download);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}