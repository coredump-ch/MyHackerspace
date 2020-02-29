/*
 * Copyright (C) 2012-2017 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 3, see README
 */

package ch.fixme.status;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Net {
    private final String USERAGENT = "Android/" + Build.VERSION.RELEASE + " ("
            + Build.MODEL + ") MyHackerspace/" + BuildConfig.VERSION_NAME;

    private static final String TAG = "MyHackerspace_Net";

    private OkHttpClient mClient;
    private Response mResponse;

    public Net(String urlStr) throws Throwable {
        this(urlStr, true);
    }

    public Net(String urlStr, boolean useCache) throws Throwable {
        // Create new client
        mClient = new OkHttpClient();

        // Connect to URL
        final Request request = new Request.Builder()
                .url(urlStr)
                .addHeader("user-agent", USERAGENT)
                .build();
        mResponse = mClient.newCall(request).execute();

        // Ensure success
        if (!mResponse.isSuccessful()) {
            mResponse.close();
            throw new Throwable(mResponse.message());
        }
    }

    public String getString() throws Throwable {
        String value = "";
        if (mResponse != null) {
            final ResponseBody body = mResponse.body();
            if (body != null) {
                value = body.string();
            }
            mResponse.close();
        }
        return value;
    }

    public Bitmap getBitmap() throws Throwable {
        if (mResponse != null) {
            final ResponseBody body = mResponse.body();
            if (body != null) {
                final Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
                mResponse.close();
                return bitmap;
            }
            mResponse.close();
        }
        return null;
    }
}
