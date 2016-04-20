package com.leenanxi.android.open.feed.broadcast.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Attachment;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.api.model.Photo;
import com.leenanxi.android.open.feed.link.UriHandler;
import com.leenanxi.android.open.feed.profile.ui.ProfileActivity;
import com.leenanxi.android.open.feed.util.*;
import com.leenanxi.android.open.feed.widget.*;

import java.util.ArrayList;

/**
 * A LinearLayout that can display a broadcast.
 * <p>
 * <p>Note that this layout tries to avoid the glitch if the same broadcast is bound again by
 * leaving attachment and text unchanged (since they cannot change once a broadcast is created).</p>
 */
public class BroadcastLayout extends LinearLayout {
    public ImageView mAvatarImage;
    public TextView mNameText;
    public TimeActionTextView mTimeActionText;
    public RelativeLayout mAttachmentLayout;
    public ImageView mAttachmentImage;
    public TextView mAttachmentTitleText;
    public TextView mAttachmentDescriptionText;
    public ImageLayout mSingleImageLayout;
    public FrameLayout mImageListLayout;
    public FrameLayout mImageListDescriptionLayout;
    public TextView mImageListDescriptionText;
    public RatioHeightRecyclerView mImageList;
    public Space mTextSpace;
    public TextView mTextText;
    public CardIconButton mLikeButton;
    public CardIconButton mCommentButton;
    public CardIconButton mRebroadcastButton;
    private HorizontalImageAdapter mImageListAdapter;
    private Long mBoundBroadcastId;

    public BroadcastLayout(Context context) {
        super(context);
        init(getContext(), null, 0, 0);
    }

    public BroadcastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getContext(), attrs, 0, 0);
    }

    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(getContext(), attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BroadcastLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(getContext(), attrs, defStyleAttr, defStyleRes);
    }

    private void initViews() {
        mAvatarImage = (ImageView) findViewById(R.id.avatar);
        mNameText = (TextView) findViewById(R.id.name);
        mTimeActionText = (TimeActionTextView) findViewById(R.id.time_action);
        mAttachmentLayout = (RelativeLayout) findViewById(R.id.attachment);
        mAttachmentImage = (ImageView) findViewById(R.id.attachment_image);
        mAttachmentTitleText = (TextView) findViewById(R.id.attachment_title);
        mAttachmentDescriptionText = (TextView) findViewById(R.id.attachment_description);
        mSingleImageLayout = (ImageLayout) findViewById(R.id.single_image);
        mImageListLayout = (FrameLayout) findViewById(R.id.image_list_layout);
        mImageListDescriptionLayout = (FrameLayout) findViewById(R.id.image_list_description_layout);
        mImageListDescriptionText = (TextView) findViewById(R.id.image_list_description);
        mImageList = (RatioHeightRecyclerView) findViewById(R.id.image_list);
        mTextSpace = (Space) findViewById(R.id.text_space);
        mTextText = (TextView) findViewById(R.id.text);
        mLikeButton = (CardIconButton) findViewById(R.id.like);
        mCommentButton = (CardIconButton) findViewById(R.id.comment);
        mRebroadcastButton = (CardIconButton) findViewById(R.id.rebroadcast);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(VERTICAL);
        inflate(context, R.layout.broadcast_layout, this);
        initViews();
        ViewCompat.setBackground(mImageListDescriptionLayout, DrawableUtils.makeScrimDrawable());
        mImageList.setHasFixedSize(true);
        mImageList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false));
        mImageListAdapter = new HorizontalImageAdapter();
        mImageList.setAdapter(mImageListAdapter);
        mImageList.addOnScrollListener(new OnHorizontalScrollListener() {
            private boolean mShowingDescription = true;

            @Override
            public void onScrolledLeft() {
                if (!mShowingDescription) {
                    mShowingDescription = true;
                    ViewUtils.fadeIn(mImageListDescriptionLayout);
                }
            }

            @Override
            public void onScrolledRight() {
                if (mShowingDescription) {
                    mShowingDescription = false;
                    ViewUtils.fadeOut(mImageListDescriptionLayout);
                }
            }
        });
        ViewUtils.setTextViewLinkClickable(mTextText);
        CheatSheetUtils.setup(mLikeButton);
        CheatSheetUtils.setup(mCommentButton);
        CheatSheetUtils.setup(mRebroadcastButton);
    }

    public void bindBroadcast(final Broadcast broadcast) {
        final Context context = getContext();
        ImageUtils.loadAvatar(mAvatarImage, broadcast.author.avatar, context);
        mAvatarImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(ProfileActivity.makeIntent(broadcast.author, context));
            }
        });
        mNameText.setText(broadcast.author.name);
        mTimeActionText.setTimeAndAction(broadcast.createdAt, broadcast.action);
        boolean isRebind = mBoundBroadcastId != null && mBoundBroadcastId == broadcast.id;
        // HACK: Attachment and text should not change on rebind.
        if (!isRebind) {
            Attachment attachment = broadcast.attachment;
            if (attachment != null) {
                mAttachmentLayout.setVisibility(View.VISIBLE);
                mAttachmentTitleText.setText(attachment.title);
                mAttachmentDescriptionText.setText(attachment.description);
                if (!TextUtils.isEmpty(attachment.image)) {
                    mAttachmentImage.setVisibility(View.VISIBLE);
                    ImageUtils.loadImage(mAttachmentImage, attachment.image, context);
                } else {
                    mAttachmentImage.setVisibility(View.GONE);
                }
                final String attachmentUrl = attachment.href;
                if (!TextUtils.isEmpty(attachmentUrl)) {
                    mAttachmentLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UriHandler.open(attachmentUrl, context);
                        }
                    });
                }
            } else {
                mAttachmentLayout.setVisibility(View.GONE);
            }
            final ArrayList<Image> images = broadcast.images.size() > 0 ? broadcast.images
                    : Photo.toImageList(broadcast.photos);
            int numImages = images.size();
            if (numImages == 1) {
                mSingleImageLayout.setVisibility(View.VISIBLE);
                mSingleImageLayout.loadImage(images.get(0));
                mSingleImageLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(GalleryActivity.makeIntent(images, 0, context));
                    }
                });
            } else {
                mSingleImageLayout.setVisibility(View.GONE);
            }
            if (numImages > 1) {
                mImageListLayout.setVisibility(View.VISIBLE);
                mImageListDescriptionText.setText(context.getString(
                        R.string.broadcast_image_list_count_format, numImages));
                mImageListAdapter.replace(images);
                mImageListAdapter.setOnImageClickListener(
                        new HorizontalImageAdapter.OnImageClickListener() {
                            @Override
                            public void onImageClick(int position) {
                                context.startActivity(GalleryActivity.makeIntent(images, position,
                                        context));
                            }
                        });
            } else {
                mImageListLayout.setVisibility(View.GONE);
            }
            boolean textSpaceVisible = (attachment != null || numImages > 0)
                    && !TextUtils.isEmpty(broadcast.text);
            ViewUtils.setVisibleOrGone(mTextSpace, textSpaceVisible);
            mTextText.setText(broadcast.getTextWithEntities(context));
        }
        mLikeButton.setText(broadcast.getLikeCountString());
        mLikeButton.setActivated(broadcast.liked);
        mLikeButton.setEnabled(true);
        mRebroadcastButton.setActivated(broadcast.isRebroadcasted());
        mRebroadcastButton.setEnabled(true);
        mCommentButton.setText(broadcast.getCommentCountString());
        mBoundBroadcastId = broadcast.id;
    }

    public void releaseBroadcast() {
        mAvatarImage.setImageDrawable(null);
        mAttachmentImage.setImageDrawable(null);
        mSingleImageLayout.releaseImage();
        mImageListAdapter.clear();
        mBoundBroadcastId = null;
    }
}
