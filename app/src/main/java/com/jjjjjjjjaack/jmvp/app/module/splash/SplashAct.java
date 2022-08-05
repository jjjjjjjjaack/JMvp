package com.jjjjjjjjaack.jmvp.app.module.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jjjjjjjjaack.jmvp.R;
import com.jjjjjjjjaack.jmvp.app.module.main.VpMainAct;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import framework.mvp1.base.f.BaseModel;
import framework.mvp1.base.f.MvpAct;
import framework.mvp1.base.util.JnCache;
import framework.mvp1.base.util.PermissionUtils;
import framework.mvp1.base.util.StatusBarUtils;
import framework.mvp1.views.other.IndicatorView;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public class SplashAct extends MvpAct<ISplashView, BaseModel, SplashPresenter> implements ISplashView {
    // 所需的全部权限
    public String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    @BindView(R.id.splash_layout)
    RelativeLayout splash_layout;
    @BindView(R.id.countDown)
    TextView countDown;
    @BindView(R.id.guide_fl)
    View guide_fl;
    @BindView(R.id.guide_vp)
    ViewPager2 guide_vp;
    @BindView(R.id.indicator)
    IndicatorView indicator;

    boolean isjump;

    private int countDownTime = 1;
    private Handler countDownHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (countDownTime >= 1) {
                if (countDown == null)
                    return false;
                countDown.setText(countDownTime + "");
                countDownTime--;
                countDownHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                String first = JnCache.getCache(SplashAct.this, "first_insert");
                if (!"1".equals(first)) {
//                    intentMainAct();
                    showGuidePage();
                } else {
                    intentMainAct();
                }

            }
            return false;
        }
    });

    @Override
    public Context getMContext() {
        return this;
    }

    @Override
    public SplashPresenter initPresenter() {
        return new SplashPresenter();
    }

    @Override
    public void baseInitialization() {
        StatusBarUtils.setImmersiveStatusBar(true, Color.BLACK, this);
    }

    @Override
    public int setR_Layout() {
        return R.layout.act_splash;
    }

    @Override
    public void viewInitialization() {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                SplashAct.this.doNotUpdate();
                break;
        }
    }

    private void doNotUpdate() {
        doAnimStart();
    }

    public void doAnimStart() {
        try {
            // 渐变展示启动屏
            AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
            aa.setDuration(1000);
            splash_layout.startAnimation(aa);
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
//                    countDown.setVisibility(View.VISIBLE);
                    countDownHandler.sendEmptyMessage(0);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void doBusiness() {
        //重新加载启动页的时候判断是否已经加载过一次，
        //防止部分机型每次返回home再返回就重新启动
        if (!isTaskRoot() && getIntent() != null) {
            String action = getIntent().getAction();
            if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        if (PermissionUtils.lacksPermissions(this, PERMISSIONS)) {//要求所有权限
            ActivityCompat.requestPermissions(SplashAct.this, PERMISSIONS, 1);
        } else {
            SplashAct.this.doNotUpdate();
//            CheckUpdatesVersionUtil.getInstance().doVersionCheck(SplashAct.this, splash_layout, new PopupVersionSelectView.ISelectUpdate() {
//                @Override
//                public void erro() {
//                    SplashAct.this.doNotUpdate();
//                }
//
//                @Override
//                public void doNotUpdate() {
//                    SplashAct.this.doNotUpdate();
//                }
//
//                @Override
//                public void agreeUpdate() {
//                    CheckUpdatesVersionUtil.getInstance().readyDownload(SplashAct.this);
//                }
//
//                @Override
//                public void disagreeUpdate() {
//                    SplashAct.this.doNotUpdate();
//                }
//            });
        }
    }

    @Override
    public void doWakeUpBusiness() {

    }

    /**
     * 进入主界面
     */
    private void intentMainAct() {
        VpMainAct.openMainAct(getMContext());
    }

    private void showGuidePage() {
        guide_fl.setVisibility(View.VISIBLE);
        indicator.setTotalIndex(4);
        indicator.setCurrentIndex(0);
        guide_vp.setOffscreenPageLimit(3);
        guide_vp.setAdapter(new ViewPagerAdapter(this));
        guide_vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.setCurrentIndex(position);
            }
        });
    }


    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

        public Context context;
        public LayoutInflater mLayoutInflater;
        public List<String> titletx = Arrays.asList(getString(R.string.guide_tx1), getString(R.string.guide_tx2), getString(R.string.guide_tx3), getString(R.string.guide_tx4));

        public ViewPagerAdapter(Context context) {
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.view_welcome_guide, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
            holder.bindViews(titletx.get(position),position);
        }

        @Override
        public int getItemCount() {
            return titletx.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv;
            public TextView tv1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
                tv1 = itemView.findViewById(R.id.tv1);
            }

            public void bindViews(String t1, int postiton) {
                tv1.setText(t1);
                if (postiton != titletx.size() - 1) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JnCache.saveCache(SplashAct.this, "first_insert", "1");
                            intentMainAct();
                        }
                    });
                }
            }
        }

    }

    @Override
    public void doReleaseSomething() {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
