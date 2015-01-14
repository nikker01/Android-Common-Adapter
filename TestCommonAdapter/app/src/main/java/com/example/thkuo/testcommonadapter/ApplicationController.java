package com.example.thkuo.testcommonadapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by thkuo on 2014/12/22.
 */
public class ApplicationController extends Application {

    private String TAG = "ApplicationController";

    private RequestQueue mRequestQueue;
    private static ApplicationController mInstance;
    private static Context mAppContext;

    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mAppContext = getApplicationContext();
        //setAppContext(getApplicationContext());

    }

//    public void setAppContext(Context applicationContext) {
//        this.mAppContext = applicationContext;
//    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
        private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }
    });

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
