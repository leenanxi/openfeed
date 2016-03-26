package com.leenanxi.android.open.feed.api;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.Authenticator;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.leenanxi.android.open.feed.api.model.Notification;
import com.leenanxi.android.open.feed.network.Request;
import com.leenanxi.android.open.feed.network.Volley;
import com.leenanxi.android.open.feed.util.GsonHelper;
import com.leenanxi.android.open.feed.util.LogUtils;
import org.apache.commons.logging.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class ApiRequest<T> extends Request<T> {
    private Type mType;
    private Authenticator mAuthenticator;
    private String mAuthToken;

    public ApiRequest(int method, String url, Type type, Context context) {
        super(method, url);
        mType = type;
        mAuthenticator = Volley.getInstance(context).getAuthenticator();
        setRetryPolicy(new RetryPolicy(ApiContract.Request.INITIAL_TIMEOUT_MS,
                ApiContract.Request.MAX_NUM_RETRIES, ApiContract.Request.BACKOFF_MULTIPLIER));
        addParam(ApiContract.Request.Base.API_KEY, ApiContract.Request.API_KEY);
        try {
            LogUtils.d("NETWORK - " + url + " - " + mAuthenticator.getAuthToken());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public ApiRequest(int method, String url, TypeToken<T> typeToken, Context context) {
        this(method, url, typeToken.getType(), context);
    }

    @Override
    public void onPreparePerformRequest() throws AuthFailureError {
        setAuthorization();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Gson gson = GsonHelper.get();
        String responseString;
        try {
            responseString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers,"UTF-8"));
            LogUtils.d("NETWORK - " + responseString);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        try {
            return Response.success(gson.<T>fromJson(responseString, mType),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonParseException | OutOfMemoryError e) {
            LogUtils.e("Error when parsing response: " + responseString);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected ApiError parseNetworkError(VolleyError volleyError) {
        return ApiError.wrap(volleyError);
    }

    private void setAuthorization() throws AuthFailureError {
        mAuthToken = mAuthenticator.getAuthToken();
        addHeaderAuthorizationBearer(mAuthToken);
    }

    private class RetryPolicy extends DefaultRetryPolicy {
        public RetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
            super(initialTimeoutMs, maxNumRetries, backoffMultiplier);
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {
            ApiError apiError = ApiError.wrap(error);
            switch (apiError.code) {
                case ApiContract.Response.Error.Codes.Token.INVALID_ACCESS_TOKEN:
                case ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_HAS_EXPIRED:
                case ApiContract.Response.Error.Codes.Token.INVALID_REFRESH_TOKEN:
                case ApiContract.Response.Error.Codes.Token
                        .ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED:
                    mAuthenticator.invalidateAuthToken(mAuthToken);
                    setAuthorization();
                    super.retry(error);
                    break;
                default:
                    // Don't retry on other error.
                    throw error;
            }
        }
    }
}
