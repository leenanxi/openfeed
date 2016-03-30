package com.leenanxi.android.open.feed.util;

import android.content.Context;
import android.os.Handler;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheBuilder;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class CacheHelper {
    private static final int MAX_DISK_CACHE_BYTES = 5 * 1024 * 1024;
    private static final int MAX_RAM_CACHE_BYTES = 1 * 1024 * 1024;
    private static final Object DISK_CACHE_LOCK = new Object();
    private static final int APP_VERSION = 1;
    private static final String KEY = "DUALCACHE";
    private static final ExecutorService sExecutorService = Executors.newSingleThreadExecutor();
    private static volatile DualCache<Object> mCache;

    private CacheHelper() {
    }

    public static DualCache<Object> getDualCache(final Context context) {
        if (mCache == null) {
            synchronized (DISK_CACHE_LOCK) {
                DualCacheContextUtils.setContext(context.getApplicationContext());
                mCache = new DualCacheBuilder<Object>(KEY, APP_VERSION, Object.class)
                        .useDefaultSerializerInRam(MAX_RAM_CACHE_BYTES)
                        .useDefaultSerializerInDisk(MAX_DISK_CACHE_BYTES, true);
            }

        }

        return mCache;
    }

    public static <T> void getData(final String key, final Handler handler,
                                   final Callback<T> callback, final Context context) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                Object o = getDualCache(context).get(key);
                deliverValue(handler, callback, (o != null) ? (T) o : null);
            }
        });
    }

    public static <T> void putData(final String key, final T value, final Context context) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                getDualCache(context).put(key, value);
            }
        });
    }

    private static void executeAsync(Runnable runnable) {
        try {
            sExecutorService.execute(runnable);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        }
    }

    private static <T> void deliverValue(Handler handler, final Callback<T> callback,
                                         final T value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onValue(value);
            }
        });
    }

    public static void delete(final String key, final Context context) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                getDualCache(context).delete(key);
            }
        });
    }

    public static void invalidate(final Context context) {
        executeAsync(new Runnable() {
            @Override
            public void run() {
                getDualCache(context).invalidate();
            }
        });
    }

    public static final String getSafeKeyByString(String key) {
        return key.toLowerCase().replace(".", "_");
    }
}
