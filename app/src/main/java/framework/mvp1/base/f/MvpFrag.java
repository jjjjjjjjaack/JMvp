package framework.mvp1.base.f;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


/**
 * MVP模式 Fragment 继承类
 */

public abstract class MvpFrag<V extends BaseView, M, P extends BasePresent<V, M>> extends BaseFrag {


    protected P presenter;

    public abstract P initPresenter();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = initPresenter();
        presenter.attach((V) this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }
}
