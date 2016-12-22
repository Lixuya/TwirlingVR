package com.twirling.SDTL.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.App;
import com.twirling.SDTL.R;
import com.twirling.SDTL.activity.AudioActivity;
import com.twirling.SDTL.model.AudioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static java.lang.System.load;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    //
    private List<AudioItem> datas = new ArrayList<AudioItem>();
    private List<Integer> mHeights = new ArrayList<Integer>();

    public AudioAdapter(List<AudioItem> datas, List<Integer> mHeights) {
        this.datas = datas;
        this.mHeights = mHeights;
    }

    @Override
    public AudioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ll_item_audio, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AudioAdapter.ViewHolder holder, int position) {
        final AudioItem item = datas.get(position);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.iv_background.getLayoutParams();
        params.height = mHeights.get(position);
        holder.iv_background.setLayoutParams(params);
        //
        Glide.with(App.getInst().getCurrentShowActivity())
                .load(item.getCover())
                .into(holder.iv_background);
        //
        holder.tv_title.setText(item.getTitle());
        holder.audio = item.getAudio();
        RxView.clicks(holder.cv_card)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(holder.itemView.getContext(), AudioActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("AudioItem", item);
                        intent.putExtras(bundle);
                        //
                        ActivityOptions transitionOptions = null;
                        String tra_image = holder.itemView.getContext().getString(R.string.tra_image);
                        Pair<View, String> imagePair = Pair.create((View) holder.iv_background, tra_image);
                        String tra_text = holder.itemView.getContext().getString(R.string.tra_text);
                        Pair<View, String> textPair = Pair.create((View) holder.tv_title, tra_text);
                        String tra_play = holder.itemView.getContext().getString(R.string.tra_play);
                        Pair<View, String> playPair = Pair.create((View) holder.iv_play, tra_play);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            Activity activity = App.getInst().getCurrentShowActivity();
                            transitionOptions = ActivityOptions.makeSceneTransitionAnimation(activity, imagePair, textPair, playPair);
                            holder.itemView.getContext().startActivity(intent, transitionOptions.toBundle());
                        } else {
                            holder.itemView.getContext().startActivity(intent);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_background)
        ImageView iv_background;
        @BindView(R.id.iv_play)
        ImageView iv_play;
        @BindView(R.id.iv_stop)
        ImageView iv_stop;
        @BindView(R.id.cv_card)
        CardView cv_card;
        @BindView(R.id.tv_title)
        TextView tv_title;
        //
        public String audio = "";
        public String image = "";

        //
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Drawable icon = new IconicsDrawable(view.getContext())
                    .icon(FontAwesome.Icon.faw_play)
                    .color(Color.parseColor("#FFFFFF"))
                    .sizeDp(25);
            iv_play.setImageDrawable(icon);
            Drawable icon2 = new IconicsDrawable(view.getContext())
                    .icon(FontAwesome.Icon.faw_pause)
                    .color(Color.parseColor("#FFFFFF"))
                    .sizeDp(25);
            iv_stop.setImageDrawable(icon2);
            //
            iv_background.setBackgroundColor(Color.parseColor("#33" + getRandColorCode()));
        }
    }

    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return r + g + b;
    }
}