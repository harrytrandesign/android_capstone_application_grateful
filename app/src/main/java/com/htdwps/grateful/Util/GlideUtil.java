package com.htdwps.grateful.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.htdwps.grateful.R;

/**
 * Created by HTDWPS on 12/8/17.
 */

public class GlideUtil {

    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary));
        Glide.with(context)
                .load(url)
                .placeholder(colorDrawable)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadProfileIcon(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .fitCenter()
                .into(imageView);
    }

}
