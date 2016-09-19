package com.example.simplemusic.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

public class LoadIcon {

    private static LoadIcon sInstance;

    private LruCache<String, Bitmap> mCache;

    public synchronized static LoadIcon getInstance() {
        if (sInstance == null)
            sInstance = new LoadIcon();
        return sInstance;
    }

    private LoadIcon() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mCache = new LruCache<String, Bitmap>(maxSize) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public Bitmap load(final String uri) {
        if (uri == null)
            return null;

        final String key = md5(uri);
        Bitmap bmp = getFromCache(key);

        if (bmp != null)
            return bmp;

        bmp = BitmapFactory.decodeFile(uri);
        addToCache(key, bmp);
        return bmp;
    }

    private Bitmap getFromCache(final String key) {
        return mCache.get(key);
    }

    private void addToCache(final String key, final Bitmap bmp) {
        if (getFromCache(key) == null && key != null && bmp != null)
            mCache.put(key, bmp);
    }

    private String md5(String str) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.reset();
            md.update(str.getBytes("UTF-8"));
            byte[] hash = md.digest();
            int len = hash.length;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len; i++) {
                if (1 == Integer.toHexString(0xFF & hash[i]).length()) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(0xFF & hash[i]));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
