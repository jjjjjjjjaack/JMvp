package framework.mvp1.base.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.jjjjjjjjaack.jmvp.app.MyApplication;

import java.util.HashMap;
import java.util.Locale;

import framework.mvp1.base.constant.ACEConstant;

/**
 * 多语言的工具类
 */
public class LanguageUtils {

    /**
     * 简体缓存KEY
     */
    public static final String LANGUAGE_SIMPLIFIED_CHINESE = "zh_cn";
    /*
     * 繁体缓存KEY
     */
    public static final String LANGUAGE_TRADITIONAL_CHINESE = "zh_tw";
    /**
     * 英语缓存KEY
     */
    public static final String LANGUAGE_ENGLISH = "en";

    //设置语言集合
    public static HashMap<String, Locale> mAllLanguages = new HashMap<String, Locale>(8) {{
        put(LANGUAGE_SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(LANGUAGE_TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
        put(LANGUAGE_ENGLISH, Locale.ENGLISH);
    }};

    /**
     * 获取缓存的语言
     *
     * @param context
     * @return
     */
    public static String getAppLanguage(Context context) {
        String language_id = JnCache.getCache(context, ACEConstant.CURRENTLANGUAGE_ID);
        if (ToolUtils.isNull(language_id)) {
            language_id = LANGUAGE_SIMPLIFIED_CHINESE;
            JnCache.saveCache(context, ACEConstant.CURRENTLANGUAGE_ID, language_id);
        }
        return language_id;
    }


    /**
     * 修改语言
     */
    public static void changeAppLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        // app locale
        Locale locale = getLocaleByLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        // updateConfiguration  用于覆盖android系统的locale
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    /**
     * 判断指定语言是否存在
     */
    private static boolean isSupportLanguage(String language) {
        return mAllLanguages.containsKey(language);
    }

    /**
     * 获取指定语言的locale信息
     */
    public static String getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return language;
        }

        if (null == language) {//为空则表示首次安装或未选择过语言，获取系统默认语言
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale.getLanguage();
                }
            }
        }
        return LANGUAGE_SIMPLIFIED_CHINESE;
    }

    /**
     * 获取指定语言的locale信息，如果指定语言不存在{@link #mAllLanguages}，返回本机语言，如果本机语言不是语言集合中的一种{@link #mAllLanguages}，返回英语
     *
     * @param language language
     * @return
     */
    public static Locale getLocaleByLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mAllLanguages.get(language);
        } else {
            //本机默认语言
            Locale locale = Locale.getDefault();
            for (String key : mAllLanguages.keySet()) {
                if (TextUtils.equals(mAllLanguages.get(key).getLanguage(), locale.getLanguage())) {
                    return locale;
                }
            }
        }
        return Locale.ENGLISH;
    }

    //重置指定app语言
    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        } else {
            return context;
        }
    }


    //重置指定app语言
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = LanguageUtils.getLocaleByLanguage(language);

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }


    /**
     * 获取程序缓存语言
     *
     * @return
     */
    public static String getCacheLanguageKey() {
        String l = JnCache.getCache(MyApplication.getApp(), ACEConstant.CURRENTLANGUAGE_ID);
        return l == null ? "zh_cn" : l;
    }

}
