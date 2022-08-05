package framework.mvp1.base.f;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.jjjjjjjjaack.jmvp.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import framework.mvp1.base.constant.BROConstant;
import framework.mvp1.base.constant.IETConstant;
import framework.mvp1.base.util.LanguageUtils;
import framework.mvp1.base.util.LoadingUtils;
import framework.mvp1.base.util.StatusBarUtils;
import framework.mvp1.base.util.ToolUtils;

import static framework.mvp1.base.constant.BROConstant.CLOSE_TRAGETACT_ACTION;

public abstract class BaseAct extends AppCompatActivity {
    public String className;
    private View mViewStatusBarPlace;
    private FrameLayout mFrameLayoutContent;
    public boolean styleControl = false;

    public boolean exitControlFlag = true;


    @Override
    public Resources getResources() {
        //禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    public Unbinder unbinder;
    private BroadcastReceiver exitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BROConstant.EXIT_APP_ACTION:
                    finish();
                    break;
                case CLOSE_TRAGETACT_ACTION:
                    try {
                        String act_name = intent.getStringExtra(IETConstant.CLOSE_TRAGETACT_KEY);
                        if (ToolUtils.isNull(className)) {
                            return;
                        }
                        if (ToolUtils.isNull(act_name)) {
                            return;
                        }
                        if (act_name.contains(className)) {
                            finish();
                        }
                    } catch (Exception e) {
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 基本初始化工作放在这个方法 如 P类
     */
    public abstract void baseInitialization();

    /**
     * 设置布局文件
     */
    public abstract int setR_Layout();

    /**
     * 控件初始化工作放在这个方法
     */
    public abstract void viewInitialization();

    /**
     * 业务逻辑放在这个方法 如获取网络数据
     */
    public abstract void doBusiness();

    /**
     * 业务逻辑放在这个方法 如获取网络数据
     */
    public abstract void doWakeUpBusiness();

    /**
     * 释放
     */
    public abstract void doReleaseSomething();

    /**
     * 多语言需要注入
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        String language = LanguageUtils.getAppLanguage(base);
        super.attachBaseContext(LanguageUtils.attachBaseContext(base, language));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        className = this.getClass().getSimpleName();
        baseInitialization();
        super.onCreate(savedInstanceState);
//        if (styleControl) {
//            super.setContentView(R.layout.act_compat_status_bar);
//            mViewStatusBarPlace = findViewById(R.id.view_status_bar_place);
//            mFrameLayoutContent = (FrameLayout) findViewById(R.id.frame_layout_content_place);
//            ViewGroup.LayoutParams params = mViewStatusBarPlace.getLayoutParams();
//            params.height = StatusBarUtils.getStatusBarHeight(this);
//            mViewStatusBarPlace.setLayoutParams(params);
//            View contentView = LayoutInflater.from(this).inflate(setR_Layout(), null);
//            mFrameLayoutContent.addView(contentView);
//        } else {
        setContentView(setR_Layout());
//        }
        //初始化注解
        unbinder = ButterKnife.bind(this);
        registrtExitBro();
        viewInitialization();
        doBusiness();
    }


    @Override
    protected void onResume() {
        super.onResume();
        doWakeUpBusiness();
    }

    /**
     * 注册退出程序
     */
    protected void registrtExitBro() {
        if (exitControlFlag) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BROConstant.EXIT_APP_ACTION);
            filter.addAction(CLOSE_TRAGETACT_ACTION);
            registerReceiver(exitReceiver, filter);
        }
    }


//    public void setStatusBarPlaceColor(int statusColor) {
//        if (mViewStatusBarPlace != null) {
//            mViewStatusBarPlace.setBackgroundColor(getResources().getColor(R.color.c_638ee3));
//        }
//    }
//
//    public void commitSetStatysStyleCfffff() {
//        if (styleControl) {
//            setStatusBarPlaceColor(getResources().getColor(R.color.c_ffffff));
//            StatusBarUtils.setImmersiveStatusBar(true, Color.BLACK, this);
//        }
//    }
//
//
//    public void commitSetStatysStyle(int color, boolean isDark) {
//        if (styleControl) {
//            setStatusBarPlaceColor(color);
//            StatusBarUtils.setImmersiveStatusBar(isDark, color, this);
////            if(isDark) {
////                StatusBarUtils.setImmersiveStatusBar(true, color, this);
////            }else{
////                StatusBarUtils.setImmersiveStatusBar(false, color, this);
////            }
//        }
//    }
//
//    public void setStatusBarColor(int color) {
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(color);
//        }
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            findViewById(android.R.id.content).setForeground(null);
//        }
//    }

    public void setStatusBarComm(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exitControlFlag) {
            unregisterReceiver(exitReceiver);
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        doReleaseSomething();
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
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    /**
     * @param title
     */
    public void setMTitle(String title) {
        try {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        } catch (Exception e) {

        }
    }

    /**
     * @param ids
     */
    public void setMTitle(int ids) {
        try {
            ((TextView) findViewById(R.id.tv_title)).setText(getString(ids));
        } catch (Exception e) {

        }
    }


    /**
     *
     */
    public void setBackPress() {
        try {
            findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
        }
    }

//    /**
//     * @description
//     * @param
//     * @return
//     * @author jieja
//     * @time 2022/6/2 12:49
//     */
//    public void setRightTextColorHight() {
//        try {
//            ((TextView)findViewById(R.id.tv_back_right)).setTextColor(getResources().getColor(R.color.c_e0b03c));
//        } catch (Exception e) {
//        }
//    }

    public void sendBrocastCloseTragetAct(String className) {
        Intent intent = new Intent();
        intent.setAction(CLOSE_TRAGETACT_ACTION);
        intent.putExtra(IETConstant.CLOSE_TRAGETACT_KEY, className);
        sendBroadcast(intent);
    }

    public void showLoadingAutoHide() {
        LoadingUtils.getLoadingUtils().showLoadingView(BaseAct.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadingUtils.getLoadingUtils().hideLoadingView(BaseAct.this);
            }
        }, 20 * 1000);
    }

    public void openWebUrlBySystem(String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }catch (Exception e){
        }
    }

}
