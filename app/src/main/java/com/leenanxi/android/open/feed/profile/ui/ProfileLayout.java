package com.leenanxi.android.open.feed.profile.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.ButterKnife;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.ViewUtils;
import com.leenanxi.android.open.feed.widget.FlexibleSpaceLayout;
import com.leenanxi.android.open.feed.widget.IntProperty;

public class ProfileLayout extends FlexibleSpaceLayout {
    public static final IntProperty<ProfileLayout> OFFSET =
            new IntProperty<ProfileLayout>("offset") {
                @Override
                public Integer get(ProfileLayout object) {
                    return object.getOffset();
                }

                @Override
                public void setValue(ProfileLayout object, int value) {
                    object.offsetTo(value);
                }
            };
    @BindInt(android.R.integer.config_shortAnimTime)
    int mShortAnimationTime;
    @BindColor(R.color.dark_70_percent)
    int mBackgroundColor;
    private ColorDrawable mWindowBackground;
    private ViewGroup mOffsetContainer;
    private ProfileHeaderLayout mProfileHeaderLayout;
    private Listener mListener;
    private boolean mExiting;

    public ProfileLayout(Context context) {
        super(context);
        init();
    }

    public ProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProfileLayout(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        mWindowBackground = new ColorDrawable(mBackgroundColor);
        ((Activity) getContext()).getWindow().setBackgroundDrawable(mWindowBackground);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // HACK: Coupled with specific XML hierarchy.
        mOffsetContainer = (ViewGroup) getChildAt(0);
        mProfileHeaderLayout = (ProfileHeaderLayout) mOffsetContainer.getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int headerMaxHeight = MeasureSpec.getSize(heightMeasureSpec) * 2 / 3;
        mProfileHeaderLayout.setMaxHeight(headerMaxHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getOffset() {
        // View.offsetTopAndBottom causes transient invalid layout position when animating in.
        return (int) mOffsetContainer.getTranslationY();
    }

    public void offsetTo(int offset) {
        if (offset < 0 || getOffset() == offset) {
            return;
        }
        mOffsetContainer.setTranslationY(offset);
        updateWindowBackground(offset);
    }

    public void offsetBy(int delta) {
        offsetTo(getOffset() + delta);
    }

    private void updateWindowBackground(int offset) {
        float fraction = Math.max(0, 1 - (float) offset / getHeight());
        mWindowBackground.setAlpha((int) (fraction * 0xFF));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mExiting) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mExiting) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mExiting) {
            return false;
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    protected void onDrag(MotionEvent event, float delta) {
        if (delta > 0) {
            int oldScroll = getScroll();
            scrollBy((int) -delta);
            delta += getScroll() - oldScroll;
            offsetBy((int) delta);
        } else {
            int oldOffset = getOffset();
            offsetBy((int) delta);
            delta -= getOffset() - oldOffset;
            int oldScroll = getScroll();
            scrollBy((int) -delta);
            delta += getScroll() - oldScroll;
            if (delta < 0) {
                pullEdgeEffectBottom(event, delta);
            }
        }
    }

    @Override
    protected void onDragEnd(boolean cancelled) {
        if (getOffset() > 0) {
            exit();
        } else {
            super.onDragEnd(cancelled);
        }
    }

    public Listener getListener() {
        return mListener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void enter() {
        ViewUtils.postOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                animateEnter();
            }
        });
    }

    private void animateEnter() {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, OFFSET, getHeight(), 0);
        animator.setDuration(mShortAnimationTime);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onEnterAnimationEnd();
                }
            }
        });
        animator.start();
    }

    public void exit() {
        mExiting = true;
        abortScrollerAnimation();
        recycleVelocityTrackerIfHas();
        animateExit();
    }

    private void animateExit() {
        int offset = getOffset();
        int height = getHeight();
        if (offset >= height) {
            mListener.onExitAnimationEnd();
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofInt(this, OFFSET, offset, height);
        animator.setDuration(mShortAnimationTime);
        animator.setInterpolator(new FastOutLinearInInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onExitAnimationEnd();
                }
            }
        });
        animator.start();
    }

    public interface Listener {
        void onEnterAnimationEnd();

        void onExitAnimationEnd();
    }
}
