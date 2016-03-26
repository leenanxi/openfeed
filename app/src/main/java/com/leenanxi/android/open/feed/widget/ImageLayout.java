package com.leenanxi.android.open.feed.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.util.ImageUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class ImageLayout extends FrameLayout {
    public static final int FILL_ORIENTATION_HORIZONTAL = 0;
    public static final int FILL_ORIENTATION_VERTICAL = 1;
    @Bind(R.id.imagelayout_image)
    RatioImageView mImageView;
    @Bind(R.id.imagelayout_gif)
    ImageView mGifImage;

    public ImageLayout(Context context) {
        super(context);
        init(getContext(), null, 0, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getContext(), attrs, 0, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setClickable(true);
        setFocusable(true);
        inflate(context, R.layout.image_layout, this);
        ButterKnife.bind(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageLayout, defStyleAttr,
                defStyleRes);
        int fillOrientation = a.getInt(R.styleable.ImageLayout_fillOrientation,
                FILL_ORIENTATION_HORIZONTAL);
        a.recycle();
        LayoutParams layoutParams = (LayoutParams) mImageView.getLayoutParams();
        layoutParams.width = fillOrientation == FILL_ORIENTATION_HORIZONTAL ?
                LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT;
        layoutParams.height = fillOrientation == FILL_ORIENTATION_HORIZONTAL ?
                LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT;
        mImageView.setLayoutParams(layoutParams);
    }

    public void loadImage(Image image) {
        ImageUtils.loadImage(mImageView, image, getContext());
        ViewUtils.setVisibleOrGone(mGifImage, image.animated);
    }

    public void releaseImage() {
        mImageView.setImageDrawable(null);
    }
}
