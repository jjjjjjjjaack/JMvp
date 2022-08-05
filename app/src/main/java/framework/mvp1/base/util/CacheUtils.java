package framework.mvp1.base.util;

import android.content.Context;

import java.io.Serializable;

import framework.mvp1.base.db.ACache;

public class CacheUtils {
    /**
     * @param context
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
     * @param context
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

}
