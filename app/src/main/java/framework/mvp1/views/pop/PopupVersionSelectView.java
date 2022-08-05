package framework.mvp1.views.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;


import com.jjjjjjjjaack.jmvp.R;

import framework.mvp1.base.util.JnCache;
import framework.mvp1.base.util.ToolUtils;


/**
 * 是否更新app对话框
 */
public class PopupVersionSelectView {

    public final static String CHECK_VERSION_KEY = "CHECK_VERSION_KEY";

    public Context context;
    public PopupWindow popupWindow;
    public ISelectUpdate selectUpdate;


    public String downLoadUrl = "";
    public int width, height;

    private TextView disagreeUpdate, msg;

    /**
     * 选择是否更新
     *
     * @author Administrator
     */
    public interface ISelectUpdate {

        /**
         * 检测失败，无法使用
         */
        void erro();

        /**
         * 不需要更新
         */
        void doNotUpdate();

        /**
         * 同意更新
         */
        void agreeUpdate();

        /**
         * 拒绝更新
         */
        void disagreeUpdate();
    }


    public PopupVersionSelectView(Activity activity, ISelectUpdate selectUpdate) {
        this.context = activity;
        this.selectUpdate = selectUpdate;
        WindowManager windowManager = activity.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        initPopupView();
    }

    /**
     * 设置新版app更新內容
     *
     * @param msgContent
     */
    public void setContentMsg(String msgContent) {
        msg.setText(msgContent);
    }


    private void initPopupView() {
        View popView = LayoutInflater.from(context).inflate(
                R.layout.popup_selectversion, null);
        popupWindow = new PopupWindow(popView, width, LayoutParams.MATCH_PARENT);
        msg = (TextView) popView.findViewById(R.id.msg);
        disagreeUpdate = (TextView) popView.findViewById(R.id.disagreeUpdate);

        popView.findViewById(R.id.agreeUpdate).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                            if (!hasInstallPermission) {
                                //跳转至“安装未知应用”权限界面，引导用户开启权限
                                Uri selfPackageUri = Uri.parse("package:" + context.getPackageName());
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
                                ((Activity)context).startActivityForResult(intent, 2003);
                                return;
                            }
                        }

                        selectUpdate.agreeUpdate();
						dismiss();
                    }
                });
        disagreeUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				ACache.get(context).put();
                JnCache.saveCache(CHECK_VERSION_KEY, ToolUtils.timestamp2String(System.currentTimeMillis(), "yyyy-MM-dd"));
                selectUpdate.disagreeUpdate();
                dismiss();
            }
        });


        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置popwindow出现和消失动画
        popupWindow.setAnimationStyle(R.style.ScaleAnimStyle);

        popupWindow.setBackgroundDrawable(BitmapDrawable.createFromPath(null));
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 控制界面只展示强制更新
     */
    public void displayConstrantUpdate() {
        disagreeUpdate.setVisibility(View.GONE);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }


    /**
     * 显示popWindow
     */
    public void showPop(View parent) {
        // 获取弹框的真实高度
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.update();
        } else {
            popupWindow.dismiss();
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
        backgroundAlpha(0.5f);
    }

    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
