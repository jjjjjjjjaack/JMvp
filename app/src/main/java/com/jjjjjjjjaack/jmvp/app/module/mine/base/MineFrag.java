package com.jjjjjjjjaack.jmvp.app.module.mine.base;

import android.content.Context;

import com.jjjjjjjjaack.jmvp.R;

import framework.mvp1.base.f.BaseModel;
import framework.mvp1.base.f.MvpFrag;

public class MineFrag extends MvpFrag<IMineView, BaseModel, MinePresenter> implements IMineView {
    @Override
    public MinePresenter initPresenter() {
        return new MinePresenter();
    }

    @Override
    public void baseInitialization() {

    }

    @Override
    public int setR_Layout() {
        return R.layout.frag_mine;
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
