package framework.mvp1.base.exception;

import android.annotation.TargetApi;
import android.os.Build;

import framework.mvp1.base.net.BaseResponse;

/**
 * Created by LB on 2018/4/8.
 */

public class NetException extends Exception {

    public int netCode;
    public String message;
    public BaseResponse baseResponse;

    public NetException() {
    }

    public NetException(int netCode, String message) {
        super(message);
        this.netCode = netCode;
        this.message = message;
    }

    public NetException(int netCode, String message,BaseResponse baseResponse) {
        super(message);
        this.netCode = netCode;
        this.message = message;
        this.baseResponse  = baseResponse;
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public NetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
