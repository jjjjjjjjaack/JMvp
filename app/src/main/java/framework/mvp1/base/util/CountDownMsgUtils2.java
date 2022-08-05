package framework.mvp1.base.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jjjjjjjjaack.jmvp.R;
import com.jjjjjjjjaack.jmvp.app.MyApplication;


/**
 * 短信验证码的倒计时
 * Created by Administrator on 2018/4/9 0009.
 */

public class CountDownMsgUtils2 {

    private long TIMECODE_MAX;//只赋值一次
    private String ACacheKey;
    private Context context;
    private boolean isRelease = false;
    /**
     * 请求验证码的按钮
     */
    private TextView requestBtn;
    private ICountDownPostCode countDownPostCode;
    private Handler mcodeCountHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (isRelease) {
                return true;
            }
            long nowTime = System.currentTimeMillis();
            long cacheTime = ToolUtils.String2Long(JnCache.getCache(MyApplication.getApp(), ACacheKey));
            final long c = (nowTime - cacheTime) / 1000;
            if (cacheTime == 0) {
                return true;
            }
            if (cacheTime != 0 && c > TIMECODE_MAX) {
                JnCache.saveCache(MyApplication.getApp(), ACacheKey, "0");
                requestBtn.setText(MyApplication.getApp().getString(R.string.register_send_code));//获取验证码
            } else {
                mcodeCountHandler.sendEmptyMessageDelayed(0, 1000);
//                requestBtn.setText(String.format(context.getString(R.string.registeract_surplus), (TIMECODE_MAX - c) + "s"));//剩余%1$s
                if ((TIMECODE_MAX - c) == 0) {
                    requestBtn.setText(MyApplication.getApp().getString(R.string.register_send_code));
                } else {
                    requestBtn.setText(MyApplication.getApp().getString(R.string.register_send_code_1, (TIMECODE_MAX - c + "")));//剩余%1$s
                }
            }
            return true;
        }
    });


    /**
     * @param ACacheKey
     * @param context
     * @param TIMECODE_MAX
     * @param requestBtn   要绑定控件
     */
    public CountDownMsgUtils2(Context context, String ACacheKey, long TIMECODE_MAX, TextView requestBtn) {
        this.ACacheKey = ACacheKey;
        this.context = context;
        this.TIMECODE_MAX = TIMECODE_MAX;
        this.requestBtn = requestBtn;
        if (this.requestBtn == null) {
            return;
        }
        mcodeCountHandler.sendEmptyMessage(0);
    }

    /**
     * 放在请求验证码的点击事件中，经内部判断后才允许向服务器请求验证码
     *
     * @param countDownPostCode
     */
    public void requestCheckCode(String checkCode, ICountDownPostCode countDownPostCode) {
        if (!ToolUtils.isNull(checkCode)) {
            this.countDownPostCode = countDownPostCode;
            long nowTime = System.currentTimeMillis();
            long cacheTime = ToolUtils.String2Long(JnCache.getCache(MyApplication.getApp(), ACacheKey));
            final long c = (nowTime - cacheTime) / 1000;
            if (cacheTime == 0 || c > TIMECODE_MAX) {
                countDownPostCode.allowToRequestCode();
            } else {
                T.showShort(context, context.getString(R.string.msg_code));//短信已发送,请稍后点击
            }
        } else {
            T.showShort(context, context.getString(R.string.msg_code_null));//手机号码不正确或者手机号为空
        }

    }

    public void requestSuccess() {
        JnCache.saveCache(MyApplication.getApp(), ACacheKey, System.currentTimeMillis() + "");
        mcodeCountHandler.sendEmptyMessage(0);
    }

    public interface ICountDownPostCode {
        /**
         * 允许请求验证码按钮点击
         */
        void allowToRequestCode();
    }

    public void releaseUtils() {
        this.context = null;
        this.isRelease = true;
    }

}


