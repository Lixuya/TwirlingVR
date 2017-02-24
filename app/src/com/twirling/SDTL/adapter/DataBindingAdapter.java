package com.twirling.SDTL.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Target: 为databinding补充绑定方法
 */
public class DataBindingAdapter extends com.twirling.player.databinding.component.DataBindingAdapter {
	@BindingAdapter({"imageUrl"})
	public static void setImageUrl(ImageView view, String url) {
		Glide.with(view.getContext())
				.load(url)
				.asBitmap()
				.centerCrop()
//				.placeholder(drawable)
				.into(view);
	}
}