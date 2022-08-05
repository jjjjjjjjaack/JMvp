package com.jjjjjjjjaack.jmvp.app.module.home.base;

import android.content.Context;

import com.jjjjjjjjaack.jmvp.R;

import framework.mvp1.base.f.BaseModel;
import framework.mvp1.base.f.MvpFrag;

public class HomeFrag extends MvpFrag<IHomeView, BaseModel,HomePresenter> implements IHomeView {
    @Override
    public HomePresenter initPresenter() {
        return new HomePresenter();
    }

    @Override
    public void baseInitialization() {

    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_home;
    }

    @Override
    public void viewInitialization() {

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWakeBussiness() {

    }

    @Override
    public Context getMContext() {
        return getContext();
    }
}
