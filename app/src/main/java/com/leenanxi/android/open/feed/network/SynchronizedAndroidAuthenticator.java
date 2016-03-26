package com.leenanxi.android.open.feed.network;

import android.accounts.Account;
import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.AndroidAuthenticator;

/**
 * An {@link AndroidAuthenticator} with {@link #getAuthToken()} synchronized.
 */
public class SynchronizedAndroidAuthenticator extends AndroidAuthenticator {
    public SynchronizedAndroidAuthenticator(Context context, Account account,
                                            String authTokenType) {
        super(context, account, authTokenType);
    }

    public SynchronizedAndroidAuthenticator(Context context, Account account, String authTokenType,
                                            boolean notifyAuthFailure) {
        super(context, account, authTokenType, notifyAuthFailure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getAuthToken() throws AuthFailureError {
        return super.getAuthToken();
    }
}
