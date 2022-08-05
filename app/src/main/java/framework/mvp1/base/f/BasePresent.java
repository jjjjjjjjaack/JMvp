package framework.mvp1.base.f;

import android.content.Context;
import android.content.Intent;

import com.jjjjjjjjaack.jmvp.app.MyApplication;
import com.jjjjjjjjaack.jmvp.app.net.REQ_Factory;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import framework.mvp1.base.exception.NetException;
import framework.mvp1.base.exception.ViewnullException;
import framework.mvp1.base.net.API_Factory;
import framework.mvp1.base.net.BaseRequest;
import framework.mvp1.base.net.NET_CODE;
import framework.mvp1.base.util.FTokenUtils;
import framework.mvp1.base.util.LoadingUtils;
import framework.mvp1.base.util.T;
import framework.mvp1.base.util.ToolUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by hankkin on 2017/3/29.
 */
public abstract class BasePresent<V extends BaseView, M> extends REQ_Factory {

    // 目前来讲有2个公用方法 , 传递的时候 会有不同的View , 怎么办？ 采用泛型

    // 发现个问题，用代理方式回调的时候debug会失败不知道什么原因
    private boolean isDeBug = true;

    private WeakReference<V> mViewReference;
    private V mProxyView;

    public M model;

    /***
     * @param view
     */
    public void attach(V view) {
        if (isDeBug) {
            this.mProxyView = view;
        } else {
            mViewReference = new WeakReference<>(view);
            MvpViewHandler viewHandler = new MvpViewHandler(mViewReference.get());
            mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), viewHandler);
        }
    }

    /**
     * 断开V层和P层
     */
    public void detach() {
        if (isDeBug) {
            return;
        }
        if (isViewAttached()) {
            mViewReference.clear();
            mViewReference = null;
        }

    }

    public V view() {
        return mProxyView;
    }

    /**
     * @return P层和V层是否关联.
     */
    public boolean isViewAttached() {
        if (isDeBug) {
            return true;
        }
        return mViewReference != null && mViewReference.get() != null;
    }

    /**
     * 动态代理处理逻辑
     */
    private class MvpViewHandler implements InvocationHandler {
        private BaseView mvpView;

        MvpViewHandler(BaseView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果V层没被销毁, 执行V层的方法.
            if (isViewAttached()) {
                return method.invoke(mvpView, args);
            }
            //P层不需要关注V层的返回值
            return null;
        }
    }


    public Context context() throws ViewnullException {
        if (!isViewAttached()) {
            throw new ViewnullException();
        }
        return view().getMContext();
    }


    public static <T, K> void doDBBusiness(Context context, T t, boolean showLoading, DoCommRequestInterface<T, K> doCommRequestInterface) {
        if (doCommRequestInterface == null) {
            return;
        }
        Observable.just(t).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (showLoading) {
                    LoadingUtils.getLoadingUtils().showLoadingView(context);
                }
                doCommRequestInterface.doStart();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io()).map(new Func1<T, K>() {
            @Override
            public K call(T t) {
                return doCommRequestInterface.doMap(t);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<K>() {
            @Override
            public void onCompleted() {
                if (showLoading) {
                    LoadingUtils.getLoadingUtils().hideLoadingView(context);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (showLoading) {
                    LoadingUtils.getLoadingUtils().hideLoadingView(context);
                }
                try {
                    doCommRequestInterface.onError(e);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            @Override
            public void onNext(K k) {
                try {
                    doCommRequestInterface.onSuccess(k);
                } catch (Exception e) {
                }
            }
        });
    }

    public <T, K> void doDBBusiness(T t, boolean showLoading, DoCommRequestInterface<T, K> doCommRequestInterface) {
        try {
            doDBBusiness(context(), t, showLoading, doCommRequestInterface);
        } catch (ViewnullException e) {
        }
    }


    public interface DoCommRequestInterface<T, K> {
        void doStart();

        K doMap(T t);

        void onSuccess(K k) throws Exception;

        void onError(Throwable e);
    }

    /**
     * 注解请求引用
     *
     * @param req
     * @return
     */
    public Observable ANN_API_POST_DOCOMM(BaseRequest req) {
        try {
//            if (!req.checkNullTip(context())) {//检测参数是否为空
            if (!req.checkObjectNull(context(), req)) {//检测参数是否为空
                return API_Factory.ANN_BulitParNull();
            }
            return API_Factory.ANN_API_POST_DOCOMM(req);
        } catch (Exception e) {
            return API_Factory.ANN_BulitParNull();
        }
    }


    /**
     * 通常请求快速方法
     *
     * @param request
     * @param showLoading            展示加载框
     * @param erroControl
     * @param doCommRequestInterface
     * @param <T>
     * @param <K>
     */
    public <T, K> void doCommRequest(BaseRequest request, final boolean showLoading, final boolean erroControl, final DoCommRequestInterface<T, K> doCommRequestInterface) {
        doCommRequest(ANN_API_POST_DOCOMM(request), showLoading, erroControl, doCommRequestInterface);
    }

    /**
     * 通常请求快速方法
     *
     * @param api_Observable
     * @param showLoading            展示加载框
     * @param erroControl
     * @param doCommRequestInterface
     * @param <T>
     * @param <K>
     */
    public <T, K> void doCommRequest(Observable<T> api_Observable, final boolean showLoading, final boolean erroControl, final DoCommRequestInterface<T, K> doCommRequestInterface) {
        api_Observable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (showLoading) {
                    try {
                        LoadingUtils.getLoadingUtils().showLoadingView(context());
                    } catch (ViewnullException e) {
                    }
                }
                doCommRequestInterface.doStart();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io()).map(new Func1<T, K>() {
            @Override
            public K call(T t) {
                return doCommRequestInterface.doMap(t);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<K>() {
                    @Override
                    public void onCompleted() {
                        try {
                            if (showLoading) {
                                LoadingUtils.getLoadingUtils().hideLoadingView(context());
                            }
                        } catch (ViewnullException e2) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (erroControl) {
                            try {
                                if (!ToolUtils.isNull(e.getMessage())) {
                                    framework.mvp1.base.util.T.showShort(context(), e.getMessage());
                                }
                            } catch (Exception exception) {
                            }
//                            T(e.getMessage());
                            if (e instanceof NetException) {
                                NetException netException = (NetException) e;
                                switch (netException.netCode) {
                                    case NET_CODE.C_401:
                                        FTokenUtils.doLogout(MyApplication.getApp());
                                        try {
//                                            Intent intent = new Intent(view().getMContext(), LoginAct.class);
//                                            view().getMContext().startActivity(intent);
                                        } catch (Exception ex) {

                                        }
                                        break;
                                }
                            }
                        }
                        try {
                            doCommRequestInterface.onError(e);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        try {
                            if (showLoading) {
                                LoadingUtils.getLoadingUtils().hideLoadingView(context());
                            }
                        } catch (Exception e2) {
                        }
                    }

                    @Override
                    public void onNext(K k) {
                        try {
                            doCommRequestInterface.onSuccess(k);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 注解请求引用
     *
     * @param req
     * @return
     */
    public static Observable STATIC_ANN_API_POST_DOCOMM(Context context, BaseRequest req) {
        try {
            if (!req.checkNullTip(context)) {//检测参数是否为空
                return API_Factory.ANN_BulitParNull();
            }
            return API_Factory.ANN_API_POST_DOCOMM(req);
        } catch (Exception e) {
            return API_Factory.ANN_BulitParNull();
        }
    }

    /**
     * 通常请求快速方法
     *
     * @param showLoading            展示加载框
     * @param erroControl
     * @param doCommRequestInterface
     * @param <T>
     * @param <K>
     */
    public static <T, K> void doStaticCommRequest(final Context context, BaseRequest req, final boolean showLoading, final boolean erroControl, final DoCommRequestInterface<T, K> doCommRequestInterface) {
        STATIC_ANN_API_POST_DOCOMM(context, req).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (showLoading) {
                    LoadingUtils.getLoadingUtils().showLoadingView(context);
                }
                doCommRequestInterface.doStart();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io()).map(new Func1<T, K>() {
            @Override
            public K call(T t) {
                return doCommRequestInterface.doMap(t);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<K>() {
                    @Override
                    public void onCompleted() {
                        if (showLoading) {
                            LoadingUtils.getLoadingUtils().hideLoadingView(context);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (erroControl) {
                            framework.mvp1.base.util.T.showShort(context, e.getMessage());
                        }
                        doCommRequestInterface.onError(e);
                        if (showLoading) {
                            LoadingUtils.getLoadingUtils().hideLoadingView(context);
                        }
                    }

                    @Override
                    public void onNext(K k) {
                        try {
                            doCommRequestInterface.onSuccess(k);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void T(String msg) {
        try {
            if (!ToolUtils.isNull(msg)) {
                T.showLong(context(), msg);
            }
        } catch (Exception e) {
        }
    }

    public void T(int res) {
        try {
            T.showLong(context(), view().getMContext().getString(res));
        } catch (Exception e) {
        }
    }

    public String getString(int id) {
        try {
            return context().getResources().getString(id);
        } catch (Exception e) {
            return "";
        }
    }

    public String getString(int id, Object... formatArgs) {
        try {
            return context().getResources().getString(id, formatArgs);
        } catch (Exception e) {
            return "";
        }
    }
}