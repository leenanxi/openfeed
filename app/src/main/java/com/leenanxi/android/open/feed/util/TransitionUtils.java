package com.leenanxi.android.open.feed.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;

/**
 * Facts:
 * - System window must be shared elements if fullscreen, otherwise overlap occurs.
 * - Shared element transition overlaps old and new elements for a period of time.
 * - Translucent element overlap is unacceptable.
 */
public class TransitionUtils {
    private static final String TRANSITION_NAME_APPBAR = "appbar";

    private TransitionUtils() {
    }

    public static boolean shouldEnableTransition() {
        // I hprof-ed the app, and found that bitmaps are kept by
        // ExitTransitionCoordinator.mSharedElementsBundle, which is in turn kept by
        // ResultReceiver$MyResultReceiver, which is kept by a FinalizerReference.
        // But this fix (
        // https://android.googlesource.com/platform/frameworks/base/+/a0a0260e48e1ee4e9b5d98b49571e8d2a6fd6c3a
        // ) should have been incorporated into android-5.0.0_r1. So I really don't know the root
        // cause now.﻿
        // New finding at 2015-12-06: OOM even happens on Android 5.1 (CM).
        // TODO: Allow disabling transition on some pre-marshmallow devices for OOM.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setupTransitionBeforeDecorate(Activity activity) {
        if (!shouldEnableTransition()) {
            return;
        }
        Window window = activity.getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setSharedElementsUseOverlay(false);
    }

    // AppCompatDelegateImplV7.setContentView() removes all views under android.R.id.content, so we
    // have to do this after it.
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setupTransitionAfterSetContentView(Activity activity) {
        if (!shouldEnableTransition()) {
            return;
        }
        postponeTransitionUntilDecorViewPreDraw(activity);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void postponeTransitionUntilDecorViewPreDraw(final Activity activity) {
        if (!shouldEnableTransition()) {
            return;
        }
        activity.postponeEnterTransition();
        ViewUtils.postOnPreDraw(activity.getWindow().getDecorView(), new Runnable() {
            @Override
            public void run() {
                activity.startPostponedEnterTransition();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setEnterReturnExplode(Activity activity) {
        if (!shouldEnableTransition()) {
            return;
        }
        Window window = activity.getWindow();
        Transition explode = new Explode()
                .excludeTarget(android.R.id.statusBarBackground, true)
                .excludeTarget(android.R.id.navigationBarBackground, true);
        window.setEnterTransition(explode);
        window.setReturnTransition(explode);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void postAfterTransition(Activity activity, Runnable runnable) {
        if (!shouldEnableTransition()) {
            runnable.run();
            return;
        }
        // HACK: Horrible hack, because we have no good way of being notified at the end of
        // transition.
        activity.getWindow().getDecorView().postDelayed(runnable,
                ViewUtils.getMediumAnimTime(activity));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bundle makeActivityOptionsBundle(Activity activity, View... sharedViews) {
        if (!shouldEnableTransition()) {
            return null;
        }
        ArrayList<Pair<View, String>> sharedElementList = new ArrayList<>();
        for (View sharedView : sharedViews) {
            sharedElementList.add(Pair.create(sharedView, sharedView.getTransitionName()));
        }
//        View appbar = activity.findViewById(R.id.appBarWrapper);
//        if (appbar != null) {
//            sharedElementList.add(Pair.create(appbar, appbar.getTransitionName()));
//        }
        //noinspection unchecked
        Pair<View, String>[] sharedElements =
                sharedElementList.toArray(new Pair[sharedElementList.size()]);
        //noinspection unchecked
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements)
                .toBundle();
    }
}
