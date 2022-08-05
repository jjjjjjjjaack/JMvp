package framework.mvp1.base.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import framework.mvp1.views.catloading.CatLoadingView;

public class LoadingUtils {

    private volatile static LoadingUtils INSTANCE; //声明成 volatile
    private Object loadingView;

    private List loadingMap = new ArrayList<>();

    public static LoadingUtils getLoadingUtils() {
        if (INSTANCE == null) {
            synchronized (LoadingUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoadingUtils();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 加载的动画
     *
     * @param context
     */
    public void showLoadingView(Context context) {
        if (context == null || isLoading()) {
            return;
        }
        if (loadingView == null) {
            loadingView = loadingAnimation(context);
        } else {
//            loadingDismiss(context, loadingView);
            loadingView = loadingAnimation(context);
        }

        loadingMap.add(loadingView);
    }

    public void hideLoadingView(Context context) {
//        if (loadingView != null) {
//            loadingDismiss(context, loadingView);
//        }
        synchronized (this) {
            try {
                for (Object o : loadingMap) {
                    loadingDismiss(context ,o);
                }
                loadingMap.clear();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 加载动画
     */
    private final Object loadingAnimation(Context context) {
        CatLoadingView mView = CatLoadingView.createInstance();
        mView.showView(context);
        return mView;
    }

    private final boolean isLoading() {
        return false;
    }

    private final void loadingDismiss(Context context, Object object) {
        ((CatLoadingView) object).hideView(context);
    }
}
