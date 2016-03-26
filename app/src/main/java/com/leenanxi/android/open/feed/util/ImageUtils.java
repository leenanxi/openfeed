package com.leenanxi.android.open.feed.util;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.widget.RatioImageView;

public class ImageUtils {
    public static void loadAvatar(ImageView view, String url, Context context) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.md_ic_avatar_grey600_40dp)
                .dontAnimate()
                .dontTransform()
                .into(view);
    }

    public static void loadProfileAvatar(final ImageView view, String url, Context context) {
        ViewUtils.fadeOut(view);
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .dontTransform()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        if (e == null) {
                            new NullPointerException().printStackTrace();
                        } else {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        ViewUtils.fadeIn(view);
                        return false;
                    }
                })
                .into(view);
    }

    public static void loadImage(RatioImageView view, Image image, Context context) {
        view.setRatio(image.width, image.height);
        Glide.with(context)
                .load(image.medium)
                // dontTransform() is required for our RatioImageView to work correctly.
                .dontTransform()
                .placeholder(android.R.color.transparent)
                .into(view);
    }

    public static void loadImage(ImageView view, String url,
                                 RequestListener<String, GlideDrawable> listener, Context context) {
        Glide.with(context)
                .load(url)
                .dontTransform()
                .placeholder(android.R.color.transparent)
                .listener(listener)
                .into(view);
    }

    public static void loadImage(ImageView view, String url, Context context) {
        loadImage(view, url, null, context);
    }
}
