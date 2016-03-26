package com.leenanxi.android.open.feed.network;

import com.android.volley.AuthFailureError;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.Map;

public class HurlStack extends com.android.volley.toolbox.HurlStack {
    public HurlStack() {
    }

    public HurlStack(UrlRewriter urlRewriter) {
        super(urlRewriter);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        super(urlRewriter, sslSocketFactory);
    }

    @Override
    public org.apache.http.HttpResponse performRequest(com.android.volley.Request<?> baseRequest,
                                                       Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        if (!(baseRequest instanceof Request<?>)) {
            throw new IllegalArgumentException("Use " + Request.class.getName() + "instead");
        }
        Request<?> request = (Request<?>) baseRequest;
        request.onPreparePerformRequest();
        return super.performRequest(baseRequest, additionalHeaders);
    }
}
