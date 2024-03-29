package com.example.ggblog;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;

/* Singleton used by HttpGetService to communicate with the Web Server, using Google Volley APIs */
public class NetworkRequestUtils {

    private static final String TAG = "NetworkRequestUtils";

    private static final boolean DBG = Log.isLoggable(TAG, Log.DEBUG);
    private static final boolean VDBG = Log.isLoggable(TAG, Log.VERBOSE);

    private static final int MAX_NUM_ENTRIES_IN_CACHE = 20;

    private static NetworkRequestUtils INSTANCE;
    private RequestQueue mRequestQueue;
    private final ImageLoader mImageLoader;
    private final Context mContext;

    private NetworkRequestUtils(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<>(MAX_NUM_ENTRIES_IN_CACHE);

            @Override
            public Bitmap getBitmap(String url) {
                if (VDBG) Log.d(TAG, "getBitmap");
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                if (VDBG) Log.d(TAG, "putBitmap");
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized NetworkRequestUtils getInstance(Context context) {
        if (VDBG) Log.d(TAG, "getInstance");
        if (INSTANCE == null) {
            INSTANCE = new NetworkRequestUtils(context);
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        if (VDBG) Log.d(TAG, "getRequestQueue");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> Request<T> addToRequestQueue(Request<T> req) {
        if (VDBG) Log.d(TAG, "addToRequestQueue");
        return getRequestQueue().add(req);
    }

    public void cancelAllRequests(String tag) {
        if (VDBG) Log.d(TAG, "cancelAllRequests for TAG=" + tag);
        getRequestQueue().cancelAll(tag);
    }

    /* Clear the whole cache */
    public void clearCache() {
        if (VDBG) Log.d(TAG, "clearCache");
        Cache cache = getRequestQueue().getCache();
        if (cache != null) {
            cache.clear();
        } else {
            if (DBG) Log.d(TAG, "no cache to clear is available");
        }
    }

    /* Clear a specific entry (JSON, bitmap...) of the cache */
    public void clearCache(String url) {
        if (VDBG) Log.d(TAG, "clearCache URL=" + url);
        Cache cache = getRequestQueue().getCache();
        if (cache != null) {
            cache.remove(url);
        } else {
            if (DBG) Log.d(TAG, "no cache to clear is available");
        }
    }

    /* Invalidate a specific entry (JSON, bitmap...) of the cache */
    public void invalidateCache(String url) {
        if (VDBG) Log.d(TAG, "invalidateCache URL=" + url);
        Cache cache = getRequestQueue().getCache();
        if (cache != null) {
            cache.invalidate(url, true);
        } else {
            if (DBG) Log.d(TAG, "no cache to invalidate is available");
        }
    }

    /* The retrieved entry needs to be converted into the correct format (JSON, bitmap...) */
    public Entry getEntryFromCache(String url) {
        if (VDBG) Log.d(TAG, "getEntryFromCache URL=" + url);
        Entry entry = null;
        if (isUrlPresentInCache(url)) {
            entry = getRequestQueue().getCache().get(url);
        }
        return entry;
    }

    public boolean isUrlPresentInCache(String url) {
        if (VDBG) Log.d(TAG, "isUrlPresentInCache URL=" + url);
        boolean isPresent = false;
        Cache cache = getRequestQueue().getCache();
        if (cache != null) {
            if (cache.get(url) != null) {
                isPresent = true;
            } else {
                if (VDBG) Log.d(TAG, "URL not present in cache");
            }
        } else {
            if (DBG) Log.d(TAG, "no cache is available");
        }
        return isPresent;
    }

    public ImageLoader getImageLoader() {
        if (VDBG) Log.d(TAG, "getImageLoader");
        return mImageLoader;
    }
}