package com.twirling.libtwirling.utils.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by xieqi on 2016/10/14.
 */

public class FontUtil {
    public static final String ROOT = "fonts/";

    public static final String FONTAWESOME = "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), FONTAWESOME);
    }
}
