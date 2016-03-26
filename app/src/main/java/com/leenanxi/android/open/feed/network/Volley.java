package com.leenanxi.android.open.feed.network;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Authenticator;
import com.leenanxi.android.open.feed.account.contract.AccountContract;
import com.leenanxi.android.open.feed.account.util.AccountUtils;

public class Volley {
    private static final Object INSTANCE_LOCK = new Object();
    private static Volley sInstance;
    private Authenticator mAuthenticator;
    private RequestQueue mRequestQueue;

    private Volley(Context context) {
        context = context.getApplicationContext();
        notifyActiveAccountChanged(context);
        mRequestQueue = getRequestQueue(context);
        mRequestQueue.start();
    }

    public static Volley peekInstance() {
        return sInstance;
    }

    public static Volley getInstance(Context context) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new Volley(context);
            }
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context, new HurlStack());
        }
        return mRequestQueue;
    }

    public void notifyActiveAccountChanged(Context context) {
        context = context.getApplicationContext();
        mAuthenticator = new SynchronizedAndroidAuthenticator(context,
                AccountUtils.getActiveAccount(context), AccountContract.AUTH_TOKEN_TYPE, true);
    }

    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
        return request;
    }

    public void cancelRequests(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
