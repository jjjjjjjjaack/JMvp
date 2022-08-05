package framework.mvp1.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.jjjjjjjjaack.jmvp.R;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static framework.mvp1.base.constant.BROConstant.EXIT_APP_ACTION;

public class ToolUtils {

    /**
     * 字符串转int
     *
     * @param o
     * @return
     */
    public final static int String2Int(String o) {
        if (o != null) {
            try {
                return Double.valueOf(o).intValue();
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * String 转long
     *
     * @param o
     * @return
     */
    public final static long String2Long(String o) {
        if (o != null) {
            try {
                return Long.valueOf(o.trim());
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    /**
     * String 转long
     *
     * @param o
     * @return
     */
    public final static float String2Float(String o) {
        if (o != null) {
            try {
                return Float.valueOf(o.trim());
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * String 转double
     *
     * @param o
     * @return
     */
    public final static double String2Double(String o) {
        if (o != null) {
            try {
                return Double.valueOf(o);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    /*****
     * String 是否空
     * ******/
    public final static boolean isNull(String value) {

        if (value == null || value.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个
        //"\\d{9}"代表后面是可以是0～9的数字，有9位
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }


    /*****
     * 校验蓝牙名字是否合规
     * ******/
    public final static boolean checkBTName(String value) {
        return !ToolUtils.isNull(value) && value.length() <= 8;
    }

    /*****
     * 校验蓝牙名字是否合规
     * ******/
    public final static boolean checkDeviceName(String value) {
        return !ToolUtils.isNull(value) && value.length() <= 8;
    }

    /**
     * 判断是否快速点击
     */
    private static final int FAST_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    private static int viewId;

    public static boolean isFastClick(int id) {
        boolean flag;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = true;
        } else {
            if (id != viewId) {
                flag = true;
            } else {
                flag = false;
            }
        }
        lastClickTime = currentClickTime;
        viewId = id;
        return flag;
    }


    /**
     * 仿IOS底部弹框
     *
     * @param context
     * @param listener
     * @param names
     * @return
     */
    public static CustomSelectDialog showDialog(Context context, CustomSelectDialog.SelectDialogListener listener,
                                                List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(((Activity) context),
                R.style.transparentFrameWindowStyle, listener, names);
        dialog.setItemColor(R.color.c_000000, R.color.c_000000);
        //判断activity是否finish
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 仿IOS底部弹框
     *
     * @param context
     * @param listener
     * @param objects
     * @return
     */
    public static CustomSelectDialog showDialogByObject(Context context, CustomSelectDialog.SelectDialogListener listener,
                                                        List objects) {
        if (objects == null || objects.size() == 0) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Object o : objects) {
            names.add(o.toString());
        }
        CustomSelectDialog dialog = new CustomSelectDialog(((Activity) context),
                R.style.transparentFrameWindowStyle, listener, names);
        dialog.setItemColor(R.color.c_e0b03c, R.color.c_e0b03c);
        //判断activity是否finish
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    public interface OnDissmis {
        void onDiss();
    }

    public static CustomSelectDialog showDialog(Context context, CustomSelectDialog.SelectDialogListener listener,
                                                List<String> names, final OnDissmis onDissmis) {
        CustomSelectDialog dialog = new CustomSelectDialog(((Activity) context),
                R.style.transparentFrameWindowStyle, listener, names);
        dialog.setItemColor(R.color.c_000000, R.color.c_000000);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDissmis.onDiss();
            }
        });
        //判断activity是否finish
        if (!((Activity) context).isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    /**
     * 底部弹框
     *
     * @param popup
     * @param parent
     */
    public static void popupWindowShowCenter(PopupWindow popup, View parent) {
        /* 获取当前系统的android版本号 */
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
            popup.update();
        } else {
            popup.dismiss();
            popup.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 底部弹框
     *
     * @param popup
     * @param parent
     */
    public static void popupWindowShowBottom(PopupWindow popup, View parent) {
        /* 获取当前系统的android版本号 */
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            popup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            popup.update();
        } else {
            popup.dismiss();
            popup.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }


    /**
     * Date类型转换得到时间,精确到秒
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2String(Date date, String format) {
        String sd = "";
        format = (isNull(format) ? "yyyy-MM-dd HH:mm" : format);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sd = sdf.format(date);
        } catch (Exception e) {

        }
        return sd;
    }


    /**
     * Date类型转换得到时间,精确到秒
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String timestamp2String(String timestamp, String format) {
        String sd = "";
        format = (isNull(format) ? "yyyy-MM-dd HH:mm:ss" : format);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sd = sdf.format(new Date(ToolUtils.String2Long(timestamp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sd;
    }

    public static String timestamp2String(long timestamp, String format) {
        String sd = "";
        format = (isNull(format) ? "yyyy-MM-dd HH:mm:ss" : format);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sd = sdf.format(new Date(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sd;
    }


    public static String strReplace(String str) {
        try {
            if (str.length() > 6) {
                return str.substring(0, 5) + "...";
            } else {
                return str;
            }
        } catch (Exception e) {
            return str;
        }

    }

    public static String strReplace(String str, int max) {
        try {
            if (str.length() > max) {
                return str.substring(0, max) + "...";
            } else {
                return str;
            }
        } catch (Exception e) {
            return str;
        }

    }

    /**
     * 秒数格式化
     *
     * @param seconds
     * @return
     */
    public static String formatSeconds(long seconds) {
        String standardTime;
        if (seconds <= 0) {
            standardTime = "00:00";
        } else if (seconds < 60) {
            standardTime = String.format(Locale.getDefault(), "00:%02d", seconds % 60);
        } else if (seconds < 3600) {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        } else {
            standardTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60);
        }
        return standardTime;
    }

    /**
     * 电话掩码
     *
     * @param phone
     * @return
     */
    public static String phoneCover(String phone) {
        String tel = "******";
        try {
            tel = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } catch (Exception e) {

        }
        return tel;
    }

    /***
     *  弹出输入框
     * @param activity
     */
    public static void showSoftInput(Activity activity, EditText et) {
        try {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *  隐藏输入框
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    /***
     *  点击提示开发中
     */
    public static void unUseClickTips(Context context, View view) {
        try {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T.showShort(context, "此功能仍在开发中");
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * @param context
     * @param msg
     */
    public static void copy2Clip(Context context, String msg) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", msg);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            T.showShort(context, "复制成功");
        } catch (Exception e) {

        }
    }

    /**
     * @param context
     * @param msg
     */
    public static void copy2ClipNoTip(Context context, String msg) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", msg);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        } catch (Exception e) {

        }
    }

    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    public static boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        return (y <= bottom && y >= top && x >= left && x <= right);
//        return y in top..bottom && x >= left && x <= right
    }


    /**
     * 横屏可通过 widthPixels - widthPixels2 > 0 来判断底部导航栏是否存在
     *
     * @param windowManager
     * @return true表示有虚拟导航栏 false没有虚拟导航栏
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getNavigationBarHeight(WindowManager windowManager) {
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels;
        //宽度
        int widthPixels = outMetrics.widthPixels;


        //获取内容高度
        DisplayMetrics outMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics2);
        int heightPixels2 = outMetrics2.heightPixels;
        //宽度
        int widthPixels2 = outMetrics2.widthPixels;

        return heightPixels - heightPixels2;
    }


    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }
//2.如果手机存在底部导航栏，判断导航栏是否为显示状态

    public static boolean isNavigationBarShowing(Context context) {
        //判断手机底部是否支持导航栏显示
        boolean haveNavigationBar = checkDeviceHasNavigationBar(context);
        if (haveNavigationBar) {
            if (Build.VERSION.SDK_INT >= 17) {
                String brand = Build.BRAND;
                String mDeviceInfo;
                if (brand.equalsIgnoreCase("HUAWEI")) {
                    mDeviceInfo = "navigationbar_is_min";
                } else if (brand.equalsIgnoreCase("XIAOMI")) {
                    mDeviceInfo = "force_fsg_nav_bar";
                } else if (brand.equalsIgnoreCase("VIVO")) {
                    mDeviceInfo = "navigation_gesture_on";
                } else if (brand.equalsIgnoreCase("OPPO")) {
                    mDeviceInfo = "navigation_gesture_on";
                } else {
                    mDeviceInfo = "navigationbar_is_min";
                }

                if (Settings.Global.getInt(context.getContentResolver(), mDeviceInfo, 0) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // 3.如果上述两个条件都符合，再获取底部导航栏的高度

    public static int getNavHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //判断底部导航栏是否为显示状态
            boolean navigationBarShowing = isNavigationBarShowing(context);
            if (navigationBarShowing) {
                int height = resources.getDimensionPixelSize(resourceId);
                return height;
            }
        }
        return 0;
    }


    public static boolean hasNavigationBar(Context context) {
        //navigationGestureEnabled()从设置中取不到值的话，返回false，因此也不会影响在其他手机上的判断
        return deviceHasNavigationBar() && !navigationGestureEnabled(context);
    }

    /**
     * 获取主流手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @param context app Context
     * @return false 表示使用的是虚拟导航键(NavigationBar)，
     * true 表示使用的是手势， 默认是false
     */
    private static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     *
     * @return
     */
    private static String getDeviceInfo() {
        String brand = Build.BRAND;
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI") || "HONOR".equals(brand)) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("samsung")) {
            return "navigationbar_hide_bar_enabled";
        } else {
            return "navigationbar_is_min";
        }
    }

    /**
     * 判断设备是否存在NavigationBar
     *
     * @return true 存在, false 不存在
     */
    public static boolean deviceHasNavigationBar() {
        boolean haveNav = false;
        try {
            //1.通过WindowManagerGlobal获取windowManagerService
            // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
            Class<?> windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal");
            Method getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService");
            getWmServiceMethod.setAccessible(true);
            //getWindowManagerService是静态方法，所以invoke null
            Object iWindowManager = getWmServiceMethod.invoke(null);

            //2.获取windowMangerService的hasNavigationBar方法返回值
            // 反射方法：haveNav = windowManagerService.hasNavigationBar();
            Class<?> iWindowManagerClass = iWindowManager.getClass();
            Method hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar");
            hasNavBarMethod.setAccessible(true);
            haveNav = (Boolean) hasNavBarMethod.invoke(iWindowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return haveNav;
    }


    /**
     * @param
     * @return
     * @description 重启APP
     * @author jieja
     * @time 2022/2/7 15:10
     */
    public static void reStartApp(Context context) {
        Intent closeIntent = new Intent(EXIT_APP_ACTION);
        context.sendBroadcast(closeIntent);
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
//        Intent intent = new Intent(context, SplashAct.class);
//        intent.putExtra("REBOOT", "reboot");
//        PendingIntent restartIntent = PendingIntent.getActivity(MyApplication.getApp(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * 设置富文本适配手机屏幕
     *
     * @param content 富文本内容
     */
    public static String setWebViewContent(String content) {
        return "<html>\n" +
                "    <head>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <style>img{max-width: 100%; width:100%; height:auto;}</style>\n" +
                "    </head>\n" +
                "    <body style=\"word-wrap:break-word; font-family:Arial;padding:0px 5px 0px 5px;\">" + (content == null ? "" : content.trim()) + " </body></html>";
    }


    /**
     * 设置富文本适配手机屏幕
     *
     * @param content 富文本内容
     */
    public static String setWebViewContent2(String content) {
        return "<html>\n" +
                "    <head>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    </head>\n" +
                "    <body style=\"word-wrap:break-word; font-family:Arial;padding:0px 5px 0px 5px;\">" + (content == null ? "" : content.trim()) + " </body></html>";
    }


    /**
     * @param
     * @return
     * @description 密码校验，必须包含数字字母，不能有特殊字符，长度8位以上
     * @author jiejack
     * @time 2022/5/15 12:15 下午
     */
    public static boolean checkPsw(String newPwd) {
        return checkPsw(null, newPwd);
    }

    public static boolean checkPsw(Context context, String newPwd) {
        //判断密码是否包含数字：包含返回1，不包含返回0
        boolean n1 = newPwd.matches(".*\\d+.*");
        // 判断密码是否包含字母：包含返回1，不包含返回0
        boolean n2 = newPwd.matches(".*[a-zA-Z]+.*");
        //判断密码是否包含特殊符号(~!@#$%^&*()_+|<>,.?/:;'[]{}\)：包含返回1，不包含返回0
        boolean n3 = !newPwd.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*");
        // 判断密码长度是否在6-16位
        boolean n4 = newPwd.length() >= 8;
        if (context != null && !(n1 && n2 && n3 && n4)) {
            T.showShort(context, "密码必须包含数字字母，且长度需8位以上");
        }
        return n1 && n2 && n3 && n4;
    }


    /**
     * @param
     * @return
     * @description 限制禁止输入特殊字符
     * @author jieja
     * @time 2022/6/2 10:58
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (source.equals(" "))
                    return "";
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    //    /**
//     * @param
//     * @return
//     * @description
//     * @author jieja
//     * @time 2022/7/28 14:21
//     */
    public static String shareQRCodeEncryption(String str) {
//        if (str == null) {
//            return null;
//        }
//        String reslut = "";
////        String s[] = reslut.split("");
//        for (int i = 0; i < str.length(); i++) {
//            int value = Character.getNumericValue(str.charAt(i)) ^ 3;
//            char c = Character.forDigit(value, 10);
//            Log.i(MyApplication.JTAG, "the int value is : " +value+ " the convert char value is : "+c);
//            reslut += c;
//        }
//        return reslut;
        if (str == null) {
            return "";
        }
        return new String(encrypt(str.getBytes()));
    }

    /**
     * @description 校验二维码合法性
     * @param 
     * @return 
     * @author jieja
     * @time 2022/7/28 15:44
     */
    public static boolean checkQRCodeIsLegal(String qrStr) {
        try {
            if (qrStr.length() < 5) {
                return false;
            }
            JSONObject jsonObject = JSONObject.parseObject(qrStr);
            String xin = jsonObject.getString("xin");
            String unencryptStr = shareQRCodeEncryption(xin);
            boolean isNum = unencryptStr.matches("[0-9]+");
            if (!isNum) {
                return false;
            }
            String deivceNo = jsonObject.getString("id");
            String xinNo = unencryptStr.substring(5, 7);
            if (xinNo.equals(deivceNo)) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }


    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        for (int i = 0; i < len; i++) {
            bytes[i] ^= 3;
        }
        return bytes;
    }

}
