package framework.mvp1.base.util;

import android.content.Context;


import com.jjjjjjjjaack.jmvp.app.MyApplication;

import java.io.Serializable;

import framework.mvp1.base.db.ACache;

public class JnCache {

    /**
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Serializable> boolean saveCache(String key, T value) {
        return saveCache(MyApplication.getApp(), key, value);
    }

    /**
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Serializable> boolean saveCache(Context context, String key, T value) {
        ACache.get(context).put(key, value);
        return true;
    }


    /**
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Serializable> boolean saveCache(Context context, String key, T value, int cacheTime) {
        ACache.get(context).put(key, value, cacheTime);
        return true;
    }

    /**
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getCache(Context context, String key) {
        try {
            return (T) ACache.get(context).getAsObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public static boolean removeCache(Context context, String key) {
        ACache.get(context).remove(key);
        return true;
    }

}
