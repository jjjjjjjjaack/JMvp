package framework.mvp1.base.f;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lzj on 2017/7/1.
 */

public abstract class BaseFrag extends Fragment {

    private Unbinder unbinder;
    public View mainView;
    //是否执行唤醒监听
    public boolean wakeListener = true;
    /**
     * 基本初始化工作放在这个方法 如 P类
     */
    public abstract void baseInitialization();

    public abstract int setR_Layout();

    /**
     * 控件初始化工作放在这个方法
     */
    public abstract void viewInitialization();

    /**
     * 业务逻辑放在这个方法 如获取网络数据
     */
    public abstract void doBusiness();


    public abstract void onWakeBussiness();

    //setUserVisibleHint是在onCreateView之前调用的
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInitialization();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        //   return super.onCreateView(inflater, container, savedInstanceState);
        super.onCreateView(inflater, container, savedInstanceState);
        if (mainView == null) {
            mainView = inflater.inflate(setR_Layout(), container, false);
            unbinder = ButterKnife.bind(this, mainView);
            viewInitialization();
        }
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doBusiness();
    }

    //唤醒
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && wakeListener) {
            onWakeBussiness();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainView = null;
        unbinder.unbind();
        unbinder = null;
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(getActivity(), clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            getActivity().finish();
        }
    }

}
